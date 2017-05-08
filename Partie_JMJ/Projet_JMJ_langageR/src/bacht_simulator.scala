/* --------------------------------------------------------------------------

   BachT simulator


   AUTHOR : J.-M. Jacquet and D. Darquennes
   DATE   : March 2016

----------------------------------------------------------------------------*/

import scala.util.Random
import language.postfixOps

/**
  * cette class a été modifié afin de faire apparaître la notion de parallèle pour gérer l'avancement du temps car nous n'utilisons qu'une seule boucle dans l'exécution.
  */
class BachTSimul(var bb: BachTStore) {

  var para = false
  var failure = false
  val bacht_random_choice = new Random()

  /**
    * Cette méthode permet de gérer la durée de vie des différentes instructions. Une instruction n'est conservé que si sa durée de vie est supérieur à 0.
    * Si elle n'a pas pu être executé, nous renvoyons une failure pour arrêter l'exécution du programme.
    */
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

  /**
    * Cette méthode permet de gérer la durée de vie des différentes instructions. Une instruction n'est conservé que si sa durée de vie est supérieur à 0.
    * Si elle n'a pas pu être executé, nous renvoyons une failure pour arrêter l'exécution du programme.
    */
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

  /**
    * Nous n'utilisons qu'une seule boucle mais la gestion du temps a pû gérer par l'utilisation d'un 'if'.
    */
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