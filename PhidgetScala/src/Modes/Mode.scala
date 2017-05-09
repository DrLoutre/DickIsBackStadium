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
