package Mode

/**
  * Created by bri_e on 20-04-17.
  * Class containing the different modes that the service can take in account.
  */
abstract class Mode(forcedMatchMode:Boolean){
  def forcedMatch():Boolean = forcedMatchMode
}

case class NormalMode(forcedMatchMode:Boolean){
  def forcedMatch():Boolean = false
}

case class Demo_1_Mode(forceMatchMode:Boolean){
  def forcedMatch():Boolean = true
}
