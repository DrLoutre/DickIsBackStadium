package Requests

import scalaj.http.Http

object ToServer {

  //TODO : Change URL when deploy
  private val BASE_URL = "http://localhost:8080/StadiumWebSite/api/v1/communication/"

  def sendLap(rfid:String, nbrTours:Int, tempsTour:String, tempsMilli:Int) : Unit = {
    val lap:Lap = new Lap(rfid, nbrTours, tempsTour, tempsMilli)
    val json:String = new Gson().toJson(lap)
    val code:Boolean = Http(BASE_URL.concat("laps")).timeout(10000, 10000).postData(json).asString.is2XX
  }

  def sendGoal(isDroit:Boolean, nbrGoals:Int) : Unit = {
    val goal:Goal = new Goal(isDroit, nbrGoals)
    val json:String = new Gson().toJson(goal)
    val code:Boolean = Http(BASE_URL.concat("goals")).timeout(10000, 10000).postData(json).asString.is2xx
  }
}