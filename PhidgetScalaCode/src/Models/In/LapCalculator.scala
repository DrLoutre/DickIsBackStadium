package Models.In

import BlackBox.BlackBox
import com.phidgets.RFIDPhidget
import com.phidgets.event.{TagGainEvent, TagGainListener, TagLossEvent, TagLossListener}

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
      //Todo:create and process new event (migrate that code in the processing of turns)
    }
    timeScanning = 0.00
  })
  rFIDPhidget.open(PHIDGET_SERIAL)




}
