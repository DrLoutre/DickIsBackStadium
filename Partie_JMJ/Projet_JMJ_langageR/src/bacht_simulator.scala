/* -------------------------------------------------------------------------- 

   BachT simulator


   AUTHOR : J.-M. Jacquet and D. Darquennes
   DATE   : March 2016

----------------------------------------------------------------------------*/

import scala.util.Random
import language.postfixOps

class BachTSimul(var bb: BachTStore) {

  val bacht_random_choice = new Random()

  def run_one(agent: Expr):(Boolean,Expr) = {

    agent match {
      case bacht_ast_primitive(prim,time,token) =>
      {  if (exec_primitive(prim,time,token)) { (true,bacht_ast_empty_agent()) }
      else { (false,agent) }
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

  def test_value(value: Int):Int = {
    if(value - 1 >= 0) {value - 1}
    else {0}
  }

  def run_time(agent: Expr):Expr = {
    agent match {
      case bacht_ast_primitive(prim, time, token) =>
      {
        prim match {
          case "tell" => {
            if(time == 0) bacht_ast_empty_agent()
            else bacht_ast_primitive(prim, test_value(time), token)
          }
          case "ask"  => {
            if(time == 0) bacht_ast_empty_agent()
            else bacht_ast_primitive(prim, test_value(time), token)
          }
          case "get"  => {
            if(time == 0) bacht_ast_empty_agent()
            else bacht_ast_primitive(prim, test_value(time), token)
          }
          case "nask" => {
            if(time == 0) bacht_ast_empty_agent()
            else bacht_ast_primitive(prim, test_value(time), token)
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
    while (c_agent != bacht_ast_empty_agent()) {
      var failure = false
      while ( c_agent != bacht_ast_empty_agent() && !failure ) {
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
    prim match
    {
      case "tell" =>
        if(time != 0) { bb.tell(token) }
        else false
      case "ask"  =>
        if(time != 0) { bb.ask(token) }
        else false
      case "get"  =>
        if(time == 0) { bb.get(token) }
        else false
      case "nask" =>
        if(time == 0) { bb.nask(token) }
        else false
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