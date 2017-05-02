package Models.In

import java.util
import java.util.{ArrayList, Date}

/**
  * Created by bri_e on 20-04-17.
  * Planning of every planned match
  */
class MatchPlanning() {

  var planned: util.ArrayList[Match] = new util.ArrayList[Match]

  def addMatchToList(start: Date, end: Date): Unit = {
    deleteOldMatches()
    planned.add(new Match(start, end))
  }

  def areWeDuringAMatch: Boolean = {
    deleteOldMatches()
    val temporaryList: util.ArrayList[Match] = planned.clone.asInstanceOf[util.ArrayList[Match]]
    temporaryList.removeIf((s: Match) => !s.areWeDuringMatch)
    if (temporaryList.isEmpty)
      false
    else { //Normaly the DB must contain only one match at one hour.
      true
    }
  }

  def matchArrayList: util.ArrayList[Match] = {
    deleteOldMatches()
    planned
  }

  private def deleteOldMatches(): Unit = {
    planned.removeIf((s: Match) => s.isMatchFinished)
  }
}
