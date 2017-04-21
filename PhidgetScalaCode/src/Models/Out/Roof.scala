package Models.Out

import com.phidgets.AdvancedServoPhidget

/**
  * Created by bri_e on 20-04-17.
  * The roof can be opened xor closed.
  * Both ServoMotor works in coalition in order to close the roof. 
  */
class Roof {
  //Todo : Add 2nd ServoMotor

  val SERVO_SERIAL:Int = 305832
  val SERVO_INDEX:Int = 0
  val VAL_ROOF_CLOSED:Double = 15.00
  val VAL_ROOF_OPEN:Double = 220.00

  var open:Boolean = false
  val servo:AdvancedServoPhidget = new AdvancedServoPhidget

  //Add listeners for error

  servo.open(SERVO_SERIAL)
  servo.waitForAttachment()
  servo.setEngaged(SERVO_INDEX, true)
  servo.setPosition(SERVO_INDEX, VAL_ROOF_CLOSED)

  def closeRoof:Unit = {
    if (open) {
      servo.setPosition(SERVO_INDEX, VAL_ROOF_CLOSED)
      println("      ======> roof closed")
      open = false
    } else
      println("      ======> roof already closed")
  }

  def openRoof:Unit = {
    if (!open) {
      servo.setPosition(SERVO_INDEX, VAL_ROOF_OPEN)
      println("      ======> roof opened")
      open = true
    } else
      println("      ======> roof already opened")
  }


}
