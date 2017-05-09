package Models.Out

import com.phidgets.InterfaceKitPhidget

/**
  * Created by bri_e on 20-04-17.
  * The lighting is moderated in relation with the ambient light power
  */
class Lighting(interfaceKitPhidget: InterfaceKitPhidget) {
  val connectedLEDS:Int = 5
  var power: Int = 0
  //Initializing
  refreshLighting

  /**
    * sets the lighting at opposite intensity from the ambient light intensity (index in %)
    * @param lightIntensity the ambient light intensity
    */
  def updatePower(lightIntensity:Int):Unit = {
    power = 100 - lightIntensity
    refreshLighting
  }

  /**
    * refresh the connected leds in order with the power
    */
  def refreshLighting:Unit = {
    // number of connected LEDS
    val ledNumber = power / (100/connectedLEDS)
    println("Nombre de leds : " + ledNumber)
    for(x <- 0 to connectedLEDS)
      if (x<ledNumber)
        interfaceKitPhidget.setOutputState(x,true)
      else
        interfaceKitPhidget.setOutputState(x,false)
  }
}
