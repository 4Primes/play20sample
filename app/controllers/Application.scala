package controllers

import play.api._
import play.api.db._
import play.api.mvc._
import play.api.libs.Codecs._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Play.current

import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

// Reactive Mongo imports
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers.DefaultBSONHandlers._

// Reactive Mongo plugin
import play.modules.reactivemongo._
import play.modules.reactivemongo.PlayBsonImplicits._

import models._

//
import play.api._
import play.api.db._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.Codecs._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Play.current

// Reactive Mongo imports
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers.DefaultBSONHandlers._

// Reactive Mongo plugin
import play.modules.reactivemongo._
import play.modules.reactivemongo.PlayBsonImplicits._

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import java.util.UUID



object Application extends Controller with MongoController {
  
  	val db = ReactiveMongoPlugin.db                             //database
  	val collection = db.collection("testcoll")					        //collection
   


  def index = Action {  
    Async{
      val qb = QueryBuilder().query( BSONDocument() )           //find all
      (collection.find[JsValue](qb).toList).map(users =>         
     	  Ok(views.html.index(users))                             //render index page
      )
    }
  }
  
  def user = Action {                                          //render new user creation page
    Ok(views.html.user(userForm))
  }

  def newUser = Action{ implicit request =>                    //create new user action   
    userForm.bindFromRequest.fold(
      formWithErrors => {                                      //if form has errors         
        Ok(views.html.user(formWithErrors))                    //render form with errors     
      },
      value => {                                               //if post value is valid       
        Async{
          val user = BSONDocument(                             //create new json for writing to database 
            "name" -> BSONString(value.name),
            "email" -> BSONString(value.email),
            "password" -> BSONString(sha1(value.password))
          )                         
          collection.insert[BSONDocument](user).map(_ =>       //write value to the database
            Redirect(routes.Application.index)                 //redirect to index page 
          )
        }
      }
    )
  }


  val userForm = Form(mapping (                                 //form validator  
    "name" -> nonEmptyText, 
    "email" -> email,
    "password" -> nonEmptyText
    )(User.apply)(User.unapply)
  )
  
}