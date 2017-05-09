package Models.Out

import com.phidgets.{InterfaceKitPhidget, PhidgetException}

/**
  * Created by bri_e on 20-04-17.
  * The field can be watered heated
  */
class Field(interfaceKitPhidget: InterfaceKitPhidget, indexRed: Int, indexBlue: Int, indexGreen: Int) {
  var is_Watering:Boolean = false
  var is_Heating: Boolean = false

  //Initializing the leds
  interfaceKitPhidget.setOutputState(indexBlue, false)
  interfaceKitPhidget.setOutputState(indexGreen, false)
  interfaceKitPhidget.setOutputState(indexRed, false)

  //returning the states of the field
  def isHeating:Boolean = isHeating
  def isWatering:Boolean = isWatering

  /**
    * @param state if we set or unset the watering
    */
  def setWatering(state:Boolean):Unit = {
    is_Watering = state
    setLight()
  }

  /**
    * @param state if we set or unset the heating
    */
  def setHeating(state: Boolean): Unit = {
    is_Heating = state
    setLight()
  }

  /**
    * Updates the light in order to explicit the action that is undertaken on the field.
    */
  private def setLight():Unit = {

    if (is_Watering) {
      // the led must be blue
      interfaceKitPhidget.setOutputState(indexGreen, false)
      interfaceKitPhidget.setOutputState(indexRed, false)
      interfaceKitPhidget.setOutputState(indexBlue, true)
    } else {
      //no more blue
      interfaceKitPhidget.setOutputState(indexBlue, false)
    }

    if (is_Heating) {
      // the led must be red
      interfaceKitPhidget.setOutputState(indexBlue, false)
      interfaceKitPhidget.setOutputState(indexGreen, false)
      interfaceKitPhidget.setOutputState(indexRed, true)
    } else {
      // no more red
      interfaceKitPhidget.setOutputState(indexRed, false)
    }
  }
}
