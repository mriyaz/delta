/**
 * Generated by apidoc - http://www.apidoc.me
 * Service version: 0.0.3
 * apidoc:0.11.24 http://www.apidoc.me/flow/docker-hub/0.0.3/play_2_4_client
 */
package io.flow.docker.hub.v0.models {

  case class Jwt(
    token: String
  )

  case class JwtForm(
    username: String,
    password: String
  )

}

package io.flow.docker.hub.v0.models {

  package object json {
    import play.api.libs.json.__
    import play.api.libs.json.JsString
    import play.api.libs.json.Writes
    import play.api.libs.functional.syntax._
    import io.flow.docker.hub.v0.models.json._

    private[v0] implicit val jsonReadsUUID = __.read[String].map(java.util.UUID.fromString)

    private[v0] implicit val jsonWritesUUID = new Writes[java.util.UUID] {
      def writes(x: java.util.UUID) = JsString(x.toString)
    }

    private[v0] implicit val jsonReadsJodaDateTime = __.read[String].map { str =>
      import org.joda.time.format.ISODateTimeFormat.dateTimeParser
      dateTimeParser.parseDateTime(str)
    }

    private[v0] implicit val jsonWritesJodaDateTime = new Writes[org.joda.time.DateTime] {
      def writes(x: org.joda.time.DateTime) = {
        import org.joda.time.format.ISODateTimeFormat.dateTime
        val str = dateTime.print(x)
        JsString(str)
      }
    }

    implicit def jsonReadsDockerHubJwt: play.api.libs.json.Reads[Jwt] = {
      (__ \ "token").read[String].map { x => new Jwt(token = x) }
    }

    def jsObjectJwt(obj: io.flow.docker.hub.v0.models.Jwt) = {
      play.api.libs.json.Json.obj(
        "token" -> play.api.libs.json.JsString(obj.token)
      )
    }

    implicit def jsonWritesDockerHubJwt: play.api.libs.json.Writes[Jwt] = {
      new play.api.libs.json.Writes[io.flow.docker.hub.v0.models.Jwt] {
        def writes(obj: io.flow.docker.hub.v0.models.Jwt) = {
          jsObjectJwt(obj)
        }
      }
    }

    implicit def jsonReadsDockerHubJwtForm: play.api.libs.json.Reads[JwtForm] = {
      (
        (__ \ "username").read[String] and
        (__ \ "password").read[String]
      )(JwtForm.apply _)
    }

    def jsObjectJwtForm(obj: io.flow.docker.hub.v0.models.JwtForm) = {
      play.api.libs.json.Json.obj(
        "username" -> play.api.libs.json.JsString(obj.username),
        "password" -> play.api.libs.json.JsString(obj.password)
      )
    }

    implicit def jsonWritesDockerHubJwtForm: play.api.libs.json.Writes[JwtForm] = {
      new play.api.libs.json.Writes[io.flow.docker.hub.v0.models.JwtForm] {
        def writes(obj: io.flow.docker.hub.v0.models.JwtForm) = {
          jsObjectJwtForm(obj)
        }
      }
    }
  }
}

package io.flow.docker.hub.v0 {

  object Bindables {

    import play.api.mvc.{PathBindable, QueryStringBindable}
    import org.joda.time.{DateTime, LocalDate}
    import org.joda.time.format.ISODateTimeFormat
    import io.flow.docker.hub.v0.models._

    // Type: date-time-iso8601
    implicit val pathBindableTypeDateTimeIso8601 = new PathBindable.Parsing[org.joda.time.DateTime](
      ISODateTimeFormat.dateTimeParser.parseDateTime(_), _.toString, (key: String, e: _root_.java.lang.Exception) => s"Error parsing date time $key. Example: 2014-04-29T11:56:52Z"
    )

    implicit val queryStringBindableTypeDateTimeIso8601 = new QueryStringBindable.Parsing[org.joda.time.DateTime](
      ISODateTimeFormat.dateTimeParser.parseDateTime(_), _.toString, (key: String, e: _root_.java.lang.Exception) => s"Error parsing date time $key. Example: 2014-04-29T11:56:52Z"
    )

    // Type: date-iso8601
    implicit val pathBindableTypeDateIso8601 = new PathBindable.Parsing[org.joda.time.LocalDate](
      ISODateTimeFormat.yearMonthDay.parseLocalDate(_), _.toString, (key: String, e: _root_.java.lang.Exception) => s"Error parsing date $key. Example: 2014-04-29"
    )

    implicit val queryStringBindableTypeDateIso8601 = new QueryStringBindable.Parsing[org.joda.time.LocalDate](
      ISODateTimeFormat.yearMonthDay.parseLocalDate(_), _.toString, (key: String, e: _root_.java.lang.Exception) => s"Error parsing date $key. Example: 2014-04-29"
    )



  }

}


package io.flow.docker.hub.v0 {

  object Constants {

    val BaseUrl = "https://hub.docker.com"
    val Namespace = "io.flow.docker.hub.v0"
    val UserAgent = "apidoc:0.11.24 http://www.apidoc.me/flow/docker-hub/0.0.3/play_2_4_client"
    val Version = "0.0.3"
    val VersionMajor = 0

  }

  class Client(
    val baseUrl: String = "https://hub.docker.com",
    auth: scala.Option[io.flow.docker.hub.v0.Authorization] = None,
    defaultHeaders: Seq[(String, String)] = Nil
  ) extends interfaces.Client {
    import io.flow.docker.hub.v0.models.json._

    private[this] val logger = play.api.Logger("io.flow.docker.hub.v0.Client")

    logger.info(s"Initializing io.flow.docker.hub.v0.Client for url $baseUrl")

    def jwts: Jwts = Jwts

    object Jwts extends Jwts {
      override def postLogin(
        jwtForm: io.flow.docker.hub.v0.models.JwtForm,
        requestHeaders: Seq[(String, String)] = Nil
      )(implicit ec: scala.concurrent.ExecutionContext): scala.concurrent.Future[io.flow.docker.hub.v0.models.Jwt] = {
        val payload = play.api.libs.json.Json.toJson(jwtForm)

        _executeRequest("POST", s"/v2/users/login/", body = Some(payload), requestHeaders = requestHeaders).map {
          case r if r.status == 201 => _root_.io.flow.docker.hub.v0.Client.parseJson("io.flow.docker.hub.v0.models.Jwt", r, _.validate[io.flow.docker.hub.v0.models.Jwt])
          case r if r.status == 400 => throw new io.flow.docker.hub.v0.errors.UnitResponse(r.status)
          case r if r.status == 401 => throw new io.flow.docker.hub.v0.errors.UnitResponse(r.status)
          case r => throw new io.flow.docker.hub.v0.errors.FailedRequest(r.status, s"Unsupported response code[${r.status}]. Expected: 201, 400, 401")
        }
      }
    }

    def _requestHolder(path: String): play.api.libs.ws.WSRequest = {
      import play.api.Play.current

      val holder = play.api.libs.ws.WS.url(baseUrl + path).withHeaders(
        "User-Agent" -> Constants.UserAgent,
        "X-Apidoc-Version" -> Constants.Version,
        "X-Apidoc-Version-Major" -> Constants.VersionMajor.toString
      ).withHeaders(defaultHeaders : _*)
      auth.fold(holder) {
        case Authorization.Basic(username, password) => {
          holder.withAuth(username, password.getOrElse(""), play.api.libs.ws.WSAuthScheme.BASIC)
        }
        case a => sys.error("Invalid authorization scheme[" + a.getClass + "]")
      }
    }

    def _logRequest(method: String, req: play.api.libs.ws.WSRequest)(implicit ec: scala.concurrent.ExecutionContext): play.api.libs.ws.WSRequest = {
      val queryComponents = for {
        (name, values) <- req.queryString
        value <- values
      } yield s"$name=$value"
      val url = s"${req.url}${queryComponents.mkString("?", "&", "")}"
      auth.fold(logger.info(s"curl -X $method $url")) { _ =>
        logger.info(s"curl -X $method -u '[REDACTED]:' $url")
      }
      req
    }

    def _executeRequest(
      method: String,
      path: String,
      queryParameters: Seq[(String, String)] = Nil,
      requestHeaders: Seq[(String, String)] = Nil,
      body: Option[play.api.libs.json.JsValue] = None
    )(implicit ec: scala.concurrent.ExecutionContext): scala.concurrent.Future[play.api.libs.ws.WSResponse] = {
      method.toUpperCase match {
        case "GET" => {
          _logRequest("GET", _requestHolder(path).withHeaders(requestHeaders:_*).withQueryString(queryParameters:_*)).get()
        }
        case "POST" => {
          _logRequest("POST", _requestHolder(path).withHeaders(_withJsonContentType(requestHeaders):_*).withQueryString(queryParameters:_*)).post(body.getOrElse(play.api.libs.json.Json.obj()))
        }
        case "PUT" => {
          _logRequest("PUT", _requestHolder(path).withHeaders(_withJsonContentType(requestHeaders):_*).withQueryString(queryParameters:_*)).put(body.getOrElse(play.api.libs.json.Json.obj()))
        }
        case "PATCH" => {
          _logRequest("PATCH", _requestHolder(path).withHeaders(requestHeaders:_*).withQueryString(queryParameters:_*)).patch(body.getOrElse(play.api.libs.json.Json.obj()))
        }
        case "DELETE" => {
          _logRequest("DELETE", _requestHolder(path).withHeaders(requestHeaders:_*).withQueryString(queryParameters:_*)).delete()
        }
         case "HEAD" => {
          _logRequest("HEAD", _requestHolder(path).withHeaders(requestHeaders:_*).withQueryString(queryParameters:_*)).head()
        }
         case "OPTIONS" => {
          _logRequest("OPTIONS", _requestHolder(path).withHeaders(requestHeaders:_*).withQueryString(queryParameters:_*)).options()
        }
        case _ => {
          _logRequest(method, _requestHolder(path).withHeaders(requestHeaders:_*).withQueryString(queryParameters:_*))
          sys.error("Unsupported method[%s]".format(method))
        }
      }
    }

    /**
     * Adds a Content-Type: application/json header unless the specified requestHeaders
     * already contain a Content-Type header
     */
    def _withJsonContentType(headers: Seq[(String, String)]): Seq[(String, String)] = {
      headers.find { _._1.toUpperCase == "CONTENT-TYPE" } match {
        case None => headers ++ Seq(("Content-Type" -> "application/json; charset=UTF-8"))
        case Some(_) => headers
      }
    }

  }

  object Client {

    def parseJson[T](
      className: String,
      r: play.api.libs.ws.WSResponse,
      f: (play.api.libs.json.JsValue => play.api.libs.json.JsResult[T])
    ): T = {
      f(play.api.libs.json.Json.parse(r.body)) match {
        case play.api.libs.json.JsSuccess(x, _) => x
        case play.api.libs.json.JsError(errors) => {
          throw new io.flow.docker.hub.v0.errors.FailedRequest(r.status, s"Invalid json for class[" + className + "]: " + errors.mkString(" "))
        }
      }
    }

  }

  sealed trait Authorization
  object Authorization {
    case class Basic(username: String, password: Option[String] = None) extends Authorization
  }

  package interfaces {

    trait Client {
      def baseUrl: String
      def jwts: io.flow.docker.hub.v0.Jwts
    }

  }

  trait Jwts {
    def postLogin(
      jwtForm: io.flow.docker.hub.v0.models.JwtForm,
      requestHeaders: Seq[(String, String)] = Nil
    )(implicit ec: scala.concurrent.ExecutionContext): scala.concurrent.Future[io.flow.docker.hub.v0.models.Jwt]
  }

  package errors {

    import io.flow.docker.hub.v0.models.json._

    case class UnitResponse(status: Int) extends Exception(s"HTTP $status")

    case class FailedRequest(responseCode: Int, message: String, requestUri: Option[_root_.java.net.URI] = None) extends _root_.java.lang.Exception(s"HTTP $responseCode: $message")

  }

}