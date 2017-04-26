package BlackBox

import java.util
import java.util.LinkedList

import Events._
import Modes._
import Models.In._
import Models.Out._
import com.phidgets.InterfaceKitPhidget
import scala.collection.JavaConverters._

import scala.collection.immutable.Set

/**
  * Created by bri_e on 20-04-17.
  * Managing the incoming flux of event.
  */
class BlackBox(interfaceKitPhidget: InterfaceKitPhidget){
  private val INDEX_LIGHT_SENSOR:Int        = 0
  private val INDEX_TEMPERATURE_SENSOR:Int  = 1
  private val INDEX_PRECISION_IR_SENSOR:Int = 3
  private val INDEX_VIBRATION_SENSOR:Int    = 4
  private val INDEX_MODE_POT:Int            = 5

  private val SEATS_NUMBER:Int              = 4

  private val INDEX_R:Int                   = 5
  private val INDEX_G:Int                   = 6
  private val INDEX_B:Int                   = 7

  private var eventList: util.LinkedList[Event] = new util.LinkedList[Event]()
  private var goalCase: GoalCase = _

  private val goal:Goal             = new Goal(  interfaceKitPhidget, INDEX_PRECISION_IR_SENSOR, INDEX_VIBRATION_SENSOR, this)
  private val weather:WeatherStation= new WeatherStation( interfaceKitPhidget, INDEX_TEMPERATURE_SENSOR, this)
  private val light:Light           = new Light( interfaceKitPhidget, INDEX_LIGHT_SENSOR, this )
  private val lighting:Lighting     = new Lighting(interfaceKitPhidget)
  private val stdSouth:Stand        = new Stand(interfaceKitPhidget, "South Stand", SEATS_NUMBER, this)
  private val stdNorth:Stand        = new Stand(interfaceKitPhidget, "North Stand", SEATS_NUMBER, this)
  private val lapCntr:LapCalculator = new LapCalculator(this)
  private val field:Field           = new Field(interfaceKitPhidget, INDEX_R, INDEX_B, INDEX_G)
  private val roof:Roof             = new Roof
  private val planning:MatchPlanning= new MatchPlanning
  private val mode:DemoModePotentiometer = new DemoModePotentiometer(interfaceKitPhidget, INDEX_MODE_POT, this)
  private var currentMode:Mode      = mode.getCurrentMode
  //val commu:CommunicationListener = new CommunicationListener

  def processEvent(event: Event): Unit = {
    var log:String = ""

    currentMode.isMatch = planning.areWeDuringAMatch

    if (!currentMode.isMatch)
      log += "Proceed Event : \n"
    else
      log += "Proceed Event in Match Mode : \n"

    eventList.add(event)

    event match {
      case BarEvent(_)           => log = processBarEvent(log)
      case HeatEvent(_)          => log = processHeatEvent(log)
      case LightEvent(_)         => log = processLightEvent(log)
      case PassageEvent(_)       => log = processGoalEvent(event,log)
      case StandEvent(_)         => log = processStandEvent(log)
      case TurnEvent(_)          => log = processTurnEvent(log)
      case VibrationEvent(_)     => log = processGoalEvent(event,log)
      case NewMatchPlanEvent(_)  =>
        log += "Received new match ! \n"
        //add the received match to communication listener
      case DemoPhaseEvent(_)     =>
        currentMode = mode.getCurrentMode
        log += "New Mode Set : " + currentMode.toString
      case _                  => log = "Error Event non recognized"
    }
    if (eventList.size()>50) eventList.removeLast()
    println(log)
  }

  @deprecated
  def noEvent(c:Class[_]):Boolean = {
    //Todo : refactor every use of this in order to use the scala option method
    getLast(c) match {
      case Some(_) => true
      case None    => false
    }
  }

  def getLast(c:Class[_]):Option[Event] = {
    val temporaryList:util.ArrayList[Event] = eventList.clone.asInstanceOf[util.ArrayList[Event]]
    val tempList:List[Event] = temporaryList.asScala.toList
    //tempList.find((p:Event) => p match {case c => true case _ => false}) todo: verify is its really equivalent
    tempList.find{case c => true case _ => false}
  }


  /*
  Todo : change the return value of every function used below to option and when none make the error log written
   */

  private def processBarEvent(log: String): _root_.scala.Predef.String = {
    "Error : Bar Event no longer taken in charge in this code"
  }
  private def processHeatEvent(log: String): _root_.scala.Predef.String = {
    val temp:Double = weather.getHeat
    val isDay:Boolean = weather.isDay
    val weatherCond:Weather = weather.getWeather
    var logChanges = ""

    weatherCond match {
      case Snow()  =>
        roof.closeRoof
        field.setHeating(temp <= 5)
        field.setWatering(!currentMode.isMatch && (temp>15 || !isDay))
      case Rain()  =>
        roof.closeRoof
        field.setHeating(temp <= 10)
        field.setWatering(!currentMode.isMatch && (temp>20 || !isDay))
      case Sun()   =>
        roof.openRoof
        field.setHeating(temp <= 5)
        field.setWatering(!currentMode.isMatch && (temp>15 || !isDay))
      case Cloud() =>
        if (roof.open) {
          field.setHeating(temp <= 15)
          field.setWatering(!currentMode.isMatch && (temp>10 || !isDay))
        } else {
          field.setHeating(temp <= 10)
          field.setWatering(!currentMode.isMatch && (temp>10 || !isDay))
        }
    }


    log + "change in temperature : " + temp + " \n" + "actual meteo : " + weatherCond.toString + "\n are we during day ? ==> " + isDay + "\n\n changes done : " + logChanges
  }

  private def processLightEvent(log: String): _root_.scala.Predef.String = {
    "change in brightness : " + light.retLightIntensity + "\n"
  }

  private def processGoalEvent(event:Event, log: String): _root_.scala.Predef.String = {
    if (currentMode.isMatch)
      event match {
        case VibrationEvent(_) =>
          if (goalCase.hasGoalHappened(event.asInstanceOf[VibrationEvent]))
            goal.incrementGoal(System.currentTimeMillis())
          log + "vibration of the goal Structure \n"
        case PassageEvent(_) =>
          goalCase = new GoalCase(event.asInstanceOf[PassageEvent], goal.getLastGoal)
          log + "passage through the goal poles \n"
      }
    else log + "no match ongoing, goal event ignored\n"
  }

  private def processStandEvent(log: String): _root_.scala.Predef.String = {
   // val northStandState = for (seat <- stdNorth.getSeats) yield {
     // if (seat) "Seat taken" else "Seat Free"
    //}

    val northStandState:Array[String] = stdNorth.getSeats.zipWithIndex.map{
      case(taken: Boolean, count: Int) => {
        val x = "Seat " + count
        val y = if (taken) " taken" else " free"
        x + y
      }
    }
    val southStandState:Array[String] = stdSouth.getSeats.zipWithIndex.map{
      case(taken: Boolean, count: Int) => {
        val x = "Seat " + count
        val y = if (taken) " taken" else " free"
        x + y
      }
    }
    log + "Change in Stands : Stand actual State : \n" + "North Stand : \n" + northStandState.mkString + "\n South Stand : \n" + southStandState.mkString + "\n"
  }

  private def processTurnEvent(log: String): _root_.scala.Predef.String = {
    //Todo : Check if new implementation suits the simple log
    log + "new turn or player \n"
  }
}
