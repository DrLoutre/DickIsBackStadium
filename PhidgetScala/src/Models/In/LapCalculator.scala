package Models.In

import BlackBox.BlackBox
import Modes.{DetachedMode, NormalMode}
import com.phidgets.RFIDPhidget
import com.phidgets.event._

/**
  * Created by bri_e on 20-04-17.
  * Class representing each runners and every turns of each runner.
  *
  */
class LapCalculator(blackBox: BlackBox) {

  // Constants about the phidgets
  val PHIDGET_SERIAL = 335178
  val MIN_PASS_TIME  = 200

  // last time the phidget scanned a RFID
  var timeScanning:Double = 0
  // structure saving all the runners time, id and laps
  val runners:Runners = new Runners

  // phidget variable
  val rFIDPhidget:RFIDPhidget = new RFIDPhidget()

  // Setting the listener on detach and triggers the deteriorated version
  rFIDPhidget.addDetachListener((_: DetachEvent) => blackBox.currentMode match {
    case DetachedMode(kit, motors, _) =>
      blackBox.currentMode = DetachedMode(kit, motors, true)
    case _ =>
      blackBox.currentMode = DetachedMode(false, false, true)
  })

  // Setting the listener on attach and triggers the normal mode or updates the deteriorated mode
  rFIDPhidget.addAttachListener((_: AttachEvent) => blackBox.currentMode match {
    case DetachedMode(kit, motors, _) =>
      if (!kit && !motors) NormalMode else DetachedMode(kit, motors, false)
  })

  // Setting the listener on scan + save when it has ben scanned.
  rFIDPhidget.addTagGainListener((tagGainEvent: TagGainEvent) => {
    println("scannnedekqdsjfmkqsjdflmkzjdfmlkajsdfmlkjadmlkfjazlkdfjqmlskdjf")
    timeScanning = System.currentTimeMillis()
  })

  // Setting the listener on end of scan + juging if goal has happened
  rFIDPhidget.addTagLossListener((tagLossEvent: TagLossEvent) => {

    val now:Double = System.currentTimeMillis()
    if(Math.abs(timeScanning-now)>MIN_PASS_TIME){
      runners.scanned(tagLossEvent.getValue)
      val lastLapTime = runners.getIdPerfs(tagLossEvent.getValue).get(runners.getIdLapsNumber(tagLossEvent.getValue) - 1)
      val lapTime = runners.getIdPerfs(tagLossEvent.getValue).getLast
      println("Runner Id     : " + tagLossEvent.getValue +
            "\nRunning Time  : " + (lapTime-lastLapTime)/1000 + "sec" +
            "\nNumber of Laps: " + runners.getIdLapsNumber(tagLossEvent.getValue))
    }
    timeScanning = 0.00
  })

  print("\nAttaching RFID reader...")
  rFIDPhidget.open(PHIDGET_SERIAL)
  rFIDPhidget.waitForAttachment()

  println(" ...done")

}
