package Models.In

import java.io.FileInputStream

import BlackBox.BlackBox
import Events.{DemoPhaseEvent, LightEvent}
import Modes._
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}
import sun.audio.{AudioPlayer, AudioStream}


/**
  * Created by bri_e on 20-04-17.
  * This class is the model of the potentiometer stating in which mode we are
  * As an example, the first 90° will be the normal mode, from 91° to 180° will be demo mode 1.
  */
class DemoModePotentiometer(interfaceKitPhidget: InterfaceKitPhidget, sensorIndex: Int, blackBox: BlackBox) {

  // Different constants for the potentiometer.
  private val MILI_INTERVAL = 500
  private val MAX_CURSVAL   = 1000
  private val NBR_MODES     = 5
  private val POT_SETTING_TIME= 2000

  /**
    * @return the current mode given by the potentiometer sensor
    */
  def getCurrentMode:Mode = {
    val value:Int = interfaceKitPhidget.getSensorValue(sensorIndex)/(MAX_CURSVAL/NBR_MODES)
    println("======================================================<>>>>> Value of pot : " + value)
    value match {
      case 0 => // Normal mode.
        println("Setting to normal mode ... ")
        NormalMode()
      case 1 => // Normal mode but matches are forced.
        println("Setting to demo mode 1 ... Normal mode but matches are forced. ")
        Demo_1_Mode()
      case 2 => // Normal mode but snow and freezing
        println("Setting to Demo mode 2 ... Normal mode but snow and freezing.")
        Demo_2_Mode()
      case 3 => // Normal mode but night
        println("Setting to Demo mode 3 ... Normal mode but night.")
        Demo_3_Mode()
      case 4 => // Normal mode but under the tropics
        println("Setting to Demo mode 4 ... Normal mode but under the tropics.")
        Demo_4_Mode()
      case _ =>
        println("Demo mode x not yet implemented")
        NormalMode()
    }
  }

  def playTheme():Unit = {

    var mode:String = ""

    getCurrentMode match {
      case NormalMode() => mode = "mode0.wav"
      case Demo_1_Mode() => mode = "mode1.wav"
      case Demo_2_Mode() => mode = "mode2.wav"
      case Demo_3_Mode() => mode = "mode3.wav"
      case Demo_4_Mode() => mode = "mode4.wav"
    }

    try {
      // open the sound file as a Java input stream
      val gongFile = "/root/Documents/ouptutdir/" + mode
      val in = new FileInputStream(gongFile)

      // create an audiostream from the inputstream
      val audioStream = new AudioStream(in)

      // play the audio clip with the audioplayer class
      AudioPlayer.player.start(audioStream)
    } catch {
      case e:Exception => println("Exception while opening theme" + e)
    }
  }

}
