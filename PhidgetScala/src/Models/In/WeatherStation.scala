package Models.In

import java.io.{BufferedReader, IOException, InputStreamReader}
import java.net.{HttpURLConnection, URL}
import java.util.Date

import BlackBox.BlackBox
import Events.HeatEvent
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}
import org.json.JSONObject
import org.json.JSONObject._

import scalaj.http.Http

/**
  * Created by bri_e on 20-04-17.
  * The weather station gives the heat, the sun phases, the weather.
  */
class WeatherStation (interfaceKitPhidget: InterfaceKitPhidget, sensorIndex: Int, blackBox: BlackBox ) {

  //Key in order to access the API
  val API_KEY       = "eff123853edf134ccd86bf1455b57487"

  /**
    * @return the converted value of the temperature
    */
  def getHeat: Double = {
    val sensorvalue = interfaceKitPhidget.getSensorValue(sensorIndex)
    val roomtemp = (sensorvalue * 0.22222) - 61.11
    roomtemp
  }

  /**
    * @return the date of today's sunrise
    */
  def getSunRise():Date = {
    println("Able to gather info? ")
    var sun = getWeatherInformation
    println("YES !")
    if (sun != null) {
      sun = sun.getJSONObject("sys")
      //3600 : UTC to UTC+1
      new Date(sun.getInt("sunrise") * 1000L + 3600)
    } else {
      println("Error while reading weather API")
      new Date
    }
  }

  /**
    * @return the date of today's sunset
    */
  def getSunSet():Date = {
    var sun = getWeatherInformation
    if (sun != null) {
      sun = sun.getJSONObject("sys")
      //3600 : UTC to UTC+1
      new Date(sun.getInt("sunset") * 1000L + 3600)
    } else {
      println("Error while reading weather API")
      new Date
    }
  }

  /**
    * @return actual weather
    */
  def getWeather: Weather = {
    val weatherInfos: JSONObject = getWeatherInformation
    val weather: String = weatherInfos.getJSONArray("weather").getJSONObject(0).getString("main")
    weather.toLowerCase match {
      case "rain"   =>  Rain()
      case "clouds" =>  Cloud()
      case "clear"  =>  Sun()
      case "snow"   =>  Snow()
      case _        =>  Sun()
    }
  }

  /**
    * @return if it is day or not
    */
  def isDay:Boolean = {
    val now     = new Date()
    println("Fletched date ok")
    val sunRise = getSunRise()
    println("Fleched Sunrise")
    val sunSet  = getSunSet()
    println("Fleched SunSet")
    now.after(sunRise)&&now.before(sunSet)
  }


  /**
    * @return the result of a request to the API
    */
  def getWeatherInformation:JSONObject = {
    val body = Http("http://api.openweathermap.org/data/2.5/weather?id=2790472&APPID=".concat(API_KEY)).timeout(10000, 10000).asString.body
    val rootObject = new JSONObject(body)
    rootObject
  }

}
