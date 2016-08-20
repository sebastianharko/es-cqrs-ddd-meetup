package test

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.persistence.{PersistentActor, PersistentView}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, jackson}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


object Main extends App with Json4sSupport {
  
  // actor system
  implicit val system = ActorSystem("system")
  implicit val materializer = ActorMaterializer()
  
  // start two actors
  val clientWriteSide = system.actorOf(Props(new Client()))
  val clientQuerySide = system.actorOf(Props(new ClientCurrentProducts()))
  
  // HTTP stuff here
  implicit val formats = DefaultFormats
  implicit val serialization = jackson.Serialization
  
  implicit val timeout = Timeout(250 milliseconds)
  
  val routes = path("products") {
    post {
      entity(as[Product]) {
        product => {
          // send message to actor
          clientWriteSide ! AddProduct(product)
          complete(StatusCodes.Accepted)
        }
      }
    } ~ get {
      complete {
        // ask the query side for the list of current products
        (clientQuerySide ? 'GetProducts).mapTo[List[Product]]
      }
    }
  }
  
  Http().bindAndHandle(routes, "0.0.0.0", 8080)
  
}

case class AddProduct(product: Product)

case class Product(name: String, price: Double, description: String)

case class ProductAdded(product: Product)

class ClientCurrentProducts extends PersistentView with ActorLogging {
  
  override def persistenceId = "client"
  
  override def viewId = "client-view"
  
  def products(existing: List[Product]): Receive = {
    
    case ProductAdded(product) =>
      log.info("added a product")
      context.become(products( product :: existing ))
    
    case 'GetProducts =>
      log.info("somebody wants the list of all products")
      sender() ! existing
  }
  
  def receive = products(List())
  
}

class Client extends PersistentActor with ActorLogging {
  
  override def persistenceId = "client"
  
  override def receiveCommand = {
    
    case AddProduct(product) =>
      log.info("received an add product command")
      persist(ProductAdded(product)) {
        event =>
        // do nothing here since there's no state
      }
  }
  
  override def receiveRecover = {
    case _ => {
      // nothing here right now since there's no state
    }
  }
  
}


