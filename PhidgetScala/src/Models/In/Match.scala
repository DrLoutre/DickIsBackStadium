package Models.In

import java.util.Date

/**
  * Created by bri_e on 20-04-17.
  * Class modelling the matches that takes places in the planning
  */
class Match(startDate: Date, endDate: Date) {

  /**
    * @return if we are or not during the match
    */
  def areWeDuringMatch: Boolean = {
    val today = new Date
    if (today.after(startDate) && today.before(endDate)) true
    else false
  }

  /**
    * @return if the match is finished or not
    */
  def isMatchFinished: Boolean = {
    val today = new Date
    if (today.after(endDate)) true
    else false
  }

  override def toString: String = "Date début : " + startDate + " et date de fin : " + endDate
}
