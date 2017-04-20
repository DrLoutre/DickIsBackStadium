package Models.In

import java.util.Date

/**
  * Created by bri_e on 20-04-17.
  * Class 
  */
class Match(startDate: Date, endDate: Date) {
  
  def areWeDuringMatch: Boolean = {
    val today = new Date
    if (today.after(startDate) && today.before(endDate)) true
    else false
  }

  def isMatchFinished: Boolean = {
    val today = new Date
    if (today.after(endDate)) true
    else false
  }

  override def toString: String = "Date début : " + startDate + " et date de fin : " + endDate
}
