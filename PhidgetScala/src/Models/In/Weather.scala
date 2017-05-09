package Models.In

/**
  * Created by bri_e on 20-04-17.
  * Different possible weathers registered.
  */
abstract class Weather()

case class Rain() extends Weather {
  override def toString():String = "Rainy"
}
case class Cloud() extends Weather {
  override def toString: String = "Cloudy"
}
case class Sun() extends Weather {
  override def toString: String = "Clear"
}
case class Snow() extends Weather {
  override def toString: String = "Snowy"
}