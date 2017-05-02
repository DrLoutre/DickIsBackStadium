package Models.In

import BlackBox.BlackBox
import Modes.{DetachedMode, NormalMode}
import com.phidgets.RFIDPhidget
import com.phidgets.event._

/**
  * Created by bri_e on 20-04-17.
  * Class representing each runners and every turns of each runner
  */
class LapCalculator(blackBox: BlackBox) {

  val PHIDGET_SERIAL = 335178
  val MIN_PASS_TIME  = 200
  var timeScanning:Double = 0
  val runners:Runners = new Runners

  val rFIDPhidget:RFIDPhidget = new RFIDPhidget()

  rFIDPhidget.addDetachListener((_: DetachEvent) => blackBox.currentMode match {
    case DetachedMode(kit, motors, _) =>
      blackBox.currentMode = DetachedMode(kit, motors, true)
    case _ =>
      blackBox.currentMode = DetachedMode(false, false, true)
  })

  rFIDPhidget.addAttachListener((_: AttachEvent) => blackBox.currentMode match {
    case DetachedMode(kit, motors, _) =>
      if (!kit && !motors) NormalMode else DetachedMode(kit, motors, false)
  })

  rFIDPhidget.addTagGainListener((tagGainEvent: TagGainEvent) => timeScanning = System.currentTimeMillis())

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
