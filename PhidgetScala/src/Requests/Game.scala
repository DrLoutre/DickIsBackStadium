package Requests

/**
  * Created by bri_e on 11-05-17.
  */
case class Game(d:String, end:Boolean, i:Int, g1:Int, g2:Int, tId1:Int, tId2:Int) {
  val date : String   = d
  val ended : Boolean = end
  val id : Int        = i
  val goals1 : Int    = g1
  val goals2 : Int    = g2
  val teamID1 : Int   = tId1
  val teamID2 : Int   = tId2
}
