package db

import io.flow.postgresql.Authorization
import org.scalatest._
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
import java.util.UUID

class ShasDaoSpec extends PlaySpec with OneAppPerSuite with Helpers {

  import scala.concurrent.ExecutionContext.Implicits.global

  "create" in {
    val project = createProject()
    val hash = createTestKey()
    val form = createShaForm(project).copy(branch = "master", hash = hash)
    val sha = rightOrErrors(ShasDao.create(systemUser, form))
    sha.project.id must be(project.id)
    sha.branch must be("master")
    sha.hash must be(hash)
  }

  "upsertMaster" in {
    val project = createProject()

    val hash = createTestKey()

    val sha = ShasDao.upsertMaster(systemUser, project.id, hash)
    sha.hash must be(hash)

    val sha2 = ShasDao.upsertMaster(systemUser, project.id, hash)
    sha2.hash must be(hash)

    val other = createTestKey()
    val sha3 = ShasDao.upsertMaster(systemUser, project.id, other)
    sha3.hash must be(other)
  }

  "delete" in {
    val sha = createSha()
    ShasDao.delete(systemUser, sha)
    ShasDao.findById(Authorization.All, sha.id) must be(None)
  }

  "findById" in {
    val sha = createSha()
    ShasDao.findById(Authorization.All, sha.id).map(_.id) must be(
      Some(sha.id)
    )

    ShasDao.findById(Authorization.All, UUID.randomUUID.toString) must be(None)
  }

  "findByProjectIdAndBranch" in {
    val project = createProject()

    val masterForm = createShaForm(project).copy(branch = "master")
    val master = rightOrErrors(ShasDao.create(systemUser, masterForm))

    val fooForm = createShaForm(project).copy(branch = "foo")
    val foo = rightOrErrors(ShasDao.create(systemUser, fooForm))

    ShasDao.findByProjectIdAndBranch(Authorization.All, project.id, "master").map(_.hash) must be(Some(masterForm.hash))
    ShasDao.findByProjectIdAndBranch(Authorization.All, project.id, "foo").map(_.hash) must be(Some(fooForm.hash))
    ShasDao.findByProjectIdAndBranch(Authorization.All, project.id, "other") must be(None)
  }

  "findAll by ids" in {
    val sha1 = createSha()
    val sha2 = createSha()

    ShasDao.findAll(Authorization.All, ids = Some(Seq(sha1.id, sha2.id))).map(_.id).sorted must be(
      Seq(sha1.id, sha2.id).sorted
    )

    ShasDao.findAll(Authorization.All, ids = Some(Nil)) must be(Nil)
    ShasDao.findAll(Authorization.All, ids = Some(Seq(UUID.randomUUID.toString))) must be(Nil)
    ShasDao.findAll(Authorization.All, ids = Some(Seq(sha1.id, UUID.randomUUID.toString))).map(_.id) must be(Seq(sha1.id))
  }

  "findAll by projectId" in {
    val project1 = createProject()
    val project2 = createProject()

    val sha1 = createSha(createShaForm(project1))
    val sha2 = createSha(createShaForm(project2))

    ShasDao.findAll(Authorization.All, projectId = Some(project1.id)).map(_.id).sorted must be(
      Seq(sha1.id)
    )

    ShasDao.findAll(Authorization.All, projectId = Some(project2.id)).map(_.id).sorted must be(
      Seq(sha2.id)
    )

    ShasDao.findAll(Authorization.All, projectId = Some(createTestKey())) must be(Nil)
  }

  "validate" must {

    "require sha" in {
      ShasDao.validate(systemUser, createShaForm().copy(hash = "   ")) must be(
        Seq("Hash cannot be empty")
      )
    }

    "require branch" in {
      ShasDao.validate(systemUser, createShaForm().copy(branch = "   ")) must be(
        Seq("Branch cannot be empty")
      )
    }

    "validate project exists" in {
      ShasDao.validate(systemUser, createShaForm().copy(projectId = createTestKey())) must be(
        Seq("Project not found")
      )
    }
 
    "validate existing record" in {
      val form = createShaForm()
      val sha = createSha(form)

      ShasDao.validate(systemUser, form) must be(
        Seq("Project already has a hash for this branch")
      )

      ShasDao.validate(systemUser, form.copy(branch = createTestKey())) must be(Nil)
    }

  }

  "authorization for shas" in {
    val org = createOrganization()
    val project = createProject(org)
    val user = createUser()
    createMembership(createMembershipForm(org = org, user = user))

    val sha = createSha(createShaForm(project), user = user)

    ShasDao.findAll(Authorization.PublicOnly, ids = Some(Seq(sha.id))) must be(Nil)
    ShasDao.findAll(Authorization.All, ids = Some(Seq(sha.id))).map(_.id) must be(Seq(sha.id))
    ShasDao.findAll(Authorization.Organization(org.id), ids = Some(Seq(sha.id))).map(_.id) must be(Seq(sha.id))
    ShasDao.findAll(Authorization.Organization(createOrganization().id), ids = Some(Seq(sha.id))) must be(Nil)
    ShasDao.findAll(Authorization.User(user.id), ids = Some(Seq(sha.id))).map(_.id) must be(Seq(sha.id))
    ShasDao.findAll(Authorization.User(createUser().id), ids = Some(Seq(sha.id))) must be(Nil)
  }

}