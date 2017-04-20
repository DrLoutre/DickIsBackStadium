package Models.In

import BlackBox.BlackBox
import Events.PassageEvent
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}

/**
  * Created by bri_e on 20-04-17.
  * This object represent a goal, we listen to both passage event and vibration event.
  * With a calculation on the time lapse between both event in a correct order we will determinate if there is a goal or not
  */
class Goal(interfaceKitPhidget: InterfaceKitPhidget, indexPassage: Int, indexVibration: Int, blackBox: BlackBox) {

  var goal:Int = 0
  var lastGoal:Double = 0.0

  interfaceKitPhidget.addSensorChangeListener(new SensorChangeListener {
    override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
      sensorChangeEvent.getIndex match {
        case indexPassage =>
          if (interfaceKitPhidget.getSensorValue(indexPassage) < 100){
            //Todo check if noEvent + timout  if ok =>  new passage event + proceed it
          }
        case indexVibration => {
          if (interfaceKitPhidget.getSensorValue(indexPassage) > 500){
            //Todo check if noEvent + timout if ok => new vibration event + proceed it
          }
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
