package Models.In

import BlackBox.BlackBox
import Events.LightEvent
import com.phidgets._
import com.phidgets.event._

/**
  * Created by bri_e on 20-04-17.
  * Class representing the ambient amount of lightness
  */

class Light(interfaceKitPhidget: InterfaceKitPhidget, sensorIndex: Int, blackBox: BlackBox){

  val MILI_INTERVAL:Int = 1000

  interfaceKitPhidget.addSensorChangeListener((sensorChangeEvent: SensorChangeEvent) => {
    if (sensorChangeEvent.getIndex == sensorIndex) {
      val time = System.currentTimeMillis
      blackBox.getLast(LightEvent(time)) match {
        case Some(LightEvent(eventTime)) => {
          // println("time : " + time + "  eventTime : " +  eventTime + "  diff : " + (time-eventTime))
          if ((time - eventTime) > MILI_INTERVAL)
            blackBox.processEvent(LightEvent(time))
        }
        case None =>
          blackBox.processEvent(LightEvent(time))
      }
    }
  })

  def retLightIntensity: Int = {
    interfaceKitPhidget.getSensorValue(sensorIndex)/10
  }
}



