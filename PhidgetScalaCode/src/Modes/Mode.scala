package Modes

/**
  * Created by bri_e on 20-04-17.
  * Class containing the different modes that the service can take in account.
  */
abstract class Mode(var isMatch:Boolean) {
  def inGame:Boolean = isMatch
  override def toString: String = this.getClass.toString
}

case class NormalMode() extends Mode(isMatch = false) {
}

case class Demo_1_Mode() extends Mode(isMatch = true) {
}

case class DetachedMode(var isKitDetached:Boolean, var isRoofDetached:Boolean, var isRFIDDetached:Boolean) extends Mode(isMatch = false) {
  def kitDetached(b:Boolean):Unit = isKitDetached = b
  def roofDettached(b:Boolean):Unit = isRoofDetached = b
  def rfidDetached(b:Boolean):Unit = isRFIDDetached = b
}
