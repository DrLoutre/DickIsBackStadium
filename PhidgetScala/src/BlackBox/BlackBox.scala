package BlackBox

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

import Events._
import Models.In._
import Models.Out._
import Modes._
import Requests.ToServer
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event._

import scala.collection.mutable

/**
  * Created by bri_e on 20-04-17.
  * BlackBox managing the incoming flux of event.
  * The blackBox two main attributes are :
  * The eventList which is the set containing all the events that take place. This list should always max have only one event of each type.
  * The goalCase which is composed of two slots that are filled when one of the two events in the process of a goal take place. If those events take place in a
  * short enough lap of time, the goal is validated.
  * Other attributes of the blackBox are the phidget kit object and the models that represent real life sensors.
  */
class BlackBox(interfaceKitPhidget: InterfaceKitPhidget){

  // Constants Threshold
  private val PRECISION_IR_SENSOR_THERSHOLD: Int = 600

  // Constants about time
  private val LIGHT_TIMEOUT:Long     = 20
  private val TEMP_TIMEOUT:Long      = 3000
  private val PASSAGE_TIMEOUT:Long   = 100
  private val VIBRATION_TIMEOUT:Long = 100
  private val POT_TIMEOUT:Long       = 3000

  // Constants index of sensors and lights
  private val INDEX_LIGHT_SENSOR:Int        = 0
  private val INDEX_TEMPERATURE_SENSOR:Int  = 1
  private val INDEX_PRECISION_IR_SENSOR:Int = 3
  private val INDEX_VIBRATION_SENSOR:Int    = 4
  private val INDEX_MODE_POT:Int            = 5

  private val INDEX_G:Int                   = 5
  private val INDEX_R:Int                   = 6
  private val INDEX_B:Int                   = 7

  // Constants number of Seats
  private val SEATS_NUMBER:Int              = 4


  // List of event of the blackbox.
  private var eventList: mutable.Set[Event] = mutable.Set[Event]()
  // Case of goal composed of two different events.
  private var goalCase: GoalCase = _

  // Objects that represent real-life sensors.
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


  ToServer.askForNewMatches(planning)

  setListener

  /**
    * General event processing function.
    * @param event event to process.
    */
  def processEvent(event: Event): Unit = {
    // Log variable.
    var log:String = "\n\n\nEvent : \n "

    //Adding event to the list.
    eventList.add(event)


    log += "Light : " + light.retLightIntensity + " and bright : " + lighting.power + "\n"
    log += "Match?: " + currentMode.isMatch + "\n"
    log += "Heat  : " + weather.getHeat + "\n"
    log += "Mode  : " + currentMode.toString + "\n"
    log += "StandN: " + stdNorth.getStandPercentage + "\n"
    log += "StandS: " + stdSouth.getStandPercentage + "\n"
    log += "Goals : " + goal.goal + "\n"
    log += "Field : " + (if(field.is_Heating) "heating " else " not heating" ) + " and "  + (if (field.is_Watering) "watering" else "not watering") + "\n"
    log += "Roof  : " + (if(roof.open) "openned" else "closed") + "\n"
    currentMode match {
      case DetachedMode(kit, rf, rfid) =>
        log += "Kit is detachd? " + kit + "\nRoof is detached? " + rf + "\nRFID is detached? " + rfid + "\n"
        log += "Is Kit REALLY DETACHED =>   Attached? "+ interfaceKitPhidget.isAttached
        if (interfaceKitPhidget.isAttached && rf && rfid)
          currentMode = mode.getCurrentMode
      case _ => log + currentMode.toString + "\n"
    }

    if (currentMode.inGame) {
      field.setWatering(false)
      field.setHeating(false)
    }

    //Filter the list before processing.
    filterEvntList

    //First of all the mode must be matched bewteen all the availables modes.
    currentMode match {
      case _ => { // It's _ because it take in charge normal mode and both demo mode 2 and 3
        // In normal mode we check in the match planning in order to update the mode attribute.
        currentMode.isMatch = planning.areWeDuringAMatch

         if (!currentMode.inGame)
          log += "Proceed Event (mode : " + currentMode.getClass + "): \n"
        else {
          log += "Proceed Event in Match Mode (mode : + " + currentMode.getClass + " : \n"
          field.setWatering(false)
          field.setHeating(false)
        }

        //Then, we match the event with its type and launch the process following its type.
        event match {
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
            if (!currentMode.isInstanceOf[DetachedMode]) {
              currentMode = mode.getCurrentMode
              Thread.sleep(500)
              processHeatEvent(log)
              log += "New Mode Set : " + currentMode.toString
            } else {
              log += "Detached mode, mode not modifed !!"
            }
          case _ => log = "Error Event non recognized" + event.toString
        }
      }
      case DetachedMode(kitDetached, roofDetached, rfidDetached) => {
        //In detached mode, the system works in altered mode without what is detached.
        if (!currentMode.inGame)
          log += "Proceed Event : \n"
        else
          log += "Proceed Event in Match Mode : \n"
        log += "/!/ Be careful, one or more phidget is detached ! \n"

        //Matching its type following its type.
        event match {
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
      case Demo_1_Mode() => {
        // Demo Mode n°1 : the match mode is forced.
        log = log + "Proceed Event in forced Match Mode : \n"
        currentMode.isMatch = true
        field.setWatering(false)
        field.setHeating(false)
        event match {
          case HeatEvent(_) => log = {
            println("Processing heat event : ")
            processHeatEvent(log)
          }
          case LightEvent(_) => log = processLightEvent(log)
          case PassageEvent(_) => log = processGoalEvent(event, log)
          case StandEvent(_) => log = processStandEvent(log)
          case TurnEvent(_) => log = processTurnEvent(log)
          case VibrationEvent(_) => log = processGoalEvent(event, log)
          case NewMatchPlanEvent(_) =>
            log += "Received new match ! \n"
          // Changed the match receiving to cron
          case DemoPhaseEvent(_) =>
            if (!currentMode.isInstanceOf[DetachedMode]) {
              currentMode = mode.getCurrentMode
              Thread.sleep(500)
              processHeatEvent(log)
              log += "New Mode Set : " + currentMode.toString
            } else {
              log += "Detached mode, mode not modifed !!"
            }
          case _ => log = "Error Event non recognized"
        }
      }
    }
    // Printing the final log.
    println(log)
  }

  /**
    * Get the last event event of the type of the event given in parameters
    * @param newEvent new event of which type the returned one must be
    * @return return the event in the eventList that have the same type as the one given in parameters
    */
  def getLast(newEvent:Event):Option[Event] = {

    var actualClass:Class[_] = classOf[Event]
    //Listing all the different type of event in order to
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
    //println("Getting last event of : " + actualClass)
    print("")
    try {
      val tempList:List[Event] = eventList.toList
      // Create a list in order to optimize the following find
      print("")
      //println("Checking with  = " + actualClass.toString + "\n for list : " + tempList)
      tempList.find {(x:Event) => x.getClass == actualClass}
    } catch {
      case e:Exception =>
        println("Exception : " + e)
        None
    }
  }

  /**
    * Function that filter the eventList so that there is only one type of each event max
    * in it. It keeps the newer event in the list.
    */
  def filterEvntList:Unit = {
    //println("Before filter : " + eventList.toString)
    eventList.retain{x: Event => !hasOtherOfTypeAndOlder(x)}
    def hasOtherOfTypeAndOlder(elem:Event): Boolean = {
      eventList.count((e:Event) => e.getClass == elem.getClass && e != elem && e.eventTime > elem.eventTime) > 0
    }
    //println("After Filter : " + eventList.toString())
  }


  /*
  Todo : change the return value of every function used below to option and when none make the error log written
   */

  /**
    * Processing of the Heat event.
    * This event happens when there is a change in temperature.
    * The changes of temperature triggers the whole weather station information to refresh.
    * We therefore actualise the roof and field states.
    * @param log string to append to the log of the event.
    * @return full log of the event.
    */
  private def processHeatEvent(log: String): _root_.scala.Predef.String = {
    //Getting information from both the temperature sensor and the weather station
    println("New heat event !")

    var temp: Double = weather.getHeat
    var isDay: Boolean = weather.isDay
    println("is not wrong for Day")
    var weatherCond: Weather = weather.getWeather
    println("is not wrong for Weather")

    currentMode match {
      case Demo_2_Mode() =>
        temp = -5.0
        println("Forcing negative temp : -5°C")
        weatherCond = Snow()
        println("Forcing snow")
      case Demo_3_Mode() =>
        isDay  = false
        println("Forcing Night")
      case Demo_4_Mode() =>
        temp = 30.0
        println("Forcing temperature to 30°C")
        weatherCond = Sun()
        println("Forcing Sun (if only...)")
    }
    // Initialisation of the log
    var logChanges = ""
    println("Matching weather condition")
    weatherCond match {
      // In case of snow, we close the roof and, depending on the temperature and the current mode, the field may be watered and/or heated.
      case Snow() =>
        roof.closeRoof
        logChanges += "    - Roof closed"
        if (!currentMode.inGame) {
          field.setHeating(temp <= 5)
          if (temp <= 5) logChanges += "    - set Heating."
          field.setWatering(!currentMode.inGame && (temp > 15 || !isDay))
          if (!currentMode.inGame && (temp > 15 || !isDay)) logChanges += "    - set Watering."
          // In case of rain, we close the roof and, depending on the temperature and the current mode, the field may be watered and/or heated.
        }
      case Rain() =>
        roof.closeRoof
        logChanges += "    Roof closed"
        if (!currentMode.inGame) {
          field.setHeating(temp <= 10)
          if (temp <= 10) logChanges += "    - set Heating."
          field.setWatering(!currentMode.inGame && (temp > 20 || !isDay))
          if (!currentMode.inGame && (temp > 20 || !isDay)) logChanges += "    - set Watering."
          // In case of sun, we open the roof and, depending on the temperature and the current mode, the field may be watered and/or heated.
        }
      case Sun()   =>
        roof.openRoof
        logChanges += "    Roof opened"
        if (!currentMode.inGame) {
          field.setHeating(temp <= 5)
          if (temp <= 5) logChanges += "    - set Heating."
          field.setWatering(!currentMode.inGame && (temp > 15 || !isDay))
          if (!currentMode.inGame && (temp > 15 || !isDay)) logChanges += "    - set Watering."
          // In case of cloudy weather, we let the roof as it is and, depending on the temperature and the current mode, the field may be watered and/or heated.
        }
      case Cloud() =>
        if (isDay) {
          roof.openRoof
          logChanges += "    - Roof opened"
        } else {
          roof.closeRoof
          logChanges += "    - Roof closed"
        }
        if (!currentMode.inGame) {
          field.setHeating(temp <= 10)
          if (temp <= 10) logChanges += "    - set Heating."
          println("Condition : " + (temp>10) )
          println("Condition : " + !isDay)
          field.setWatering(temp > 10 || !isDay)
          if (temp > 10 || !isDay) logChanges += "    - set Watering."
        }
    }


    log + "change in temperature : " + temp + " \n" + "actual meteo : " + weatherCond.toString + "\n are we during day ? ==> " + isDay + "\n\n changes done : " + logChanges + "\n"
  }

  /**
    * Processing of the Light event.
    * This event happens the light intensity has changed. We therefore change the lighting power.
    * @param log string to append to the log of the event.
    * @return full log of the event.
    */
  private def processLightEvent(log: String): _root_.scala.Predef.String = {
    // Getting the new light intensity
    val ret:Int = light.retLightIntensity
    // Updating the lighting power
    lighting.updatePower(ret)
    log + "change in brightness : " + ret + "\n"
  }

  /**
    * Processing of the Goal event.
    * This event happens either when the ball makes the poles passage sensor or the net vibration sensor trigger.
    * It decides if a goal has happened or not.
    * @param event event in order to differentiate the vibration event from the passage event
    * @param log string to append to the log of the event.
    * @return full log of the event.
    */
  private def processGoalEvent(event:Event, log: String): _root_.scala.Predef.String = {
    // a goal is only registered if we are during a match
    if (currentMode.inGame)
      event match {
          // In case of a vibration, it checks the goalCase, if the goalCase decide it is a goal, we increment the goal.
        case VibrationEvent(_) =>
          if (goalCase.hasGoalHappened(event.asInstanceOf[VibrationEvent])) {
            if (goal.incrementGoal(System.currentTimeMillis())) {
              goalCase.updateLastGoal(System.currentTimeMillis()) //Double verification of the timing of goals.
              ToServer.sendGoal(true,goal.goal)
              log + "goal registered !! Number of goals : " + goal.goal
            } else {
              log + "vibration of the goal Structure \n"
            }
          } else {
            log + "vibration of the goal Structure \n"
          }
          // In case of a passage, it instantiate the goalCase
        case PassageEvent(_) =>
          goalCase = new GoalCase(event.asInstanceOf[PassageEvent], goal.getLastGoal)
          log + "passage through the goal poles \n"
      }
    else log + "no match ongoing, goal event ignored\n"
  }

  /**
    * Processing of the Stand event.
    * This event happens when a new seat is freed or taken.
    * @param log string to append to the log of the event.
    * @return full log of the event.
    */
  private def processStandEvent(log: String): _root_.scala.Predef.String = {

    // displaying the states of the north stand seats
    val northStandState:Array[String] = stdNorth.getSeats.zipWithIndex.map{
      case(taken: Boolean, count: Int) => {
        val x = "Seat " + count
        val y = if (taken) " taken " else " free "
        x + y
      }
    }
    //displaying the states of the south stand seats
    val southStandState:Array[String] = stdSouth.getSeats.zipWithIndex.map{
      case(taken: Boolean, count: Int) => {
        val x = "Seat " + count
        val y = if (taken) " taken " else " free "
        x + y
      }
    }

    ToServer.sendStand(0, stdSouth.getSeats)
    ToServer.sendStand(1, stdNorth.getSeats)
    log + "Change in Stands : Stand actual State : \n" + "North Stand : \n" + northStandState.mkString + "\n South Stand : \n" + southStandState.mkString + "\n"
  }

  /**
    * Processing of the Turn event.
    * This event happens when a new runner enters on the court or pass the finish line.
    * @param log string to append to the log of the event.
    * @return full log of the event.
    */
  private def processTurnEvent(log: String): _root_.scala.Predef.String = {
    val id:String = lapCntr.lastScanned
    val idnum = lapCntr.runners.idNumber.get(lapCntr.runners.idList.indexOf(id))
    val time = lapCntr.runners.time.get(lapCntr.runners.idList.indexOf(id)).getLast
    val total = lapCntr.runners.getTotal(id)


    ToServer.sendLap(
      id,
      idnum,
      time)
    log + "new turn or player \nid => " + id + "\nnumber => " + idnum + "\ntime => " + time
  }


  /**
    * Method that set all the needed listeners for the Interface Kit Phidget.
    * It returns nothing.
    */
  private def setListener:Unit = {

    /**
      * Set a listener that triggers the Detached Mode when the Interface Kit is detached.
      */
    interfaceKitPhidget.addDetachListener((detachEvent: DetachEvent) => {
      println("===========================================================>>>>>> Detached IFK")
      currentMode match {
        case DetachedMode(_, motors, rfid) =>
          currentMode = DetachedMode(true, motors, rfid)
        case _ =>
          currentMode = DetachedMode(true, false, false)
      }
    }
    )

    /**
      * Set a listener that update the mode status and if everything is plugged in, set the normal mode when the IPK is attached.
      */
    interfaceKitPhidget.addAttachListener((attachEvent: AttachEvent) => {
      println("=====================================================================> Connected ! => ")
      currentMode match {
        case DetachedMode(_, motors, rfid) =>
          println("Motors : " + motors + "         RFID = " + rfid)
          if (!motors && !rfid)
            currentMode = mode.getCurrentMode
          else
            currentMode = DetachedMode(false, motors, rfid)
      }
    })

    /**
      * Set a listener that will lead to processing of event accordingly to the current mode
      */
    interfaceKitPhidget.addSensorChangeListener((sensorChangeEvent: SensorChangeEvent) => {
      // Matching the index of the sensor
      sensorChangeEvent.getIndex match {
          //Case of a change event on the light sensor
        case INDEX_LIGHT_SENSOR =>
          //First of all, filtering of the list.
          filterEvntList
          val time = System.currentTimeMillis
          // Getting if existing the last event of that type
          getLast(LightEvent(time)) match {
              // if it exists, it check that the last light event is not too recent
            case Some(LightEvent(eventTime)) =>
              if ((time - eventTime) > LIGHT_TIMEOUT) {
                println("New Light Event")
                processEvent(LightEvent(time))
              }
              // if not create a new one.
            case None =>
              println("First Light Event")
              processEvent(LightEvent(time))
          }
          //Case of a change event on the temperature sensor
        case INDEX_TEMPERATURE_SENSOR =>
          //First of all, filtering of the list.
          filterEvntList
          val time = System.currentTimeMillis
          // Getting if existing the last event of that type
          getLast(HeatEvent(time)) match {
              //if it exists, it check that the last light event is not too recent
            case Some(HeatEvent(eventTime)) =>
              if (time - eventTime > TEMP_TIMEOUT)
                println("New heat event")
              processEvent(HeatEvent(time))
              //if not create a new one.
            case None =>
              println("First heat event")
              processEvent(HeatEvent(time))
          }
          //Case of a change event on the passage sensor
        case INDEX_PRECISION_IR_SENSOR =>
          //First of all, filtering of the list.
          filterEvntList
          //Checking if the variation is important enough in order to trigger our event processing
          if (interfaceKitPhidget.getSensorValue(INDEX_PRECISION_IR_SENSOR) < PRECISION_IR_SENSOR_THERSHOLD) {
            val time = System.currentTimeMillis
            // Getting if existing the last event of that type
            getLast(PassageEvent(time)) match {
                //if it exists, it check that the last light event is not too recent
              case Some(PassageEvent(eventTime)) =>
                if (time - eventTime > PASSAGE_TIMEOUT)
                  println("IR detected")
                  processEvent(PassageEvent(time))
                //if not create a new one.
              case None =>
                println("First IR event")
                processEvent(PassageEvent(time))
            }
          }
          //Case of a change event on the vibration sensor
        case INDEX_VIBRATION_SENSOR =>
          //First of all, filtering of the list.
          filterEvntList
          //Checking if the variation is important enough in order to trigger our event processing
          if (interfaceKitPhidget.getSensorValue(4) > 600 || interfaceKitPhidget.getSensorValue(4) < 400) {
            val time = System.currentTimeMillis
            // Getting if existing the last event of that type
            getLast(VibrationEvent(time)) match {
                //if it exists, it check that the last light event is not too recent
              case Some(VibrationEvent(eventTime)) =>
                if ((time - eventTime) > VIBRATION_TIMEOUT) {
                  println("Vibation occured")
                  processEvent(VibrationEvent(time))
                }
                //if not create a new one.
              case None =>
                println("First Vib event")
                processEvent(VibrationEvent(time))
            }
          }
          //Case of a change event on the potentiometer
        case INDEX_MODE_POT =>
          //First of all, filtering of the list.
          filterEvntList
          Thread.sleep(1000)
          val time = System.currentTimeMillis
          // Getting if existing the last event of that type
          getLast(DemoPhaseEvent(time)) match {
              //if it exists, it check that the last light event is not too recent
            case Some(DemoPhaseEvent(eventTime)) =>
              if (time - eventTime > POT_TIMEOUT)
                println("Potentiometer changed !")
                processEvent(DemoPhaseEvent(time))
              //if not create a new one.
            case None => processEvent(DemoPhaseEvent(time))
          }
          // Unknown event...
        case _ =>
          //filtering the list to be sure
          filterEvntList
          println("Event Not Recognized")
      }
    })

    interfaceKitPhidget.addInputChangeListener((inputChangeEvent: InputChangeEvent) => {
      val time = System.currentTimeMillis


      if (inputChangeEvent.getIndex >= 4) {
        if (inputChangeEvent.getState) {
          val seatOfConcern = inputChangeEvent.getIndex - stdSouth.getNumberOfSeats
          if (stdNorth.seats(seatOfConcern)) {
            stdNorth.seats(seatOfConcern) = false
            println("Siège n°" + seatOfConcern + " libéré dans la tribune " + stdNorth.getStandName)
          }
          else {
            stdNorth.seats(seatOfConcern) = true
            println("Siège n°" + seatOfConcern + " occupé dans la tribune " + stdNorth.getStandName)
          }
          processEvent(StandEvent(time))
        }
      } else  {
        if (inputChangeEvent.getState) {
          val seatOfConcern = inputChangeEvent.getIndex
          if (stdSouth.seats(seatOfConcern)) {
            stdSouth.seats(seatOfConcern) = false
            println("Siège n°" + seatOfConcern + " libéré dans la tribune " + stdSouth.getStandName)
          }
          else {
            stdSouth.seats(seatOfConcern) = true
            println("Siège n°" + seatOfConcern + " occupé dans la tribune " + stdSouth.getStandName)
          }
          processEvent(StandEvent(time))
        }
      }

    })


    //In order to get temperature update more often (since it changes slowly) :
    val ex1 = new ScheduledThreadPoolExecutor(1)
    val task1 = new Runnable {
      def run():Unit = {
        processEvent(HeatEvent(System.currentTimeMillis()))
      }
    }
    val f1 = ex1.scheduleAtFixedRate(task1, 0, 20, TimeUnit.SECONDS)
    // f.cancel(false)


    //In order to get some updates of matches every 10 minutes
    val ex2 = new ScheduledThreadPoolExecutor(1)
    val task2 = new Runnable {
      def run():Unit = {
        ToServer.askForNewMatches(planning)
      }
    }
    val f2 = ex2.scheduleAtFixedRate(task2, 0, 10, TimeUnit.MINUTES)
  }

}
