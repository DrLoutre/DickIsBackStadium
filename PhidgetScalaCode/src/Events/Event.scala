package Events

/**
  * Created by bri_e on 20-04-17.
  * Class containing the different case classes with different events
  */
abstract class Event()

case class BarEvent(eventTime:Double) extends Event
case class DemoPhaseEvent(eventTime:Double) extends Event
case class HeatEvent(eventTime:Double) extends Event
case class LightEvent(eventTime:Double) extends Event
case class NewMatchPlanEvent(eventTime:Double) extends Event
case class PassageEvent(eventTime:Double) extends Event
case class StandEvent(eventTime:Double) extends Event
case class TurnEvent(eventTime:Double) extends Event
case class VibrationEvent(eventTime:Double) extends Event
