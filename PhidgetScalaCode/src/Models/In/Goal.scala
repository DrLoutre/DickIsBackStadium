package Models.In

import BlackBox.BlackBox
import Events._
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}

/**
  * Created by bri_e on 20-04-17.
  * This object represent a goal, we listen to both passage event and vibration event.
  * With a calculation on the time lapse between both event in a correct order we will determinate if there is a goal or not
  */
class Goal(interfaceKitPhidget: InterfaceKitPhidget, indexPassage: Int, indexVibration: Int, blackBox: BlackBox) {
  private val MILI_INTERVAL = 200

  var goal:Int = 0
  var lastGoal:Double = 0.0

  interfaceKitPhidget.addSensorChangeListener((sensorChangeEvent: SensorChangeEvent) => {
    sensorChangeEvent.getIndex match {
      case indexPassage =>
        if (interfaceKitPhidget.getSensorValue(indexPassage) < 100) {
          val time = System.currentTimeMillis
          blackBox.getLast(PassageEvent(time)) match {
            case Some(PassageEvent(eventTime)) =>
              if (time - eventTime > MILI_INTERVAL)
                blackBox.processEvent(PassageEvent(time))
          }
        }
      case indexVibration =>
        if (interfaceKitPhidget.getSensorValue(indexPassage) > 500) {
          val time = System.currentTimeMillis
          blackBox.getLast(VibrationEvent(time)) match {
            case Some(VibrationEvent(eventTime)) =>
              if (time - eventTime > MILI_INTERVAL)
                blackBox.processEvent(VibrationEvent(time))
          }
        }
    }
  })

  def getGoal:Int = goal
  def getLastGoal:Double = lastGoal
  def incrementGoal(time:Double):Unit = {
    goal += 1
    lastGoal = time
  }
}
