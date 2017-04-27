/* -------------------------------------------------------------------------- 

   The BachT store

   
   AUTHOR : J.-M. Jacquet and D. Darquennes
   DATE   : March 2016

----------------------------------------------------------------------------*/

import scala.collection.mutable.Map
import scala.swing._

class BachTStore {

   var theStore = Map[(String, Int), Int]()

   def run_time() = {
      theStore = for {
         x <- theStore
         if x._1._2 > 0
      } yield ((x._1._1, x._1._2 - 1), x._2)
   }

   def tell(token:String, time: Int):Boolean = {
      if(time > 0) {
         if (theStore.contains(token, time))
         { theStore((token, time)) = theStore(token, time) + 1 }
         else
         { theStore = theStore ++ Map((token, time) -> 1) }
      }
      true
   }


   def ask(token:String, time: Int):Boolean = {
      val theStoreBis = for {
         x <- theStore
         if ((x._1._1).equals(token) && (x._2 >= 1))
      } yield ((x._1._1, x._1._2), x._2)
      if(!theStoreBis.isEmpty) {
         true
      } else {
         false
      }
   }

   def get(token:String, time: Int):Boolean = {
      val theStoreBis = for {
         x <- theStore
         if ((x._1._1).equals(token) && (x._2 >= 1))
      } yield ((x._1._1, x._1._2), x._2)
      if(!theStoreBis.isEmpty) {
         theStore((theStoreBis.head._1._1, theStoreBis.head._1._2)) = theStore((theStoreBis.head._1._1, theStoreBis.head._1._2)) - 1
         true
      } else {
         false
      }
   }


   def nask(token:String, time: Int):Boolean = {
      val theStoreBis = for {
         x <- theStore
         if ((x._1._1).equals(token) && (x._2 >= 1))
      } yield ((x._1._1, x._1._2), x._2)
      if(!theStoreBis.isEmpty) {
         false
      } else {
         true
      }
   }

   def print_store {
      print("{ ")
      for ((t,d) <- theStore)
         print ( t + "(" + theStore(t) + ")" )
      println(" }")
   }

   def clear_store {
      theStore = Map[(String, Int),Int]()
   }

}

object bb extends BachTStore {

   def reset { clear_store }

}