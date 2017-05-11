package Models.In

import java.util
import java.util.{ArrayList, Date}

/**
  * Created by bri_e on 20-04-17.
  * Planning of every planned match. Contains an array of matches that are planned in th stadium
  */
class MatchPlanning() {

  // array of matches that are planned in th stadium
  var planned: util.ArrayList[Match] = new util.ArrayList[Match]

  /**
    * add a match to the planning
    * @param start start date of the new match
    * @param end end date of the new match
    */
  def addMatchToList(start: Date, end: Date): Unit = {
    deleteOldMatches()
    println("Filtered list of matches...")
    planned.add(new Match(start, end))

  }

  /**
    * @return if we are during a match
    */
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

  /**
    * @return the actual list filtered from oldmatches
    */
  def matchArrayList: util.ArrayList[Match] = {
    deleteOldMatches()
    planned
  }

  /**
    * filtering old matches
    */
  private def deleteOldMatches(): Unit = {
    planned.removeIf((s: Match) => s.isMatchFinished)
  }
}
