package Events

/**
  * Created by bri_e on 20-04-17.
  * Class containing the different case classes with different events
  */
abstract class Event(var eventTime:Long)

case class BarEvent(var evntTime:Long)           extends Event(evntTime)
case class DemoPhaseEvent(var evntTime:Long)     extends Event(evntTime)
case class HeatEvent(var evntTime:Long)          extends Event(evntTime)
case class LightEvent(var evntTime:Long)         extends Event(evntTime)
case class NewMatchPlanEvent(var evntTime:Long)  extends Event(evntTime)
case class PassageEvent(var evntTime:Long)       extends Event(evntTime)
case class StandEvent(var evntTime:Long)         extends Event(evntTime)
case class TurnEvent(var evntTime:Long)          extends Event(evntTime)
case class VibrationEvent(var evntTime:Long)     extends Event(evntTime)
