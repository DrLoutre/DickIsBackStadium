package Models.In

import java.util
import java.util._

/**
  * Created by bri_e on 20-04-17.
  * A runner has turns at a certain time
  */
class Runners {

  var idList:util.LinkedList[String] = new util.LinkedList[String]()
  var idNumber:util.LinkedList[Integer] = new util.LinkedList[Integer]()
  var time:util.LinkedList[util.LinkedList[Long]] = new util.LinkedList[util.LinkedList[Long]]()

  def scanned(id: String):Unit = {
    if (idList.contains(id)) {
      val index = idList.indexOf(id)
      val newVal = idNumber.get(index)
      val perfs:util.LinkedList[Long] = time.get(index)
      perfs.addLast(System.currentTimeMillis())
      idNumber.set(index, Predef.Integer2int(newVal) + 1)
      time.set(index, perfs)
    } else {
      val tempLinked = new util.LinkedList[Long]()
      tempLinked.addLast(System.currentTimeMillis)
      idList.addLast(id)
      idNumber.addLast(0)
      time.addLast(tempLinked)
    }
  }

  def reset(id: String):Unit = {
    if(idList.contains(id)){
      val index = idList.indexOf(id)
      idNumber.remove(index)
      time.remove(index)
      scanned(id)
    } else
      println("Nothing to be Reset")
  }

  def getIdLapsNumber(id: String): Int =
    if(idList.contains(id))
      idNumber.get(idList.indexOf(id))
    else
      0


  def getIdPerfs(id: String): util.LinkedList[Long] = {
    if(idList.contains(id))
      time.get(idList.indexOf(id))
    else
      null
  }

}
