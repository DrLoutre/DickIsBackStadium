import BlackBox.BlackBox
import com.phidgets.InterfaceKitPhidget

/**
  * Created by bri_e on 20-04-17.
  */
class Main {
  def main(args: Array[String]): Unit = {
    print("Initialising the InterfaceKit Phidget . . . ")
    val interfaceKitPhidget:InterfaceKitPhidget = InterfaceKitPhidget
    interfaceKitPhidget.open(329107)
    interfaceKitPhidget.waitForAttachment
    println(". . . done !")
    print("Initialising blackbox . . . ")
    val blackBox:BlackBox = new BlackBox(interfaceKitPhidget)
    println(". . . done !")
    Thread.sleep(60000000)
  }
}
