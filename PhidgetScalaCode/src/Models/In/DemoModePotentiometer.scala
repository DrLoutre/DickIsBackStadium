package Models.In

import BlackBox.BlackBox
import Events.DemoPhaseEvent
import Mode.{Demo_1_Mode, Mode, NormalMode}
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}

import scala.reflect.internal.Mode

/**
  * Created by bri_e on 20-04-17.
  * This class is the model of the potentiometer stating in which mode we are
  * As an example, the first 90° will be the normal mode, from 91° to 180° will be demo mode 1 and so one...
  */
class DemoModePotentiometer(interfaceKitPhidget: InterfaceKitPhidget, sensorIndex: Int, blackBox: BlackBox) {
  val MILI_INTERVAL = 500
  val MAX_CURSVAL   = 1000
  val NBR_MODES     = 2

  interfaceKitPhidget.addSensorChangeListener(new SensorChangeListener {
    override def sensorChanged(sensorChangeEvent: SensorChangeEvent): Unit = {
      if(sensorChangeEvent.getIndex == sensorIndex /*TODO : && si noEvent de la blackbox + timing*/) {
        Thread.sleep(2000)
        DemoPhaseEvent(System.currentTimeMillis)
        //Todo : process event
      }

    }
  })

  def getCurrentMode = {
    val value:Int = interfaceKitPhidget.getSensorValue(sensorIndex)/(MAX_CURSVAL/NBR_MODES)%NBR_MODES
    value match {
      case 0 =>
        println("Setting to normal mode ... ")
        NormalMode
      case 1 =>
        println("Setting to forced match mode ... ")
        NormalMode
      case 2 =>
        println("Setting to Demo mode 1 ... ")
        Demo_1_Mode
      case _ => println("Demo mode x not yet implemented")
    }
  }




}
