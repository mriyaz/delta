package io.flow.delta.actors

import io.flow.delta.v0.models.{Publication, SubscriptionForm}
import io.flow.common.v0.models.UserReference
import io.flow.play.actors.ErrorHandler
import db.{OrganizationsDao, SubscriptionsDao, UserIdentifiersDao, UsersDao}
import akka.actor.Actor
import scala.concurrent.ExecutionContext

object UserActor {

  trait Message

  object Messages {
    case class Data(id: String) extends Message
    case object Created extends Message
  }

}

class UserActor extends Actor with ErrorHandler {

  var dataUser: Option[UserReference] = None

  def receive = {

    case msg @ UserActor.Messages.Data(id) => withVerboseErrorHandler(msg) {
      dataUser = UsersDao.findById(id)
    }

    case msg @ UserActor.Messages.Created => withVerboseErrorHandler(msg) {
      dataUser.foreach { user =>
        // This method will force create an identifier
        UserIdentifiersDao.latestForUser(MainActor.SystemUser, user)

        // Subscribe the user automatically to key personalized emails.
        Seq(Publication.Deployments).foreach { publication =>
          SubscriptionsDao.upsert(
            MainActor.SystemUser,
            SubscriptionForm(
              userId = user.id,
              publication = publication
            )
          )
        }
      }
    }

    case msg: Any => logUnhandledMessage(msg)
  }

}
