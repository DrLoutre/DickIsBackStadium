package Models.In

/**
  * Created by bri_e on 20-04-17.
  * Different possible weathers
  */
abstract class Weather
case class Rain() extends Weather {
  override def toString():String = "Pluvieux"
}
case class Cloud() extends Weather {
  override def toString: String = "Nuageux"
}
case class Sun() extends Weather {
  override def toString: String = "Dégagé"
}
case class Snow() extends Weather {
  override def toString: String = "Chutes de neige"
}