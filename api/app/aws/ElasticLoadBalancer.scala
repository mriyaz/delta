package io.flow.delta.aws

import io.flow.delta.api.lib.RegistryClient

import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient
import com.amazonaws.services.elasticloadbalancing.model._

import collection.JavaConverters._

import play.api.libs.concurrent.Akka
import play.api.Play.current
import scala.concurrent.Future

case class ElasticLoadBalancer(settings: Settings, registryClient: RegistryClient) extends Credentials {

  private[this] implicit val executionContext = Akka.system.dispatchers.lookup("ec2-context")

  private[this] lazy val client = new AmazonElasticLoadBalancingClient(awsCredentials)

  def getLoadBalancerName(projectId: String): String = s"$projectId-ecs-lb"

  def getHealthyInstances(projectId: String): Future[Seq[String]] = {
    val loadBalancerName = getLoadBalancerName(projectId)
    Future {
      client.describeInstanceHealth(
        new DescribeInstanceHealthRequest()
        .withLoadBalancerName(loadBalancerName)
      ).getInstanceStates.asScala.filter(_.getState == "InService").map(_.getInstanceId)
    }
  }

  def createLoadBalancerAndHealthCheck(projectId: String): Future[String] = {
    // create the load balancer first, then configure healthcheck
    // they do not allow this in a single API call
    val name = getLoadBalancerName(projectId)

    registryClient.getById(projectId).map {
      case None => sys.error(s"project[$projectId] was not found in the registry")
      case Some(application) => {
        val registryPorts = application.ports.headOption.getOrElse {
          sys.error(s"project[$projectId] does not have any ports in the registry")
        }
        val externalPort: Long = registryPorts.external

        createLoadBalancer(name, externalPort)
        configureHealthCheck(name, externalPort)

        name
      }
    }
  }

  def createLoadBalancer(name: String, externalPort: Long) {
    val elbListeners = Seq(
      new Listener()
        .withProtocol("HTTP")
        .withInstanceProtocol("HTTP")
        .withLoadBalancerPort(80)
        .withInstancePort(externalPort.toInt)
    )

    try {
      val result = client.createLoadBalancer(
        new CreateLoadBalancerRequest()
          .withLoadBalancerName(name)
          .withListeners(elbListeners.asJava)
          .withSubnets(settings.elbSubnets.asJava)
          .withSecurityGroups(settings.elbSecurityGroups.asJava)
      )
      println(s"Created Load Balancer: ${result.getDNSName}")
    } catch {
      case e: DuplicateLoadBalancerNameException => println(s"Launch Configuration '$name' already exists")
    }
  }

  def configureHealthCheck(name: String, externalPort: Long) {
    try {
      client.configureHealthCheck(
        new ConfigureHealthCheckRequest()
          .withLoadBalancerName(name)
          .withHealthCheck(
            new HealthCheck()
              .withHealthyThreshold(2)
              .withInterval(30)
              .withTarget(s"HTTP:$externalPort/_internal_/healthcheck")
              .withTimeout(5)
              .withUnhealthyThreshold(2)
          )
      )
    } catch {
      case e: LoadBalancerNotFoundException => sys.error("Cannot find load balancer $name: $e")
    }
  }

}
