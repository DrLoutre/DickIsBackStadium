/* --------------------------------------------------------------------------

   Complete code for the command-line interpreter


   AUTHOR : J.-M. Jacquet and D. Darquennes
   DATE   : March 2016

----------------------------------------------------------------------------*/

class Expr

case class bacht_ast_empty_agent() extends Expr
case class bacht_ast_primitive(primitive: String, time: Int, token: String) extends Expr
case class bacht_ast_agent(op: String, agenti: Expr, agentii: Expr) extends Expr
import scala.util.parsing.combinator._
import scala.util.matching.Regex

class BachTParsers extends RegexParsers {

  def token 	: Parser[String] = ("[a-z][0-9a-zA-Z_]*").r ^^ {_.toString}
  def duration 	: Parser[Int] = ("[0-9]*").r ^^ {_.toInt}

  val opChoice  : Parser[String] = "+"
  val opPara    : Parser[String] = "||"
  val opSeq     : Parser[String] = ";"

  def primitive : Parser[Expr]   = "tell("~duration~")("~token~")" ^^ {
    case _ ~ vduration ~ _ ~ vtoken ~ _ => bacht_ast_primitive("tell",vduration,vtoken) }  |
    "ask("~duration~")("~token~")" ^^ {
      case _ ~ vduration ~ _ ~ vtoken ~ _  => bacht_ast_primitive("ask",vduration,vtoken) }   |
    "get("~duration~")("~token~")" ^^ {
      case _ ~ vduration ~ _ ~ vtoken ~ _  => bacht_ast_primitive("get",vduration,vtoken) }   |
    "nask("~duration~")("~token~")" ^^ {
      case _ ~ vduration ~ _ ~ vtoken ~ _ => bacht_ast_primitive("nask",vduration,vtoken) }

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

object BachTSimulParser extends BachTParsers {

  def parse_primitive(prim: String) = parseAll(primitive,prim) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }

  def parse_agent(ag: String) = parseAll(agent,ag) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }

}

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
import scala.util.Random
import language.postfixOps

class BachTSimul(var bb: BachTStore) {

  var para = false
  var failure = false
  val bacht_random_choice = new Random()

  def run_one(agent: Expr):(Boolean,Expr) = {

    agent match {
      case bacht_ast_primitive(prim,time,token) => {
        if (exec_primitive(prim, time, token)) {
          (true, bacht_ast_empty_agent())
        } else {
          (false, agent)
        }
      }

      case bacht_ast_agent(";",ag_i,ag_ii) =>
      {  run_one(ag_i) match
      { case (false,_) => (false,agent)
        case (true,bacht_ast_empty_agent()) => (true,ag_ii)
        case (true,ag_cont) => (true,bacht_ast_agent(";",ag_cont,ag_ii))
      }
      }

      case bacht_ast_agent("||",ag_i,ag_ii) =>
      {
        para = true;
        var branch_choice = bacht_random_choice.nextInt(2)
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

  def test_value(value: Int):Int = {
    if(value - 1 >= 0) {value - 1}
    else {0}
  }

  def run_time(agent: Expr):Expr = {
    agent match {
      case bacht_ast_primitive(prim, time, token) =>
      {
        prim match {
          case "tell" => {bacht_ast_primitive(prim, time, token)}
          case "ask"  => {
            if(time == 0) {
              failure = true
              bacht_ast_primitive(prim, time, token)
            }
            else {
              failure = false
              bacht_ast_primitive(prim, test_value(time), token)
            }
          }
          case "get"  => {
            if(time == 0) {
              failure = true
              bacht_ast_primitive(prim, time, token)
            }
            else {
              failure = false
              bacht_ast_primitive(prim, test_value(time), token)
            }
          }
          case "nask" => {
            if(time == 0) {
              failure = true
              bacht_ast_primitive(prim, time, token)
            }
            else {
              failure = false
              bacht_ast_primitive(prim, test_value(time), token)
            }
          }
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

      case bacht_ast_empty_agent() => {
        bacht_ast_empty_agent()
      }
    }
  }

  def bacht_exec_all(agent: Expr):Boolean = {

    var c_agent = agent
    while ( c_agent != bacht_ast_empty_agent() && !failure ) {
      failure = run_one(c_agent) match {
        case (false, _) => true
        case (true, new_agent) => {
          c_agent = new_agent
          false
        }
      }
      bb.print_store
      if (!para) {
        bb.run_time()
        c_agent = run_time(c_agent)
      }
      para = false
      println("\n")
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

  def exec_primitive(prim:String,time:Int,token:String):Boolean = {
    prim match {
      case "tell" => {
        bb.tell(token, time)
      }
      case "ask" => {
        bb.ask(token, time)
      }
      case "get" => {
        bb.get(token, time)
      }
      case "nask" => {
        bb.nask(token, time)
      }
    }
  }

}

object ag extends BachTSimul(bb) {

  def apply(agent: String) {
    val agent_parsed = BachTSimulParser.parse_agent(agent)
    ag.bacht_exec_all(agent_parsed)
  }
  def eval(agent:String) { apply(agent) }
  def run(agent:String) { apply(agent) }

}