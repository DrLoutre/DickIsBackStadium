package Models.In

import BlackBox.BlackBox
import Events.{DemoPhaseEvent, LightEvent}
import Modes._
import com.phidgets.InterfaceKitPhidget
import com.phidgets.event.{SensorChangeEvent, SensorChangeListener}


/**
  * Created by bri_e on 20-04-17.
  * This class is the model of the potentiometer stating in which mode we are
  * As an example, the first 90° will be the normal mode, from 91° to 180° will be demo mode 1 and so one...
  */
class DemoModePotentiometer(interfaceKitPhidget: InterfaceKitPhidget, sensorIndex: Int, blackBox: BlackBox) {
  private val MILI_INTERVAL = 500
  private val MAX_CURSVAL   = 1000
  private val NBR_MODES     = 2
  private val POT_SETTING_TIME= 2000

  interfaceKitPhidget.addSensorChangeListener((sensorChangeEvent: SensorChangeEvent) =>
    if (sensorChangeEvent.getIndex == sensorIndex) {
      val time = System.currentTimeMillis
      blackBox.getLast(DemoPhaseEvent(time)) match {
        case Some(DemoPhaseEvent(eventTime)) =>
          if (time - eventTime > MILI_INTERVAL)
            Thread.sleep(POT_SETTING_TIME)
      }
      blackBox.processEvent(DemoPhaseEvent(time))
  })

  def getCurrentMode:Mode = {
    val value:Int = interfaceKitPhidget.getSensorValue(sensorIndex)/(MAX_CURSVAL/NBR_MODES)%NBR_MODES
    value match { //TODO: Create correct modes to test.
      case 0 =>
        println("Setting to normal mode ... ")
        NormalMode()
      case 1 =>
        println("Setting to match mode ... ")
        NormalMode()
      case 2 =>
        println("Setting to Demo mode 1 ... ")
        Demo_1_Mode()
      case _ =>
        println("Demo mode x not yet implemented")
        NormalMode()
    }
  }

}
