package Models.In

import BlackBox.BlackBox
import com.phidgets._
import com.phidgets.event._

/**
  * Created by bri_e on 20-04-17.
  * Class representing the ambient amount of lightness
  */

class Light(interfaceKitPhidget: InterfaceKitPhidget, sensorIndex: Int, blackBox: BlackBox){

  val MILI_INTERVAL:Int = 1000

  interfaceKitPhidget.addSensorChangeListener((sensorChangeEvent: SensorChangeEvent) => {
    if (true /*TODO : if we can get last element from the blackbox list from Light type*/ )
    //TODO : process a new light event
      println("Processing Light Event")
    else
      println("No Light Event Proceeded")
  })

  def retLightIntensity: Int = {
    interfaceKitPhidget.getSensorValue(sensorIndex)/10
  }
}



