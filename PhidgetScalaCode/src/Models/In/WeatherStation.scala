package Models.In

import java.io.{BufferedReader, IOException, InputStreamReader}
import java.net.{HttpURLConnection, URL}
import java.util.Date

import BlackBox.BlackBox
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}
import org.json.JSONObject
import org.json.JSONObject._

/**
  * Created by bri_e on 20-04-17.
  * The weather station gives the heat, the sun phases, the weather.
  */
class WeatherStation (interfaceKitPhidget: InterfaceKitPhidget, sensorIndex: Int, blackBox: BlackBox ) {
  val MILI_INTERVAL = 1000
  val API_KEY       = "eff123853edf134ccd86bf1455b57487"

  interfaceKitPhidget.addSensorChangeListener((sensorChangeEvent: SensorChangeEvent) => {
    if (sensorChangeEvent.getIndex == sensorIndex) {
      //Todo : check if blackBox.noEvent + time difference + Create and process event
    }
  })

  def getHeat: Double = {
    val sensorvalue = interfaceKitPhidget.getSensorValue(sensorIndex)
    val roomtemp = (sensorvalue * 0.22222) - 61.11
    roomtemp
  }

  def getSunRise():Date = {
    var sun = getWeatherInformation
    if (sun != null) {
      sun = sun.getJSONObject("sys")
      //3600 : UTC to UTC+1
      new Date(sun.getInt("sunrise") * 1000L + 3600)
    } else {
      println("Error while reading weather API")
      Date
    }
  }

  def getSunSet():Date = {
    var sun = getWeatherInformation
    if (sun != null) {
      sun = sun.getJSONObject("sys")
      //3600 : UTC to UTC+1
      new Date(sun.getInt("sunset") * 1000L + 3600)
    } else {
      println("Error while reading weather API")
      Date
    }
  }

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

  def isDay:Boolean = {
    val now     = new Date()
    val sunRise = getSunRise()
    val sunSet  = getSunSet()
    now.after(sunRise)&&now.before(sunSet)
  }



  def getWeatherInformation:JSONObject = {
    val url = new URL("http://api.openweathermap.org/data/2.5/weather?id=2790472&APPID=" + API_KEY)
    val httpURLConnection = url.openConnection.asInstanceOf[HttpURLConnection]
    httpURLConnection.setRequestMethod("GET")
    httpURLConnection.setRequestProperty("accept", "application/json")
    if (httpURLConnection.getResponseCode != 200) throw new RuntimeException("Http Get method failed " + httpURLConnection.getResponseCode)
    val br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream))
    val buffer:StringBuilder = new StringBuilder
    var temp:String = null
    while ( {
      (temp = br.readLine) != null
    }) buffer.append(temp)
    httpURLConnection.disconnect()
    val rootObject = new JSONObject(buffer.toString)
    rootObject
  }

}
