package Modes

/**
  * Created by bri_e on 20-04-17.
  * Class containing the different modes that the service can take in account.
  */
abstract class Mode(var isMatch:Boolean) {
  def inGame:Boolean = isMatch
  override def toString: String = this.getClass.toString
}

/**
  * This is the normal mode, every action undertake by this mode is how it should be in production
  */
case class NormalMode() extends Mode(isMatch = false) {
}

/**
  * This is the demo mode 1. Every this is the same as in normal mode but the match mode is forced in every situation.
  */
case class Demo_1_Mode() extends Mode(isMatch = true) {
  override def inGame = true
}

/**
  * This is the demo mode 2. Every thing in this mode is normal but its snowing and it is -5°c
  */
case class Demo_2_Mode() extends Mode(isMatch = false) {
}

/**
  * This is the demo mode 3. Every thing in this mode is normal but we're at night.
  */
case class Demo_3_Mode() extends Mode(isMatch = false) {
}

/**
  * This is the demo mode 4. Everything in this mode is normal but it's 30°C and the weather is sunny
  */
case class Demo_4_Mode() extends Mode(isMatch = false) {
}

/**
  * The detached moe is a reduced mode which must be set at detachment of any phidget. It deteriorates the functionalities in correlation with the detached tools.
  * @param isKitDetached  when the kit is detached
  * @param isRoofDetached when the servo's are detached
  * @param isRFIDDetached when the RFID reader is detached
  */
case class DetachedMode(var isKitDetached:Boolean, var isRoofDetached:Boolean, var isRFIDDetached:Boolean) extends Mode(isMatch = false) {
  def kitDetached(b:Boolean):Unit = isKitDetached = b
  def roofDettached(b:Boolean):Unit = isRoofDetached = b
  def rfidDetached(b:Boolean):Unit = isRFIDDetached = b
}
