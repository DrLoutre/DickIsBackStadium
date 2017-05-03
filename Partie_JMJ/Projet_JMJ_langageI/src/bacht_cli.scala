/* --------------------------------------------------------------------------

   Complete code for the command-line interpreter


   AUTHOR : J.-M. Jacquet and D. Darquennes
   DATE   : March 2016

----------------------------------------------------------------------------*/

class Expr
case class bacht_ast_empty_agent() extends Expr
case class bacht_ast_primitive(primitive: String, begin: Int, end: Int, token: String) extends Expr
case class bacht_ast_agent(op: String, agenti: Expr, agentii: Expr) extends Expr
import scala.util.parsing.combinator._
import scala.util.matching.Regex

class BachTParsers extends RegexParsers {

  def token 	: Parser[String] = ("[a-z][0-9a-zA-Z_]*").r ^^ {_.toString}
  def duration 	: Parser[Int] = ("[0-9]*").r ^^ {_.toInt}

  val opChoice  : Parser[String] = "+"
  val opPara    : Parser[String] = "||"
  val opSeq     : Parser[String] = ";"

  def primitive : Parser[Expr]   = "tell("~duration~")("~duration~")("~token~")" ^^ {
    case _ ~ begin ~ _ ~ end ~ _ ~ vtoken ~ _ => bacht_ast_primitive("tell", begin, end, vtoken) }  |
    "ask("~duration~")("~duration~")("~token~")" ^^ {
      case _ ~ begin ~ _ ~ end ~ _ ~ vtoken ~ _ => bacht_ast_primitive("ask", begin, end, vtoken) }  |
    "get("~duration~")("~duration~")("~token~")" ^^ {
      case _ ~ begin ~ _ ~ end ~ _ ~ vtoken ~ _ => bacht_ast_primitive("get", begin, end, vtoken) }  |
    "nask("~duration~")("~duration~")("~token~")" ^^ {
      case _ ~ begin ~ _ ~ end ~ _ ~ vtoken ~ _ => bacht_ast_primitive("nask", begin, end, vtoken) }

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

  var theStore = Map[(String, (Int, Int)), Int]()
  var hour = 0;

  def give_time():Int = {
    hour
  }

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
import scala.util.Random
import language.postfixOps

class BachTSimul(var bb: BachTStore) {

  var block = false
  var para = false
  var failure = false
  val bacht_random_choice = new Random()

  def run_one(agent: Expr):(Boolean,Expr) = {

    agent match {
      case bacht_ast_primitive(prim,begin, end,token) => {
        if (exec_primitive(prim,begin, end,token)) {
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

  def run_time(agent: Expr):Expr = {
    agent match {
      case bacht_ast_primitive(prim, begin, end,token) =>
      {
        prim match {
          case "tell" => {
            if(begin <= bb.give_time()) {
              block = false
            }
            if(end < bb.give_time()) {
              bacht_ast_primitive(prim, begin, end,token)
            }
            else {
              failure = false
              bacht_ast_primitive(prim, begin, end,token)
            }
          }
          case "ask"  => {
            if(begin <= bb.give_time()) {
              block = false
            }
            if(end < bb.give_time()) {
              bacht_ast_primitive(prim, begin, end,token)
            }
            else {
              failure = false;
              bacht_ast_primitive(prim, begin, end,token)
            }
          }
          case "get"  => {
            if(begin <= bb.give_time()) {
              block = false
            }
            if(end < bb.give_time()) {
              bacht_ast_primitive(prim, begin, end,token)
            }
            else {
              failure = false
              bacht_ast_primitive(prim, begin, end,token)
            }
          }
          case "nask" => {
            if(begin <= bb.give_time()) {
              block = false
            }
            if(end < bb.give_time()) {
              bacht_ast_primitive(prim, begin, end,token)
            }
            else {
              failure = false
              bacht_ast_primitive(prim, begin, end,token)
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
      run_time(c_agent)
      if (!para || block) {
        bb.run_time()
      }
      para = false
      block = true
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

  def exec_primitive(prim:String,begin:Int,end:Int,token:String):Boolean = {
    prim match {
      case "tell" => {
        bb.tell(token,begin,end)
      }
      case "ask" => {
        bb.ask(token,begin,end)
      }
      case "get" => {
        bb.get(token,begin, end)
      }
      case "nask" => {
        bb.nask(token,begin, end)
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