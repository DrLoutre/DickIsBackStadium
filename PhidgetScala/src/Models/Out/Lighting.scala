package Models.Out

import com.phidgets.InterfaceKitPhidget

/**
  * Created by bri_e on 20-04-17.
  * The lighting is moderated in relation with the ambient light power
  */
class Lighting(interfaceKitPhidget: InterfaceKitPhidget) {
  val connectedLEDS:Int = 5
  var power: Int = 0

  def updatePower(lightIntensity:Int):Unit = {
    power = 100 - lightIntensity
    refreshLighting
  }

  def refreshLighting:Unit = {
    // number of connected LEDS
    val ledNumber = power / (100/connectedLEDS)
    for(x <- 0 to connectedLEDS)
      if (x<ledNumber)
        interfaceKitPhidget.setOutputState(x,true)
      else
        interfaceKitPhidget.setOutputState(x,false)
  }
}
