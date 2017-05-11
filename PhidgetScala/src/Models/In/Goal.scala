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

  // Constants for the goal's sensors.
  private val TIMEOUT_BTW_TWO_GOALS:Long = 8000
  private val INDEX_IR = 3
  private val INDEX_VIB = 4

  // Number of goals and time of the last goal.
  var goal:Int = 0
  var lastGoal:Long = 10000000

  // getters
  def getGoal:Int = goal
  def getLastGoal:Long = lastGoal

  // increments the goal
  def incrementGoal(time:Long):Boolean = {
    if (lastGoal-time < TIMEOUT_BTW_TWO_GOALS) {
      println("Gooaaaal !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! the time set :" + time)
      goal += 1
      println("Goal number : " + goal)
      lastGoal = time
      true
    } else {
      println("Too soon")
      false
    }
  }
}
