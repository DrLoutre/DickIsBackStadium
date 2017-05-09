package BlackBox

import Events.{PassageEvent, VibrationEvent}

/**
  * Created by bri_e on 20-04-17.
  * Case Window in relation with a the goal sensors.
  */
class GoalCase(passageEvent: PassageEvent, var lastGoal:Double) {

  val GOAL_MIN_LAP:Double = 8000.0
  val TIME_BETWEEN_EVENTs:Double = 800.0
  println("init")

  /**
    * Checking if the vibration event given in parameter is close enough from the passage event
    * that initialized this instance of GoalCase to suppose that a goal has happened.
    * Two goals must have a certain time between them.
    * @param secondEvent the vibration event that happened
    * @return the jugement about the goal.
    */
  def hasGoalHappened(secondEvent: VibrationEvent): Boolean = {
    println("Comparing those two times : " + secondEvent.eventTime + " and " + passageEvent.eventTime + " that makes a dffernce : " + (secondEvent.eventTime - passageEvent.eventTime))
    if ((secondEvent.eventTime - passageEvent.eventTime) < TIME_BETWEEN_EVENTs) println("passed")
    println("Since last goal : " + (System.currentTimeMillis()-lastGoal) + "because last goal was :" + lastGoal)
    if (System.currentTimeMillis()-lastGoal > GOAL_MIN_LAP) println("2nd passed")
    (secondEvent.eventTime - passageEvent.eventTime) < TIME_BETWEEN_EVENTs && (System.currentTimeMillis()-lastGoal > GOAL_MIN_LAP)
  }

  /**
    * Get the last time value of a goal in order to compare it with a potential new one.
    * @param last last time value of a goal.
    */
  def updateLastGoal(last:Double):Unit ={
    lastGoal = last
  }

}
