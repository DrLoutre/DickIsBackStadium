package Models.Out

import BlackBox.BlackBox
import Modes.{DetachedMode, NormalMode}
import com.phidgets.AdvancedServoPhidget
import com.phidgets.event.{AttachEvent, AttachListener, DetachEvent, DetachListener}

/**
  * Created by bri_e on 20-04-17.
  * The roof can be opened xor closed.
  * Both ServoMotor works in coalition in order to close the roof. 
  */
class Roof(blackBox: BlackBox) {

  //Set the phidget values.
  val SERVO1_SERIAL:Int = 305832
  val SERVO2_SERIAL:Int = 305826
  val SERVO_INDEX:Int = 0
  val VAL_ROOF1_CLOSED:Double = 0.00
  val VAL_ROOF2_CLOSED:Double = 180.00
  val VAL_ROOF1_OPEN:Double = 180.00
  val VAL_ROOF2_OPEN:Double = 0.00
  val SPEED   = 100.0

  //Initialization
  var open:Boolean = false
  val servo1:AdvancedServoPhidget = new AdvancedServoPhidget
  val servo2:AdvancedServoPhidget = new AdvancedServoPhidget

  /**
    * In case of detachment
    */
  def detachServo:Unit = {
    blackBox.currentMode match {
      case DetachedMode(kit, _, rfid) =>
        blackBox.currentMode = DetachedMode(kit, true, rfid)
      case _ => blackBox.currentMode = DetachedMode(false, true, false)
    }
  }

  /**
    * In case of re-attachment
    */
  def attachServo(attachEvent: AttachEvent):Unit = {
    println("SERVO ATTACHED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!SERVO ATTACHED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!SERVO ATTACHED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!SERVO ATTACHED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!SERVO ATTACHED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!SERVO ATTACHED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!SERVO ATTACHED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!SERVO ATTACHED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!SERVO ATTACHED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!SERVO ATTACHED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    println(attachEvent.getSource + "    string     " + attachEvent.toString)
    blackBox.currentMode match {
      case DetachedMode(kit, _, rfid) =>
        if (!kit && !rfid) {
          if (servo1.isAttached && servo2.isAttached)
            initialize
            blackBox.currentMode = NormalMode()
        } else {
          if (servo1.isAttached && servo2.isAttached) blackBox.currentMode = DetachedMode(kit, false, rfid)
        }
    }
  }

  //Setting the listeners
  servo1.addDetachListener((detachEvent: DetachEvent) => detachServo)
  servo1.addAttachListener((attachEvent: AttachEvent) => attachServo(attachEvent))
  servo2.addDetachListener((detachEvent: DetachEvent) => detachServo)
  servo2.addAttachListener((attachEvent: AttachEvent) => attachServo(attachEvent))

  print("\nAttaching both servo motors... ")
  servo1.open(SERVO1_SERIAL)
  servo2.open(SERVO2_SERIAL)
  servo1.waitForAttachment()
  servo2.waitForAttachment()
  initialize
  println("...done")

  /**
    * Close the roof, ignore if already done.
    */
  def closeRoof:Unit = {
    if (open) {
      servo1.setPosition(SERVO_INDEX, VAL_ROOF1_CLOSED)
      servo2.setPosition(SERVO_INDEX, VAL_ROOF2_CLOSED)
      println("      ======> roof closed")
      open = false
    } else
      println("      ======> roof already closed")
  }

  /**
    * Open the roof, ignore if already done
    */
  def openRoof:Unit = {
    if (!open) {
      servo1.setPosition(SERVO_INDEX, VAL_ROOF1_OPEN)
      servo2.setPosition(SERVO_INDEX, VAL_ROOF2_OPEN)
      println("      ======> roof opened")
      open = true
    } else
      println("      ======> roof already opened")
  }

  def initialize:Unit = {
    servo1.setEngaged(SERVO_INDEX, true)
    servo2.setEngaged(SERVO_INDEX, true)
    servo1.setAcceleration(SERVO_INDEX, SPEED)
    servo2.setAcceleration(SERVO_INDEX, SPEED)
    servo1.setPosition(SERVO_INDEX, VAL_ROOF1_CLOSED)
    servo2.setPosition(SERVO_INDEX, VAL_ROOF2_CLOSED)
  }

}
