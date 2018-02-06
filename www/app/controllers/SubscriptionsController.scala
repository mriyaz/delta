package controllers

import io.flow.common.v0.models.{User, UserReference}
import io.flow.delta.v0.models.{Publication, SubscriptionForm}
import io.flow.delta.www.lib.{DeltaClientProvider, UiData}
import io.flow.play.controllers.FlowControllerComponents
import io.flow.play.util.Config
import play.api.i18n._
import play.api.mvc._

import scala.concurrent.Future

object Subscriptions {

  case class UserPublication(publication: Publication, isSubscribed: Boolean) {
    val label = publication match {
      case Publication.Deployments => "Email me whenever a new deployment is created"
      case Publication.UNDEFINED(key) => key
    }
  }

}

class SubscriptionsController @javax.inject.Inject() (
  override val config: Config,
  override val messagesApi: MessagesApi,
  override val tokenClient: io.flow.token.v0.interfaces.Client,
  override val deltaClientProvider: DeltaClientProvider,
  override val controllerComponents: ControllerComponents,
  override val flowControllerComponents: FlowControllerComponents
) extends BaseController(tokenClient, deltaClientProvider, controllerComponents, flowControllerComponents) with I18nSupport {

  import scala.concurrent.ExecutionContext.Implicits.global

  override def section = Some(io.flow.delta.www.lib.Section.Events)

  lazy val client = deltaClientProvider.newClient(user = None, requestId = None)

  def index() = Action.async { implicit request =>
    Helpers.userFromSession(tokenClient, request.session).flatMap { userOption =>
      userOption match {
        case None => Future {
          Redirect(routes.LoginController.index(return_url = Some(request.path)))
        }
        case Some(user) => {
          deltaClientProvider.newClient(user = Some(user), requestId = None).users.getIdentifierById(user.id).map { id =>
            Redirect(routes.SubscriptionsController.identifier(id.value))
          }
        }
      }
    }
  }

  def identifier(identifier: String) = Action.async { implicit request =>
    for {
      users <- client.users.get(
        identifier = Some(identifier)
      )
      user <- Future { users.headOption.map(u => UserReference(u.id)) }
      subscriptions <- deltaClientProvider.newClient(user = user, requestId = None).subscriptions.get(
        identifier = Some(identifier),
        limit = Publication.all.size + 1
      )
    } yield {
      val userPublications = Publication.all.map { p =>
        Subscriptions.UserPublication(
          publication = p,
          isSubscribed = !subscriptions.find(_.publication == p).isEmpty
            )
      }
      Ok(views.html.subscriptions.identifier(uiData(request, users.headOption), identifier, userPublications))
    }
  }

  def postToggle(identifier: String, publication: Publication) = Action.async { implicit request =>
    client.users.get(identifier = Some(identifier)).flatMap { users =>
      users.headOption match {
        case None => Future {
          Redirect(routes.SubscriptionsController.index()).flashing("warning" -> "User could not be found")
        }
        case Some(user) => {
          val identifiedClient = deltaClientProvider.newClient(user = Some(UserReference(user.id)), requestId = None)

          identifiedClient.subscriptions.get(
            identifier = Some(identifier),
            publication = Some(publication)
          ).flatMap { subscriptions =>
            subscriptions.headOption match {
              case None => {
                identifiedClient.subscriptions.post(
                  SubscriptionForm(
                    userId = user.id,
                    publication = publication
                  ),
                  identifier = Some(identifier)
                ).map { _ =>
                  Redirect(routes.SubscriptionsController.identifier(identifier)).flashing("success" -> "Subscription added")
                }
              }
              case Some(subscription) => {
                identifiedClient.subscriptions.deleteById(
                  subscription.id,
                  identifier = Some(identifier)
                ).map { _ =>
                  Redirect(routes.SubscriptionsController.identifier(identifier)).flashing("success" -> "Subscription removed")
                }
              }
            }
          }
        }
      }
    }
  }

  def uiData[T](request: Request[T], user: Option[User]): UiData = {
    UiData(
      requestPath = request.path,
      user = user,
      section = Some(io.flow.delta.www.lib.Section.Subscriptions)
    )
  }

}
