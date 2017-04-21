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
    power = 100/lightIntensity
    refreshLighting
  }

  def refreshLighting:Unit = {
    var rest = power
    val step = 100 / connectedLEDS
    var ledNumber = 0
    while (step <= rest) {
      ledNumber += 1
      rest -= step
    }

    var x = 0
    while (x < ledNumber) {
      if (!interfaceKitPhidget.getOutputState(x)) {
        interfaceKitPhidget.setOutputState(x, true)
        x += 1
      }
    }

    var i = ledNumber
    while (i < connectedLEDS) {
      if (interfaceKitPhidget.getOutputState(i)) {
        interfaceKitPhidget.setOutputState(i, false)
        i += 1
      }
    }
  }
}
