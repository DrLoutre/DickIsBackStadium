package BlackBox

import Events.{PassageEvent, VibrationEvent}

/**
  * Created by bri_e on 20-04-17.
  * Case Window in relation with a the goal sensors. 
  */
class GoalCase(passageEvent: PassageEvent) {
  var vibrationEvent:VibrationEvent = _

  def hasGoalHappened(secondEvent: VibrationEvent): Boolean = {
    vibrationEvent = secondEvent
    (vibrationEvent.eventTime - passageEvent.eventTime) < 1000
  }

}
