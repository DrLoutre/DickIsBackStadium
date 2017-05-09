package Models.In

import BlackBox.BlackBox
import Events.StandEvent
import com.phidgets.{InterfaceKitPhidget, PhidgetException}
import com.phidgets.event.{InputChangeEvent, InputChangeListener}

/**
  * Created by bri_e on 20-04-17.
  * A stand has a certain number of seats which are taken or not.
  */
class Stand(interfaceKitPhidget: InterfaceKitPhidget, standName: String, numberOfSeats: Int, blackBox: BlackBox) {

  //the array of boolean that represent seats. the element is true if the seat is taken
  var seats:Array[Boolean] = new Array[Boolean](numberOfSeats)

  /**
    * @return the number of seats
   */
  def getNumberOfSeats: Int = numberOfSeats

  /**
    * @return return the name of the stand
    */
  def getStandName: String = standName

  /**
    * @return return the percentage of occupation of a stand
    */
  def getStandPercentage:Int = {
    seats.toList.count((elem:Boolean) => elem)*100/numberOfSeats
  }

  /**
    * @return the array of seats
    */
  def getSeats: Array[Boolean] = seats
}
