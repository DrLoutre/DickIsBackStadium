/* -------------------------------------------------------------------------- 

   Complete code for the command-line interpreter


   AUTHOR : J.-M. Jacquet and D. Darquennes
   DATE   : March 2016

----------------------------------------------------------------------------*/

class Expr
case class bacht_ast_empty_agent() extends Expr
case class bacht_ast_primitive(primitive: String, token: String) extends Expr
case class bacht_ast_agent(op: String, agenti: Expr, agentii: Expr) extends Expr
case class bacht_ast_wait(primitive: String, time: Int) extends Expr
import scala.util.parsing.combinator._
import scala.util.matching.Regex

/**
  * Le code suivant a été modifié de manière à faire reconnaitre la primitive wait au parser.
  */
class BachTParsers extends RegexParsers {

  def token 	: Parser[String] = ("[a-z][0-9a-zA-Z_]*").r ^^ {_.toString}
  def duration 	: Parser[Int] = ("[0-9]*").r ^^ {_.toInt}

  val opChoice  : Parser[String] = "+"
  val opPara    : Parser[String] = "||"
  val opSeq     : Parser[String] = ";"

  def primitive : Parser[Expr]   = "tell("~token~")" ^^ {
    case _ ~ vtoken ~ _  => bacht_ast_primitive("tell",vtoken) }  |
    "ask("~token~")" ^^ {
      case _ ~ vtoken ~ _  => bacht_ast_primitive("ask",vtoken) }   |
    "get("~token~")" ^^ {
      case _ ~ vtoken ~ _  => bacht_ast_primitive("get",vtoken) }   |
    "nask("~token~")" ^^ {
      case _ ~ vtoken ~ _  => bacht_ast_primitive("nask",vtoken) }  |
    "wait("~duration~")" ^^ {
      case _ ~ vtoken ~ _  => bacht_ast_wait("wait",vtoken) }

  def agent = compositionChoice

  def compositionChoice : Parser[Expr] = compositionPara~rep(opChoice~compositionChoice) ^^ {
    case ag ~ List() => ag
    case agi ~ List(op~agii)  => bacht_ast_agent(op,agi,agii) }

  def compositionPara : Parser[Expr] = compositionSeq~rep(opPara~compositionPara) ^^ {
    case ag ~ List() => ag
    case agi ~ List(op~agii)  => bacht_ast_agent(op,agi,agii) }

  def compositionSeq : Parser[Expr] = simpleAgent~rep(opSeq~compositionSeq) ^^ {
    case ag ~ List() => ag
    case agi ~ List(op~agii)  => bacht_ast_agent(op,agi,agii) }

  def simpleAgent : Parser[Expr] = primitive | parenthesizedAgent

  def parenthesizedAgent : Parser[Expr] = "("~>agent<~")"

}

/**
  * Nous avons donc créé un nouveau parse_wait en rapport avec la primitive que nous avons ajouté précédemment.
  */
object BachTSimulParser extends BachTParsers {

  def parse_primitive(prim: String) = parseAll(primitive,prim) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }

  def parse_agent(ag: String) = parseAll(agent,ag) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }

  def parse_wait(prim: String) = parseAll(primitive,prim) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }
}
import scala.collection.mutable.Map
import scala.swing._

/**
  * Cette class n'a pas du être modifié
  */
class BachTStore {

  var theStore = Map[String,Int]()

  def tell(token:String):Boolean = {
    if (theStore.contains(token))
    { theStore(token) = theStore(token) + 1 }
    else
    { theStore = theStore ++ Map(token -> 1) }
    true
  }


  def ask(token:String):Boolean = {
    if (theStore.contains(token))
      if (theStore(token) >= 1) { true }
      else { false }
    else false
  }


  def get(token:String):Boolean = {
    if (theStore.contains(token))
      if (theStore(token) >= 1)
      { theStore(token) = theStore(token) - 1
        true
      }
      else { false }
    else false
  }


  def nask(token:String):Boolean = {
    if (theStore.contains(token))
      if (theStore(token) >= 1) { false }
      else { true }
    else true
  }

  def print_store {
    print("{ ")
    for ((t,d) <- theStore)
      print ( t + "(" + theStore(t) + ")" )
    println(" }")
  }

  def clear_store {
    theStore = Map[String,Int]()
  }

}

object bb extends BachTStore {

  def reset { clear_store }

}

import scala.util.Random
import language.postfixOps

/**
  * Cette class a été modifié afin de permettre la gestion de la primitive wait.
  */
class BachTSimul(var bb: BachTStore) {

  var failure = false
  var hour = 0
  val bacht_random_choice = new Random()

  def run_one(agent: Expr):(Boolean,Expr) = {

    agent match {
      case bacht_ast_primitive(prim,token) =>
      {  if (exec_primitive(prim,token)) { (true,bacht_ast_empty_agent()) }
      else { (false,agent) }
      }

      case bacht_ast_wait(prim, time) => {
        (false, bacht_ast_wait(prim, time))
      }

      case bacht_ast_agent(";",ag_i,ag_ii) =>
      {  run_one(ag_i) match
      { case (false,_) => (false,agent)
        case (true,bacht_ast_empty_agent()) => (true,ag_ii)
        case (true,ag_cont) => (true,bacht_ast_agent(";",ag_cont,ag_ii))
      }
      }

      case bacht_ast_agent("||",ag_i,ag_ii) =>
      {  var branch_choice = bacht_random_choice.nextInt(2)
        if (branch_choice == 0)
        { run_one( ag_i ) match
        { case (false,_) =>
          { run_one( ag_ii ) match
          { case (false,_)
            => (false,agent)
            case (true,bacht_ast_empty_agent())
            => (true,ag_i)
            case (true,ag_cont)
            => (true,bacht_ast_agent("||",ag_i,ag_cont))
          }
          }
          case (true,bacht_ast_empty_agent())
          => (true,ag_ii)
          case (true,ag_cont)
          => (true,bacht_ast_agent("||",ag_cont,ag_ii))
        }
        }
        else
        { run_one( ag_ii ) match
        { case (false,_) =>
          { run_one( ag_i ) match
          { case (false,_) => (false,agent)
            case (true,bacht_ast_empty_agent()) => (true,ag_ii)
            case (true,ag_cont)
            => (true,bacht_ast_agent("||",ag_cont,ag_ii))
          }
          }
          case (true,bacht_ast_empty_agent())
          => (true,ag_i)
          case (true,ag_cont)
          => (true,bacht_ast_agent("||",ag_i,ag_cont))
        }
        }

      }


      case bacht_ast_agent("+",ag_i,ag_ii) =>
      {  var branch_choice = bacht_random_choice.nextInt(2)
        if (branch_choice == 0)
        { run_one( ag_i ) match
        { case (false,_) =>
          { run_one( ag_ii ) match
          { case (false,_) => (false,agent)
            case (true,bacht_ast_empty_agent())
            => (true,bacht_ast_empty_agent())
            case (true,ag_cont)
            => (true,ag_cont)
          }
          }
          case (true,bacht_ast_empty_agent())
          => (true,bacht_ast_empty_agent())
          case (true,ag_cont)
          => (true,ag_cont)
        }
        }
        else
        { run_one( ag_ii ) match
        { case (false,_) =>
          { run_one( ag_i ) match
          { case (false,_)
            => (false,agent)
            case (true,bacht_ast_empty_agent())
            => (true,bacht_ast_empty_agent())
            case (true,ag_cont)
            => (true,ag_cont)
          }
          }
          case (true,bacht_ast_empty_agent())
          => (true,bacht_ast_empty_agent())
          case (true,ag_cont)
          => (true,ag_cont)
        }
        }
      }
    }
  }

  /**
    * Permet de gérer la primitive wait, cette méthode consiste à comparer la valeur de "l'horloge"
    * avec les différents wait afin de savoir si nous devons attendre que l'horloge augmente ou si le temps voulu
    * est déjà atteint.
    */
  def run_time(agent: Expr):Expr = {
    agent match
    {
      case bacht_ast_wait(prim, time) => {
        if (time <= hour) {
          failure = false
          bacht_ast_empty_agent()
        }
        else {
          failure = false
          (bacht_ast_wait(prim, time))
        }
      }

      case bacht_ast_agent(";",ag_i,ag_ii) =>
      {
        run_time(ag_i) match {
          case bacht_ast_empty_agent() =>
            ag_ii
          case _ =>
            bacht_ast_agent(";", run_time(ag_i), ag_ii)
        }
      }

      case bacht_ast_agent("||",ag_i,ag_ii) =>
      {
        run_time(ag_i) match {
          case bacht_ast_empty_agent() =>
            run_time(ag_ii) match {
              case bacht_ast_empty_agent() =>
                bacht_ast_empty_agent()
              case _ =>
                run_time(ag_ii)
            }
          case _ =>
            run_time(ag_ii) match {
              case bacht_ast_empty_agent() =>
                run_time(ag_i)
              case _ =>
                bacht_ast_agent("||", run_time(ag_i), run_time(ag_ii))
            }
        }
      }

      case bacht_ast_agent("+",ag_i,ag_ii) =>
      {
        run_time(ag_i) match {
          case bacht_ast_empty_agent() =>
            run_time(ag_ii) match {
              case bacht_ast_empty_agent() =>
                bacht_ast_empty_agent()
              case _ =>
                run_time(ag_ii)
            }
          case _ =>
            run_time(ag_ii) match {
              case bacht_ast_empty_agent() =>
                run_time(ag_i)
              case _ =>
                bacht_ast_agent("+", run_time(ag_i), run_time(ag_ii))
            }
        }
      }

      case bacht_ast_primitive(prim, token) => {
        bacht_ast_primitive(prim, token)
      }

      case bacht_ast_empty_agent() => {
        bacht_ast_empty_agent()
      }
    }
  }

  /**
    * Ajout d'une seconde boucle permettant de gérer les primitives wait.
    */
  def bacht_exec_all(agent: Expr):Boolean = {

    var c_agent = agent
    while (c_agent != bacht_ast_empty_agent() && !failure) {
      while (c_agent != bacht_ast_empty_agent() && !failure) {
        failure = run_one(c_agent) match {
          case (false, _) => true
          case (true, new_agent) => {
            c_agent = new_agent
            false
          }
        }
        bb.print_store
        println("\n")
      }
      c_agent = run_time(c_agent)
      hour += 1
    }

    if (c_agent == bacht_ast_empty_agent()) {
      println("Success\n")
      true
    }
    else {
      println("failure\n")
      false
    }
  }

  def exec_primitive(prim:String,token:String):Boolean = {
    prim match
    { case "tell" => bb.tell(token)
    case "ask"  => bb.ask(token)
    case "get"  => bb.get(token)
    case "nask" => bb.nask(token)
    }
  }

}

/**
  * Cet object n'a pas été modifié.
  */
object ag extends BachTSimul(bb) {

  def apply(agent: String) {
    val agent_parsed = BachTSimulParser.parse_agent(agent)
    ag.bacht_exec_all(agent_parsed)
  }
  def eval(agent:String) { apply(agent) }
  def run(agent:String) { apply(agent) }

}