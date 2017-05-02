package BlackBox

import java.util
import java.util.LinkedList

import Events._
import Modes._
import Models.In._
import Models.Out._
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event._

import scala.collection.JavaConverters._
import scala.collection.immutable.Set
import scala.collection.mutable

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

  private var eventList: mutable.Set[Event] = mutable.Set[Event]()
  private var goalCase: GoalCase = _

  private val goal:Goal             = new Goal(  interfaceKitPhidget, INDEX_PRECISION_IR_SENSOR, INDEX_VIBRATION_SENSOR, this)
  private val weather:WeatherStation= new WeatherStation( interfaceKitPhidget, INDEX_TEMPERATURE_SENSOR, this)
  private val light:Light           = new Light( interfaceKitPhidget, INDEX_LIGHT_SENSOR, this )
  private val lighting:Lighting     = new Lighting(interfaceKitPhidget)
  private val stdSouth:Stand        = new Stand(interfaceKitPhidget, "South Stand", SEATS_NUMBER, this)
  private val stdNorth:Stand        = new Stand(interfaceKitPhidget, "North Stand", SEATS_NUMBER, this)
  private val lapCntr:LapCalculator = new LapCalculator(this)
  private val field:Field           = new Field(interfaceKitPhidget, INDEX_R, INDEX_B, INDEX_G)
  private val roof:Roof             = new Roof(this)
  private val planning:MatchPlanning= new MatchPlanning
  private val mode:DemoModePotentiometer = new DemoModePotentiometer(interfaceKitPhidget, INDEX_MODE_POT, this)
  var currentMode:Mode      = mode.getCurrentMode
  //val commu:CommunicationListener = new CommunicationListener

  interfaceKitPhidget.addDetachListener((detachEvent: DetachEvent) => currentMode match {
      case DetachedMode(_, motors, rfid) =>
        currentMode = DetachedMode(true, motors, rfid)
      case _ =>
        currentMode = DetachedMode(true, false, false)
    }
  )

  interfaceKitPhidget.addAttachListener((attachEvent: AttachEvent) => currentMode match {
    case DetachedMode(_, motors, rfid) =>
      if (!motors && !rfid) NormalMode else DetachedMode(false, motors, rfid)
  })

  def processEvent(event: Event): Unit = {
    var log:String = ""

    currentMode.isMatch = planning.areWeDuringAMatch

    if (!currentMode.isMatch)
      log += "Proceed Event : \n"
    else
      log += "Proceed Event in Match Mode : \n"

    eventList += event


    currentMode match {
      case NormalMode() => {
        if (!currentMode.isMatch)
          log += "Proceed Event : \n"
        else
          log += "Proceed Event in Match Mode : \n"
        event match {
          case BarEvent(_) => log = processBarEvent(log)
          case HeatEvent(_) => log = {
            println("Processing heat event : ")
            processHeatEvent(log)}
          case LightEvent(_) => log = processLightEvent(log)
          case PassageEvent(_) => log = processGoalEvent(event, log)
          case StandEvent(_) => log = processStandEvent(log)
          case TurnEvent(_) => log = processTurnEvent(log)
          case VibrationEvent(_) => log = processGoalEvent(event, log)
          case NewMatchPlanEvent(_) =>
            log += "Received new match ! \n"
          //add the received match to communication listener
          case DemoPhaseEvent(_) =>
            currentMode = mode.getCurrentMode
            log += "New Mode Set : " + currentMode.toString
          case _ => log = "Error Event non recognized"
        }
      }
      case DetachedMode(kitDetached, roofDetached, rfidDetached) => {
        if (!currentMode.isMatch)
          log += "Proceed Event : \n"
        else
          log += "Proceed Event in Match Mode : \n"
        log += "/!/ Be careful, one or more phidget is detached ! \n"
        event match {
          case BarEvent(_) =>       if (!kitDetached)  log = processBarEvent(log)         else log += "--PhidgetKit Detached ! \n"
          case HeatEvent(_) =>      if (!kitDetached)  log = processHeatEvent(log)        else log += "--PhidgetKit Detached ! \n"
          case LightEvent(_) =>     if (!kitDetached)  log = processLightEvent(log)       else log += "--PhidgetKit Detached ! \n"
          case PassageEvent(_) =>   if (!kitDetached)  log = processGoalEvent(event, log) else log += "--PhidgetKit Detached ! \n"
          case StandEvent(_) =>     if (!kitDetached)  log = processStandEvent(log)       else log += "--PhidgetKit Detached ! \n"
          case TurnEvent(_) =>      if (!rfidDetached) log = processTurnEvent(log)        else log += "--RFID Reader Detached ! \n"
          case VibrationEvent(_) => if (!kitDetached)  log = processGoalEvent(event, log) else log += "--PhidgetKit Detached ! \n"
          case NewMatchPlanEvent(_) =>
            log += "Received new match ! \n"
          //add the received match to communication listener
          case DemoPhaseEvent(_) =>
            if (!kitDetached) {
              currentMode = mode.getCurrentMode
              log += "New Mode Set : " + currentMode.toString
            } else {
              log += "--PhidgetKit Detached ! \n"
            }
          case _ => log = "Error Event non recognized"
        }
      }
    }


    //if (eventList.size()>50) eventList.removeLast()
    filterEvntList
    println(log)
  }

  def getLast(newEvent:Event):Option[Event] = {

    var actualClass:Class[_] = classOf[Event]

    newEvent match {
      case LightEvent(_) => actualClass = classOf[LightEvent]
      case BarEvent(_) => actualClass = classOf[BarEvent]
      case DemoPhaseEvent(_) => actualClass = classOf[DemoPhaseEvent]
      case HeatEvent(_) => actualClass = classOf[HeatEvent]
      case NewMatchPlanEvent(_) => actualClass = classOf[NewMatchPlanEvent]
      case PassageEvent(_) => actualClass = classOf[PassageEvent]
      case StandEvent(_) => actualClass = classOf[StandEvent]
      case TurnEvent(_) => actualClass = classOf[TurnEvent]
      case VibrationEvent(_) => actualClass = classOf[VibrationEvent]
      case _ => actualClass = classOf[Event]
    }



    try {
      val tempList:List[Event] = eventList.toList
      //println("Conversion done, checking with  = " + actualClass.toString + "\n for list : " + tempList)
      tempList.find { case actualClass => true case _ => false}
    } catch {
      case e:Exception => println("Exception : " + e)
        None
    }
  }

  def filterEvntList:Unit = {
    //println("Before filter : " + eventList.toString)
    val typeList:List[Class[_]] = List(classOf[BarEvent], classOf[DemoPhaseEvent], classOf[HeatEvent], classOf[LightEvent], classOf[NewMatchPlanEvent], classOf[PassageEvent], classOf[PassageEvent], classOf[StandEvent], classOf[TurnEvent], classOf[VibrationEvent])
    for (y <- typeList) {
      val cnt:Int = eventList.count((e: Event) => !(e.getClass == y))
      if (cnt > 1) retains(y)
    }
    def retains(ty:Class[_]): Unit = {
      eventList.retain {
        x: Event => {
          (x.getClass == ty) &&
            (eventList.find((y: Event) =>
              (ty == y.getClass) && (y.eventTime < x.eventTime)
            ) match {
              case Some(_) =>
                true
              case None =>
                false
            })
        }
      }
    }
    //println("After Filter : " + eventList.toString())
  }


  /*
  Todo : change the return value of every function used below to option and when none make the error log written
   */

  private def processBarEvent(log: String): _root_.scala.Predef.String = {
    "Error : Bar Event no longer taken in charge in this code"
  }

  private def processHeatEvent(log: String): _root_.scala.Predef.String = {
    println("New heat event !")
    val temp:Double = weather.getHeat
    val isDay:Boolean = weather.isDay
    println("is not wrong for Day")
    val weatherCond:Weather = weather.getWeather
    println("is not wrong for Weather")
    var logChanges = ""
    println("Matching weather condition")
    weatherCond match {
      case Snow()  =>
        println("neige")
        roof.closeRoof
        field.setHeating(temp <= 5)
        field.setWatering(!currentMode.isMatch && (temp>15 || !isDay))
      case Rain()  =>
        println("pluie")
        roof.closeRoof
        field.setHeating(temp <= 10)
        field.setWatering(!currentMode.isMatch && (temp>20 || !isDay))
      case Sun()   =>
        println("soleil")
        roof.openRoof
        field.setHeating(temp <= 5)
        field.setWatering(!currentMode.isMatch && (temp>15 || !isDay))
      case Cloud() =>
        println("nuages")
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
    val ret:Int = light.retLightIntensity
    lighting.updatePower(ret)
    "change in brightness : " + ret + "\n"
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
