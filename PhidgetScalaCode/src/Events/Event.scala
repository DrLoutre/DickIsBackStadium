package Events

/**
  * Created by bri_e on 20-04-17.
  * Class containing the different case classes with different events
  */
abstract class Event(eventTime:Double) {
  def time():Double = eventTime
}

case class BarEvent(eventTime:Double)
case class DemoPhaseEvent(eventTime:Double)
case class HeatEvent(eventTime:Double)
case class LightEvent(eventTime:Double)
case class NewMatchPlanEvent(eventTime:Double)
case class PassageEvent(eventTime:Double)
case class StandEvent(eventTime:Double)
case class TurnEvent(eventTime:Double)
case class VibrationEvent(eventTime:Double)
