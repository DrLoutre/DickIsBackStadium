
import java.io.FileInputStream

import BlackBox.BlackBox
import com.phidgets.InterfaceKitPhidget
import sun.audio.{AudioPlayer, AudioStream}

/**
  * Created by bri_e on 20-04-17.
  * main method launching the application
  */
object Main {
  def main(args: Array[String]): Unit = {

    try {
      // open the sound file as a Java input stream
      val gongFile = "/root/Documents/ouptutdir/begin.wav"
      val in = new FileInputStream(gongFile)

      // create an audiostream from the inputstream
      val audioStream = new AudioStream(in)

      // play the audio clip with the audioplayer class
      AudioPlayer.player.start(audioStream)
    } catch {
      case e:Exception => println("Exception while opening theme" + e)
    }

    print("Initialising the InterfaceKit Phidget . . . ")
    var interfaceKitPhidget:InterfaceKitPhidget = new InterfaceKitPhidget
    interfaceKitPhidget.open(329107)
    interfaceKitPhidget.waitForAttachment()
    println(". . . done !")
    print("Initialising blackbox . . . ")
    val blackBox:BlackBox = new BlackBox(interfaceKitPhidget)
    println(". . . done !")
  }
}
