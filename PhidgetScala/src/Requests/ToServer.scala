package Requests

import scalaj.http.Http

object ToServer {

  private val BASE_URL = ""

  def sendLap(rfid:String, nbrTours:Int, tempsTour:String) : Unit = {
    val json:String = new Gson().toJson()
    val code:Boolean = Http(BASE_URL.concat("laps")).timeout(10000, 10000).postData(json).asString.is2XX
  }

  def sendGoal(isDroit:Boolean, nbrGoals:Int) : Unit = {
    val goal:Goal = new Goal(isDroit, nbrGoals)
    val json:String = new Gson().toJson(goal)
    Http(BASE_URL.concat("goals")).timeout(10000, 10000).postData().asString.is2xx
  }
}