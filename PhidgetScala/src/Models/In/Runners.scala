package Models.In

import java.util
import java.util._

/**
  * Created by bri_e on 20-04-17.
  * Modelling all the runners, theirs turns and theirs laps and times
  */
class Runners {

  //The different lists
  // List of RFID Tags
  var idList:util.LinkedList[String] = new util.LinkedList[String]()
  // List of number of turns by tags
  var idNumber:util.LinkedList[Integer] = new util.LinkedList[Integer]()
  // List List of performances by Tags
  var time:util.LinkedList[util.LinkedList[Long]] = new util.LinkedList[util.LinkedList[Long]]()

  /**
    * @param id id that was scanned and to witch we will either create a new runner either add a lap
    */
  def scanned(id: String):Unit = {      // Getting the time :
    val now = System.currentTimeMillis()
    // getting last infos
    val index = idList.indexOf(id)
    val newVal = idNumber.get(index)
    val perfs:util.LinkedList[Long] = time.get(index)
    // If the turn lasted more than 5 minutes, we count a new run, so we reset the runner.
    if (perfs.getLast - now > 300000) {
      reset(id)
    }
    if (idList.contains(id)) {
      // Coutning a new turn
      perfs.addLast(System.currentTimeMillis())
      idNumber.set(index, Predef.Integer2int(newVal) + 1)
      time.set(index, perfs)
    } else {
      // Creating a new runner
      val tempLinked = new util.LinkedList[Long]()
      tempLinked.addLast(System.currentTimeMillis)
      idList.addLast(id)
      idNumber.addLast(0)
      time.addLast(tempLinked)
    }
  }

  /**
    * @param id id of the runner that will be reset
    */
  def reset(id: String):Unit = {
    if(idList.contains(id)){
      val index = idList.indexOf(id)
      idNumber.remove(index)
      time.remove(index)
      scanned(id)
    } else
      println("Nothing to be Reset")
  }

  /**
    * @param id id of the runner of which datas will be sent
    * @return the performances in laps of the runner
    */
  def getIdLapsNumber(id: String): Int =
    if(idList.contains(id))
      idNumber.get(idList.indexOf(id))
    else
      0

  /**
    * @param id id of the runner of which we want perfs
    * @return list of the values of turns of the runner
    */
  def getIdPerfs(id: String): util.LinkedList[Long] = {
    if(idList.contains(id))
      time.get(idList.indexOf(id))
    else
      null
  }

  def getTotal(id:String):Long = {
    val index:Int = idList.indexOf(id)
    val subList = time.get(index)
    var res:Long = 0
    subList.forEach(res += _)
    res
  }
}
