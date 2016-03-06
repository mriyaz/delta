package db

import io.flow.delta.actors.MainActor
import io.flow.delta.lib.Semver
import io.flow.delta.v0.models.{OrganizationSummary, ProjectSummary, Tag}
import io.flow.postgresql.{Authorization, Query, OrderBy}
import io.flow.common.v0.models.User
import anorm._
import play.api.db._
import play.api.Play.current
import play.api.libs.json._

case class TagForm(
  projectId: String,
  name: String,
  hash: String
)

object TagsDao {

  private[this] val BaseQuery = Query(s"""
    select tags.id,
           tags.created_at,
           tags.name,
           tags.hash,
           projects.id as project_id,
           projects.name as project_name,
           projects.uri as project_uri,
           projects.organization_id as project_organization_id
      from tags
      join projects on tags.project_id = projects.id
  """)


  def findLatestByProjectId(auth: Authorization, projectId: String): Option[Tag] = {
    findAll(auth, projectId = Some(projectId), orderBy = OrderBy("-tags.created_at"), limit = 1).headOption
  }

  def findByProjectIdAndName(auth: Authorization, projectId: String, name: String): Option[Tag] = {
    findAll(auth, projectId = Some(projectId), name = Some(name), limit = 1).headOption
  }

  def findById(auth: Authorization, id: String): Option[Tag] = {
    findAll(auth, ids = Some(Seq(id)), limit = 1).headOption
  }

  def findAll(
    auth: Authorization,
    ids: Option[Seq[String]] = None,
    projectId: Option[String] = None,
    name: Option[String] = None,
    orderBy: OrderBy = OrderBy("-tags.sort_key, tags.created_at"),
    limit: Long = 25,
    offset: Long = 0
  ): Seq[Tag] = {

    DB.withConnection { implicit c =>
      Standards.query(
        BaseQuery,
        tableName = "tags",
        auth = Filters(auth).organizations("projects.organization_id"),
        ids = ids,
        orderBy = orderBy.sql,
        limit = limit,
        offset = offset
      ).
        equals("projects.id", projectId).
        optionalText(
          "tags.name",
          name,
          columnFunctions = Seq(Query.Function.Lower),
          valueFunctions = Seq(Query.Function.Lower, Query.Function.Trim)
        ).
        as(
          io.flow.delta.v0.anorm.parsers.Tag.parser().*
        )
    }
  }
}

case class TagsWriteDao @javax.inject.Inject() (
  @javax.inject.Named("main-actor") mainActor: akka.actor.ActorRef
) {
 
  private[this] val InsertQuery = """
    insert into tags
    (id, project_id, name, hash, sort_key, updated_by_user_id)
    values
    ({id}, {project_id}, {name}, {hash}, {sort_key}, {updated_by_user_id})
  """

  private[this] val UpdateQuery = """
    update tags
       set project_id = {project_id},
           name = {name},
           hash = {hash},
           sort_key = {sort_key},
           updated_by_user_id = {updated_by_user_id}
     where id = {id}
  """

  private[db] def validate(
    user: User,
    form: TagForm,
    existing: Option[Tag] = None
  ): Seq[String] = {
    val nameErrors = if (form.name.trim == "") {
      Seq("Name cannot be empty")
    } else {
      Semver.isSemver(form.name.trim) match {
        case true => Nil
        case false => Seq("Name must match semver pattern (e.g. 0.1.2)")
      }
    }

    val hashErrors = if (form.hash.trim == "") {
      Seq("Hash cannot be empty")
    } else {
      Nil
    }

    val projectErrors = ProjectsDao.findById(Authorization.All, form.projectId) match {
      case None => Seq("Project not found")
      case Some(project) => Nil
    }

    val existingErrors = TagsDao.findByProjectIdAndName(Authorization.All, form.projectId, form.name) match {
      case None => Nil
      case Some(found) => {
        existing.map(_.id) == Some(found.id) match {
          case true => Nil
          case false => Seq("Project already has a tag with this name")
        }
      }
    }

    nameErrors ++ hashErrors ++ projectErrors ++ existingErrors
  }

  /**
    * If the tag exists, updates the hash to match (if
    * necessary). Otherwise creates the tag.
    */
  def upsert(createdBy: User, projectId: String, tag: String, hash: String): Tag = {
    val form = TagForm(
      projectId = projectId,
      name = tag,
      hash = hash
    )
    TagsDao.findByProjectIdAndName(Authorization.All, projectId, tag) match {
      case None => {
        create(createdBy, form) match {
          case Left(errors) => sys.error(errors.mkString(", "))
          case Right(tag) => tag
        }
      }
      case Some(existing) => {
        existing.hash == hash match {
          case true => existing
          case false => update(createdBy, existing, form) match {
            case Left(errors) => sys.error(errors.mkString(", "))
            case Right(tag) => tag
          }
        }
      }
    }
  }

  def create(createdBy: User, form: TagForm): Either[Seq[String], Tag] = {
    validate(createdBy, form) match {
      case Nil => {
        val id = io.flow.play.util.IdGenerator("tag").randomId()

        DB.withConnection { implicit c =>
          SQL(InsertQuery).on(
            'id -> id,
            'project_id -> form.projectId,
            'name -> form.name.trim,
            'hash -> form.hash.trim,
            'sort_key -> Util.generateVersionSortKey(form.name.trim),
            'updated_by_user_id -> createdBy.id
          ).execute()
        }

        mainActor ! MainActor.Messages.TagCreated(form.projectId, id, form.name.trim)

        Right(
          TagsDao.findById(Authorization.All, id).getOrElse {
            sys.error("Failed to create tag")
          }
        )
      }
      case errors => {
        Left(errors)
      }
    }
  }

  private[this] def update(createdBy: User, tag: Tag, form: TagForm): Either[Seq[String], Tag] = {
    validate(createdBy, form, Some(tag)) match {
      case Nil => {
        DB.withConnection { implicit c =>
          SQL(UpdateQuery).on(
            'id -> tag.id,
            'project_id -> form.projectId,
            'name -> form.name.trim,
            'hash -> form.hash.trim,
            'sort_key -> Util.generateVersionSortKey(form.name.trim),
            'updated_by_user_id -> createdBy.id
          ).execute()
        }

        mainActor ! MainActor.Messages.TagUpdated(form.projectId, tag.id, form.name.trim)

        Right(
          TagsDao.findById(Authorization.All, tag.id).getOrElse {
            sys.error("Failed to create tag")
          }
        )
      }
      case errors => {
        Left(errors)
      }
    }
  }

  def delete(deletedBy: User, tag: Tag) {
    Delete.delete("tags", deletedBy.id, tag.id)
  }

}
