package Requests

import com.google.gson.Gson

import scalaj.http.Http

object ToServer {

  //TODO : Change URL when deploy
  private val BASE_URL = "http://localhost:8080/StadiumWebSite/api/v1/communication/"

  /**
    * Send a lap to the server
    * @param rfid rfid tag of the runner
    * @param nbrTours number of laps of the runner
    * @param tempsTour time of the last lap of the runner
    * @param tempsMilli total time of the run
    */
  def sendLap(rfid:String, nbrTours:Int, tempsTour:Long, tempsMilli:Long) : Unit = {
    val lap:Lap = new Lap(rfid, nbrTours, tempsTour, tempsMilli)
    val json:String = new Gson().toJson(lap)
    val code:Boolean = Http(BASE_URL.concat("laps")).timeout(10000, 10000).postData(json).asString.is2xx
  }

  /**
    * Sent goal information to the server
    * @param isDroit boolean representing the side of the goal
    * @param nbrGoals give the number of goals of the corresponding goal
    */
  def sendGoal(isDroit:Boolean, nbrGoals:Int) : Unit = {
    val goal:Goal = new Goal(isDroit, nbrGoals)
    val json:String = new Gson().toJson(goal)
    val code:Boolean = Http(BASE_URL.concat("goals")).timeout(10000, 10000).postData(json).asString.is2xx
  }
}