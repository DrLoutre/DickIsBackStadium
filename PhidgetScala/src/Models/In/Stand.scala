package Models.In

import BlackBox.BlackBox
import Events.StandEvent
import com.phidgets.{InterfaceKitPhidget, PhidgetException}
import com.phidgets.event.{InputChangeEvent, InputChangeListener}

/**
  * Created by bri_e on 20-04-17.
  * A stand has a certain number of seats which are taken or not
  */
class Stand(interfaceKitPhidget: InterfaceKitPhidget, standName: String, numberOfSeats: Int, blackBox: BlackBox) {

  var seats:Array[Boolean] = new Array[Boolean](numberOfSeats)

  interfaceKitPhidget.addInputChangeListener((inputChangeEvent: InputChangeEvent) => {
    val time = System.currentTimeMillis
    if (inputChangeEvent.getState) {
      val seatOfConcern = inputChangeEvent.getIndex
      if (seats(seatOfConcern)) {
        seats(seatOfConcern) = false
        println("Siège n°" + seatOfConcern + " libéré dans la tribune " + standName)
      }
      else {
        seats(seatOfConcern) = true
        println("Siège n°" + seatOfConcern + " occupé dans la tribune " + standName)
      }
      blackBox.processEvent(StandEvent(time))
    }
  })

  def getNumberOfSeats: Int = numberOfSeats

  def getStandName: String = standName

  def getStandPercentage:Int = {
    seats.toList.count((elem:Boolean) => elem)/numberOfSeats
  }

  def getSeats: Array[Boolean] = seats
}
