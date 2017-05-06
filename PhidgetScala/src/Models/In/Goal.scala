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
  private val INDEX_IR = 3
  private val INDEX_VIB = 4

  var goal:Int = 0
  var lastGoal:Double = 0.0


  def getGoal:Int = goal
  def getLastGoal:Double = lastGoal
  def incrementGoal(time:Double):Unit = {
    goal += 1
    lastGoal = time
  }
}
