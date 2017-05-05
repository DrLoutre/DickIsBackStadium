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


  def retLightIntensity: Int = {
    interfaceKitPhidget.getSensorValue(sensorIndex)/10
  }
}



