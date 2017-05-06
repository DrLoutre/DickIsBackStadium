/* -------------------------------------------------------------------------- 

   The BachT store

   
   AUTHOR : J.-M. Jacquet and D. Darquennes
   DATE   : March 2016

----------------------------------------------------------------------------*/

import scala.collection.mutable.Map
import scala.swing._

/**
  * Cet class a dû être modifié pour faire apparaître l'intervalle de temps de validité des tokens.
  */
class BachTStore {

   /**
     * Le premier élément du couple représente donc le "nom" du token et le second élément est un couple qui représente l'intervalle de validité des tokens.
     */
   var theStore = Map[(String, (Int, Int)), Int]()
   var hour = 0;

   def give_time():Int = {
      hour
   }

   /**
     * Permet de ne conserver que les tokens qui ont un temps de fin de validité supérieur au temps actuel.
     */
   def run_time() = {
      theStore = for {
         x <- theStore
         if x._1._2._2 > hour
      } yield ((x._1._1, x._1._2), x._2)
      hour += 1
   }

   def tell(token:String, begin: Int, end: Int):Boolean = {
      if (theStore.contains(token, (begin, end)))
      { theStore((token, (begin, end))) = theStore(token, (begin, end)) + 1 }
      else
      { theStore = theStore ++ Map((token, (begin, end)) -> 1) }
      true
   }


   def ask(token:String, begin: Int, end: Int):Boolean = {
      val theStoreBis = for {
         x <- theStore
         if ((x._1._1).equals(token) && (x._2 >= 1) && begin <= hour && end >= hour && x._1._2._1 <= hour && x._1._2._2 >= hour)
      } yield ((x._1._1, x._1._2), x._2)
      if(!theStoreBis.isEmpty) {
         true
      } else {
         false
      }
   }

   def get(token:String, begin: Int, end: Int):Boolean = {
      val theStoreBis = for {
         x <- theStore
         if ((x._1._1).equals(token) && (x._2 >= 1) && begin <= hour && end >= hour && x._1._2._1 <= hour && x._1._2._2 >= hour)
      } yield ((x._1._1, x._1._2), x._2)
      if(!theStoreBis.isEmpty) {
         theStore((theStoreBis.head._1._1, theStoreBis.head._1._2)) = theStore((theStoreBis.head._1._1, theStoreBis.head._1._2)) - 1
         true
      } else {
         false
      }
   }


   def nask(token:String, begin: Int, end: Int):Boolean = {
      val theStoreBis = for {
         x <- theStore
         if ((x._1._1).equals(token) && (x._2 >= 1) && begin <= hour && end >= hour && x._1._2._1 <= hour && x._1._2._2 >= hour)
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
      theStore = Map[(String, (Int, Int)),Int]()
   }

}

object bb extends BachTStore {

   def reset { clear_store }

}