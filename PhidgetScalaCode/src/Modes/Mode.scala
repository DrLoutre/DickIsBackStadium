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
