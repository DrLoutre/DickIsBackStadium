package Requests

import java.util.Date

import Models.In.{Match, MatchPlanning}
import com.google.gson.Gson

import scalaj.http.{Http, HttpResponse}

object ToServer {

  private val BASE_URL = "http://192.168.137.18:8080/StadiumWebSite/api/v1/communication/"
  private val TIMEOUT = 10000
  private val CONT_TY = "content-type"
  private val APP_JS = "application/json"

  /**
    * Send a lap to the server
    * @param rfid rfid tag of the runner
    * @param nbrTours number of laps of the runner
    * @param tempsTour time of the last lap of the runner
    */
  def sendLap(rfid:String, nbrTours:Int, tempsTour:Long) : Unit = {
    val lap:Lap = new Lap(rfid, nbrTours, tempsTour.toString)
    val json:String = new Gson().toJson(lap)
    val code:Boolean = Http(BASE_URL.concat("laps")).timeout(TIMEOUT, TIMEOUT).postData(json).header(CONT_TY, APP_JS).asString.is2xx
    // println(code.toString + "\n" + lap + "\n" + json)
    if (code) println("Lap sent correctly") else println("Error while sending Lap")

  }

  /**
    * Send goal information to the server
    * @param isDroit boolean representing the side of the goal
    * @param nbrGoals give the number of goals of the corresponding goal
    */
  def sendGoal(isDroit:Boolean, nbrGoals:Int) : Unit = {
    val goal:Goal = new Goal(isDroit, nbrGoals)
    val json:String = new Gson().toJson(goal)
    val code:Boolean = Http(BASE_URL.concat("goals")).timeout(TIMEOUT, TIMEOUT).postData(json).header(CONT_TY, APP_JS).asString.is2xx
    if (code) println("Goal sent correctly") else println("Error while sending goal")
  }

  /**
    * Send stand information to the server
    * @param standID id of the stand
    * @param occupArray give the state of each seat of the stand
    */
  def sendStand(standID:Int, occupArray:Array[Boolean]): Unit ={
    val stand:Stand = new Stand(standID, occupArray)
    val json:String = new Gson().toJson(stand)
    val code:Boolean = Http(BASE_URL.concat("stand")).timeout(TIMEOUT, TIMEOUT).postData(json).header(CONT_TY, APP_JS).asString.is2xx
    if (code) println("Stand sent correctly") else println("Error while sending Stand")
  }

  def askForNewMatches(matchPlanning: MatchPlanning):Unit = {
    val code:HttpResponse[String] = Http(BASE_URL.concat("newMatch")).timeout(TIMEOUT,TIMEOUT).header(CONT_TY, APP_JS).asString
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    if (code.is2xx) {
      println("Match Request sent correctly")
      val result:List[Game] = new Gson().fromJson(code.body, classOf[List[Game]])
      println(result.toString)
      //filter all the matches that are finished
      result.filter{case Game(_,ended,_,_,_,_,_) => ended}
      // convert the gson match format to the local match format, filtered from useless info adding them directly to the match list
      result.foreach {
        case Game(date,_,_,_,_,_,_) => matchPlanning.addMatchToList(format.parse(date),new Date(format.parse(date).getTime+6000000))
      }
    } else {
      println("Error with Match Request")
    }
  }
}