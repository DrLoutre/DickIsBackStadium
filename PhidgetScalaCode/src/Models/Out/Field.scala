package Models.Out

import com.phidgets.{InterfaceKitPhidget, PhidgetException}

/**
  * Created by bri_e on 20-04-17.
  * The field can be watered xor heated
  */
class Field(interfaceKitPhidget: InterfaceKitPhidget, indexRed: Int, indexBlue: Int, indexGreen: Int) {
  var is_Watering:Boolean = false
  var is_Heating: Boolean = false

  def isHeating:Boolean = isHeating
  def isWatering:Boolean = isWatering

  def setWatering(state:Boolean):Unit = {
    is_Watering = state
    setLight()
  }

  def setHeating(state: Boolean): Unit = {
    is_Heating = state
    setLight()
  }

  private def setLight():Unit = {
    if (is_Watering)
      if (is_Heating)
        println("Erreur : état incorrect !!")
      else {
        interfaceKitPhidget.setOutputState(indexGreen, false)
        interfaceKitPhidget.setOutputState(indexRed, false)
        interfaceKitPhidget.setOutputState(indexBlue, true)
      }

    if (is_Heating)
      if (is_Watering)
        println("Erreur : état incorrect !!")
      else {
        interfaceKitPhidget.setOutputState(indexBlue, false)
        interfaceKitPhidget.setOutputState(indexGreen, false)
        interfaceKitPhidget.setOutputState(indexRed, true)
      }
  }
}
