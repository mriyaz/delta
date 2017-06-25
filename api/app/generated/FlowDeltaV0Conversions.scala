/**
 * Generated by API Builder - https://www.apibuilder.io
 * Service version: 0.3.90
 * apibuilder:0.12.3 https://api.apibuilder.io/flow/delta/0.3.90/anorm_2_x_parsers
 */
package io.flow.delta.v0.anorm.conversions {

  import anorm.{Column, MetaDataItem, TypeDoesNotMatch}
  import play.api.libs.json.{JsArray, JsObject, JsValue}
  import scala.util.{Failure, Success, Try}

  /**
    * Conversions to collections of objects using JSON.
    */
  object Util {

    def parser[T](
      f: play.api.libs.json.JsValue => T
    ) = anorm.Column.nonNull { (value, meta) =>
      val MetaDataItem(columnName, nullable, clazz) = meta
      value match {
        case json: org.postgresql.util.PGobject => parseJson(f, columnName.qualified, json.getValue)
        case json: java.lang.String => parseJson(f, columnName.qualified, json)
        case _=> {
          Left(
            TypeDoesNotMatch(
              s"Column[${columnName.qualified}] error converting $value to Json. Expected instance of type[org.postgresql.util.PGobject] and not[${value.asInstanceOf[AnyRef].getClass}]"
            )
          )
        }


      }
    }

    private[this] def parseJson[T](f: play.api.libs.json.JsValue => T, columnName: String, value: String) = {
      Try {
        f(
          play.api.libs.json.Json.parse(value)
        )
      } match {
        case Success(result) => Right(result)
        case Failure(ex) => Left(
          TypeDoesNotMatch(
            s"Column[$columnName] error parsing json $value: $ex"
          )
        )
      }
    }

  }

  object Types {
    import io.flow.delta.v0.models.json._
    implicit val columnToSeqDeltaDockerProvider: Column[Seq[_root_.io.flow.delta.v0.models.DockerProvider]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.DockerProvider]] }
    implicit val columnToMapDeltaDockerProvider: Column[Map[String, _root_.io.flow.delta.v0.models.DockerProvider]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.DockerProvider]] }
    implicit val columnToSeqDeltaEventType: Column[Seq[_root_.io.flow.delta.v0.models.EventType]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.EventType]] }
    implicit val columnToMapDeltaEventType: Column[Map[String, _root_.io.flow.delta.v0.models.EventType]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.EventType]] }
    implicit val columnToSeqDeltaPublication: Column[Seq[_root_.io.flow.delta.v0.models.Publication]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Publication]] }
    implicit val columnToMapDeltaPublication: Column[Map[String, _root_.io.flow.delta.v0.models.Publication]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Publication]] }
    implicit val columnToSeqDeltaRole: Column[Seq[_root_.io.flow.delta.v0.models.Role]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Role]] }
    implicit val columnToMapDeltaRole: Column[Map[String, _root_.io.flow.delta.v0.models.Role]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Role]] }
    implicit val columnToSeqDeltaScms: Column[Seq[_root_.io.flow.delta.v0.models.Scms]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Scms]] }
    implicit val columnToMapDeltaScms: Column[Map[String, _root_.io.flow.delta.v0.models.Scms]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Scms]] }
    implicit val columnToSeqDeltaStatus: Column[Seq[_root_.io.flow.delta.v0.models.Status]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Status]] }
    implicit val columnToMapDeltaStatus: Column[Map[String, _root_.io.flow.delta.v0.models.Status]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Status]] }
    implicit val columnToSeqDeltaVisibility: Column[Seq[_root_.io.flow.delta.v0.models.Visibility]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Visibility]] }
    implicit val columnToMapDeltaVisibility: Column[Map[String, _root_.io.flow.delta.v0.models.Visibility]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Visibility]] }
    implicit val columnToSeqDeltaAwsActor: Column[Seq[_root_.io.flow.delta.v0.models.AwsActor]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.AwsActor]] }
    implicit val columnToMapDeltaAwsActor: Column[Map[String, _root_.io.flow.delta.v0.models.AwsActor]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.AwsActor]] }
    implicit val columnToSeqDeltaBuild: Column[Seq[_root_.io.flow.delta.v0.models.Build]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Build]] }
    implicit val columnToMapDeltaBuild: Column[Map[String, _root_.io.flow.delta.v0.models.Build]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Build]] }
    implicit val columnToSeqDeltaBuildState: Column[Seq[_root_.io.flow.delta.v0.models.BuildState]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.BuildState]] }
    implicit val columnToMapDeltaBuildState: Column[Map[String, _root_.io.flow.delta.v0.models.BuildState]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.BuildState]] }
    implicit val columnToSeqDeltaDashboardBuild: Column[Seq[_root_.io.flow.delta.v0.models.DashboardBuild]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.DashboardBuild]] }
    implicit val columnToMapDeltaDashboardBuild: Column[Map[String, _root_.io.flow.delta.v0.models.DashboardBuild]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.DashboardBuild]] }
    implicit val columnToSeqDeltaDocker: Column[Seq[_root_.io.flow.delta.v0.models.Docker]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Docker]] }
    implicit val columnToMapDeltaDocker: Column[Map[String, _root_.io.flow.delta.v0.models.Docker]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Docker]] }
    implicit val columnToSeqDeltaEvent: Column[Seq[_root_.io.flow.delta.v0.models.Event]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Event]] }
    implicit val columnToMapDeltaEvent: Column[Map[String, _root_.io.flow.delta.v0.models.Event]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Event]] }
    implicit val columnToSeqDeltaGithubAuthenticationForm: Column[Seq[_root_.io.flow.delta.v0.models.GithubAuthenticationForm]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.GithubAuthenticationForm]] }
    implicit val columnToMapDeltaGithubAuthenticationForm: Column[Map[String, _root_.io.flow.delta.v0.models.GithubAuthenticationForm]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.GithubAuthenticationForm]] }
    implicit val columnToSeqDeltaGithubUser: Column[Seq[_root_.io.flow.delta.v0.models.GithubUser]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.GithubUser]] }
    implicit val columnToMapDeltaGithubUser: Column[Map[String, _root_.io.flow.delta.v0.models.GithubUser]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.GithubUser]] }
    implicit val columnToSeqDeltaGithubUserForm: Column[Seq[_root_.io.flow.delta.v0.models.GithubUserForm]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.GithubUserForm]] }
    implicit val columnToMapDeltaGithubUserForm: Column[Map[String, _root_.io.flow.delta.v0.models.GithubUserForm]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.GithubUserForm]] }
    implicit val columnToSeqDeltaGithubWebhook: Column[Seq[_root_.io.flow.delta.v0.models.GithubWebhook]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.GithubWebhook]] }
    implicit val columnToMapDeltaGithubWebhook: Column[Map[String, _root_.io.flow.delta.v0.models.GithubWebhook]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.GithubWebhook]] }
    implicit val columnToSeqDeltaImage: Column[Seq[_root_.io.flow.delta.v0.models.Image]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Image]] }
    implicit val columnToMapDeltaImage: Column[Map[String, _root_.io.flow.delta.v0.models.Image]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Image]] }
    implicit val columnToSeqDeltaImageForm: Column[Seq[_root_.io.flow.delta.v0.models.ImageForm]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.ImageForm]] }
    implicit val columnToMapDeltaImageForm: Column[Map[String, _root_.io.flow.delta.v0.models.ImageForm]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.ImageForm]] }
    implicit val columnToSeqDeltaItem: Column[Seq[_root_.io.flow.delta.v0.models.Item]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Item]] }
    implicit val columnToMapDeltaItem: Column[Map[String, _root_.io.flow.delta.v0.models.Item]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Item]] }
    implicit val columnToSeqDeltaMembership: Column[Seq[_root_.io.flow.delta.v0.models.Membership]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Membership]] }
    implicit val columnToMapDeltaMembership: Column[Map[String, _root_.io.flow.delta.v0.models.Membership]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Membership]] }
    implicit val columnToSeqDeltaMembershipForm: Column[Seq[_root_.io.flow.delta.v0.models.MembershipForm]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.MembershipForm]] }
    implicit val columnToMapDeltaMembershipForm: Column[Map[String, _root_.io.flow.delta.v0.models.MembershipForm]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.MembershipForm]] }
    implicit val columnToSeqDeltaOrganization: Column[Seq[_root_.io.flow.delta.v0.models.Organization]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Organization]] }
    implicit val columnToMapDeltaOrganization: Column[Map[String, _root_.io.flow.delta.v0.models.Organization]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Organization]] }
    implicit val columnToSeqDeltaOrganizationForm: Column[Seq[_root_.io.flow.delta.v0.models.OrganizationForm]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.OrganizationForm]] }
    implicit val columnToMapDeltaOrganizationForm: Column[Map[String, _root_.io.flow.delta.v0.models.OrganizationForm]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.OrganizationForm]] }
    implicit val columnToSeqDeltaOrganizationSummary: Column[Seq[_root_.io.flow.delta.v0.models.OrganizationSummary]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.OrganizationSummary]] }
    implicit val columnToMapDeltaOrganizationSummary: Column[Map[String, _root_.io.flow.delta.v0.models.OrganizationSummary]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.OrganizationSummary]] }
    implicit val columnToSeqDeltaProject: Column[Seq[_root_.io.flow.delta.v0.models.Project]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Project]] }
    implicit val columnToMapDeltaProject: Column[Map[String, _root_.io.flow.delta.v0.models.Project]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Project]] }
    implicit val columnToSeqDeltaProjectForm: Column[Seq[_root_.io.flow.delta.v0.models.ProjectForm]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.ProjectForm]] }
    implicit val columnToMapDeltaProjectForm: Column[Map[String, _root_.io.flow.delta.v0.models.ProjectForm]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.ProjectForm]] }
    implicit val columnToSeqDeltaProjectSummary: Column[Seq[_root_.io.flow.delta.v0.models.ProjectSummary]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.ProjectSummary]] }
    implicit val columnToMapDeltaProjectSummary: Column[Map[String, _root_.io.flow.delta.v0.models.ProjectSummary]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.ProjectSummary]] }
    implicit val columnToSeqDeltaReference: Column[Seq[_root_.io.flow.delta.v0.models.Reference]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Reference]] }
    implicit val columnToMapDeltaReference: Column[Map[String, _root_.io.flow.delta.v0.models.Reference]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Reference]] }
    implicit val columnToSeqDeltaRepository: Column[Seq[_root_.io.flow.delta.v0.models.Repository]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Repository]] }
    implicit val columnToMapDeltaRepository: Column[Map[String, _root_.io.flow.delta.v0.models.Repository]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Repository]] }
    implicit val columnToSeqDeltaSha: Column[Seq[_root_.io.flow.delta.v0.models.Sha]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Sha]] }
    implicit val columnToMapDeltaSha: Column[Map[String, _root_.io.flow.delta.v0.models.Sha]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Sha]] }
    implicit val columnToSeqDeltaState: Column[Seq[_root_.io.flow.delta.v0.models.State]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.State]] }
    implicit val columnToMapDeltaState: Column[Map[String, _root_.io.flow.delta.v0.models.State]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.State]] }
    implicit val columnToSeqDeltaStateForm: Column[Seq[_root_.io.flow.delta.v0.models.StateForm]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.StateForm]] }
    implicit val columnToMapDeltaStateForm: Column[Map[String, _root_.io.flow.delta.v0.models.StateForm]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.StateForm]] }
    implicit val columnToSeqDeltaSubscription: Column[Seq[_root_.io.flow.delta.v0.models.Subscription]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Subscription]] }
    implicit val columnToMapDeltaSubscription: Column[Map[String, _root_.io.flow.delta.v0.models.Subscription]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Subscription]] }
    implicit val columnToSeqDeltaSubscriptionForm: Column[Seq[_root_.io.flow.delta.v0.models.SubscriptionForm]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.SubscriptionForm]] }
    implicit val columnToMapDeltaSubscriptionForm: Column[Map[String, _root_.io.flow.delta.v0.models.SubscriptionForm]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.SubscriptionForm]] }
    implicit val columnToSeqDeltaTag: Column[Seq[_root_.io.flow.delta.v0.models.Tag]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Tag]] }
    implicit val columnToMapDeltaTag: Column[Map[String, _root_.io.flow.delta.v0.models.Tag]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Tag]] }
    implicit val columnToSeqDeltaToken: Column[Seq[_root_.io.flow.delta.v0.models.Token]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Token]] }
    implicit val columnToMapDeltaToken: Column[Map[String, _root_.io.flow.delta.v0.models.Token]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Token]] }
    implicit val columnToSeqDeltaTokenForm: Column[Seq[_root_.io.flow.delta.v0.models.TokenForm]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.TokenForm]] }
    implicit val columnToMapDeltaTokenForm: Column[Map[String, _root_.io.flow.delta.v0.models.TokenForm]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.TokenForm]] }
    implicit val columnToSeqDeltaUserForm: Column[Seq[_root_.io.flow.delta.v0.models.UserForm]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.UserForm]] }
    implicit val columnToMapDeltaUserForm: Column[Map[String, _root_.io.flow.delta.v0.models.UserForm]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.UserForm]] }
    implicit val columnToSeqDeltaUserIdentifier: Column[Seq[_root_.io.flow.delta.v0.models.UserIdentifier]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.UserIdentifier]] }
    implicit val columnToMapDeltaUserIdentifier: Column[Map[String, _root_.io.flow.delta.v0.models.UserIdentifier]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.UserIdentifier]] }
    implicit val columnToSeqDeltaUserSummary: Column[Seq[_root_.io.flow.delta.v0.models.UserSummary]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.UserSummary]] }
    implicit val columnToMapDeltaUserSummary: Column[Map[String, _root_.io.flow.delta.v0.models.UserSummary]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.UserSummary]] }
    implicit val columnToSeqDeltaUsernamePassword: Column[Seq[_root_.io.flow.delta.v0.models.UsernamePassword]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.UsernamePassword]] }
    implicit val columnToMapDeltaUsernamePassword: Column[Map[String, _root_.io.flow.delta.v0.models.UsernamePassword]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.UsernamePassword]] }
    implicit val columnToSeqDeltaVariable: Column[Seq[_root_.io.flow.delta.v0.models.Variable]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Variable]] }
    implicit val columnToMapDeltaVariable: Column[Map[String, _root_.io.flow.delta.v0.models.Variable]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Variable]] }
    implicit val columnToSeqDeltaVariableForm: Column[Seq[_root_.io.flow.delta.v0.models.VariableForm]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.VariableForm]] }
    implicit val columnToMapDeltaVariableForm: Column[Map[String, _root_.io.flow.delta.v0.models.VariableForm]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.VariableForm]] }
    implicit val columnToSeqDeltaVersion: Column[Seq[_root_.io.flow.delta.v0.models.Version]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.Version]] }
    implicit val columnToMapDeltaVersion: Column[Map[String, _root_.io.flow.delta.v0.models.Version]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.Version]] }
    implicit val columnToSeqDeltaItemSummary: Column[Seq[_root_.io.flow.delta.v0.models.ItemSummary]] = Util.parser { _.as[Seq[_root_.io.flow.delta.v0.models.ItemSummary]] }
    implicit val columnToMapDeltaItemSummary: Column[Map[String, _root_.io.flow.delta.v0.models.ItemSummary]] = Util.parser { _.as[Map[String, _root_.io.flow.delta.v0.models.ItemSummary]] }
  }

  object Standard {
    implicit val columnToJsObject: Column[play.api.libs.json.JsObject] = Util.parser { _.as[play.api.libs.json.JsObject] }
    implicit val columnToSeqBoolean: Column[Seq[Boolean]] = Util.parser { _.as[Seq[Boolean]] }
    implicit val columnToMapBoolean: Column[Map[String, Boolean]] = Util.parser { _.as[Map[String, Boolean]] }
    implicit val columnToSeqDouble: Column[Seq[Double]] = Util.parser { _.as[Seq[Double]] }
    implicit val columnToMapDouble: Column[Map[String, Double]] = Util.parser { _.as[Map[String, Double]] }
    implicit val columnToSeqInt: Column[Seq[Int]] = Util.parser { _.as[Seq[Int]] }
    implicit val columnToMapInt: Column[Map[String, Int]] = Util.parser { _.as[Map[String, Int]] }
    implicit val columnToSeqLong: Column[Seq[Long]] = Util.parser { _.as[Seq[Long]] }
    implicit val columnToMapLong: Column[Map[String, Long]] = Util.parser { _.as[Map[String, Long]] }
    implicit val columnToSeqLocalDate: Column[Seq[_root_.org.joda.time.LocalDate]] = Util.parser { _.as[Seq[_root_.org.joda.time.LocalDate]] }
    implicit val columnToMapLocalDate: Column[Map[String, _root_.org.joda.time.LocalDate]] = Util.parser { _.as[Map[String, _root_.org.joda.time.LocalDate]] }
    implicit val columnToSeqDateTime: Column[Seq[_root_.org.joda.time.DateTime]] = Util.parser { _.as[Seq[_root_.org.joda.time.DateTime]] }
    implicit val columnToMapDateTime: Column[Map[String, _root_.org.joda.time.DateTime]] = Util.parser { _.as[Map[String, _root_.org.joda.time.DateTime]] }
    implicit val columnToSeqBigDecimal: Column[Seq[BigDecimal]] = Util.parser { _.as[Seq[BigDecimal]] }
    implicit val columnToMapBigDecimal: Column[Map[String, BigDecimal]] = Util.parser { _.as[Map[String, BigDecimal]] }
    implicit val columnToSeqJsObject: Column[Seq[_root_.play.api.libs.json.JsObject]] = Util.parser { _.as[Seq[_root_.play.api.libs.json.JsObject]] }
    implicit val columnToMapJsObject: Column[Map[String, _root_.play.api.libs.json.JsObject]] = Util.parser { _.as[Map[String, _root_.play.api.libs.json.JsObject]] }
    implicit val columnToSeqString: Column[Seq[String]] = Util.parser { _.as[Seq[String]] }
    implicit val columnToMapString: Column[Map[String, String]] = Util.parser { _.as[Map[String, String]] }
    implicit val columnToSeqUUID: Column[Seq[_root_.java.util.UUID]] = Util.parser { _.as[Seq[_root_.java.util.UUID]] }
    implicit val columnToMapUUID: Column[Map[String, _root_.java.util.UUID]] = Util.parser { _.as[Map[String, _root_.java.util.UUID]] }
  }

}