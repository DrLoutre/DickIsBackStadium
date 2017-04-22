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
  val INDEX_LIGHT_SENSOR:Int        = 0
  val INDEX_TEMPERATURE_SENSOR:Int  = 1
  val INDEX_PRECISION_IR_SENSOR:Int = 3
  val INDEX_VIBRATION_SENSOR:Int    = 4
  val INDEX_MODE_POT:Int            = 5

  val SEATS_NUMBER:Int              = 4

  val INDEX_R:Int                   = 5
  val INDEX_G:Int                   = 6
  val INDEX_B:Int                   = 7

  var eventList: util.LinkedList[Event] = new util.LinkedList[Event]()
  var goalCase: GoalCase = _

  val goal:Goal             = new Goal(  interfaceKitPhidget, INDEX_PRECISION_IR_SENSOR, INDEX_VIBRATION_SENSOR, this)
  val weather:WeatherStation= new WeatherStation( interfaceKitPhidget, INDEX_TEMPERATURE_SENSOR, this)
  val light:Light           = new Light( interfaceKitPhidget, INDEX_LIGHT_SENSOR, this )
  val lighting:Lighting     = new Lighting(interfaceKitPhidget)
  val stdSouth:Stand        = new Stand(interfaceKitPhidget, "South Stand", SEATS_NUMBER, this)
  val stdNorth:Stand        = new Stand(interfaceKitPhidget, "North Stand", SEATS_NUMBER, this)
  val lapCntr:LapCalculator = new LapCalculator(this)
  val field:Field           = new Field(interfaceKitPhidget, INDEX_R, INDEX_B, INDEX_G)
  val roof:Roof             = new Roof
  val planning:MatchPlanning= new MatchPlanning
  val mode:DemoModePotentiometer = new DemoModePotentiometer(interfaceKitPhidget, INDEX_MODE_POT, this)
  var currentMode:Mode      = mode.getCurrentMode
  //val commu:CommunicationListener = new CommunicationListener

  def processBarEvent(log: String): _root_.scala.Predef.String = ???
  def processHeatEvent(log: String): _root_.scala.Predef.String = ???
  def processLightEvent(log: String): _root_.scala.Predef.String = ???
  def processGoalEvent(log: String): _root_.scala.Predef.String = ???
  def processStandEvent(log: String): _root_.scala.Predef.String = ???
  def processTurnEvent(log: String): _root_.scala.Predef.String = ???

  def processEvent(event: Event): Unit = {
    var log:String = _

    if (!currentMode.isMatch)
      log += "Proceed Event : \n"
    else
      log += "Proceed Event in Match Mode : \n"

    eventList.add(event)

    event match {
      case BarEvent(_)           => log = processBarEvent(log)
      case HeatEvent(_)          => log = processHeatEvent(log)
      case LightEvent(_)         => log = processLightEvent(log)
      case PassageEvent(_)       => log = processGoalEvent(log)
      case StandEvent(_)         => log = processStandEvent(log)
      case TurnEvent(_)          => log = processTurnEvent(log)
      case VibrationEvent(_)     => log = processGoalEvent(log)
      case NewMatchPlanEvent(_)  => {
        log += "Received new match ! \n"
        //add the received match to communication listener
      }
      case DemoPhaseEvent(_)     => {
        currentMode = mode.getCurrentMode
        log += "New Mode Set : " + currentMode.toString
      }
      case _                  => log = "Error Event non recognized"
    }
    if (eventList.size()>50) eventList.removeLast();
    println(log)
  }

  def noEvent(c:Class[_]):Boolean = {
    getLast(c) match {
      case Some(_) => true
      case None    => false
    }
  }

  def getLast(c:Class[_]):Option[Event] = {
    val temporaryList:util.ArrayList[Event] = eventList.clone.asInstanceOf[util.ArrayList[Event]]
    val tempList:List[Event] = temporaryList.asScala.toList
    tempList.find((p:Event) => p match {case c => true case _ => false})
    
  }
}
