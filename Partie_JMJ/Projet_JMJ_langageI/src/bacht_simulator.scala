/* -------------------------------------------------------------------------- 

   BachT simulator


   AUTHOR : J.-M. Jacquet and D. Darquennes
   DATE   : March 2016

----------------------------------------------------------------------------*/

import scala.util.Random
import language.postfixOps

/**
  * cette class a été modifié afin de faire apparaître la notion de parallèle pour gérer l'avancement du temps car nous n'utilisons qu'une seule boucle dans l'exécution.
  * Ainsi que la notion de bloquer, c'est-à-dire que l'ensemble des instructions qui peuvent être exécuté ont un temps de commencement supérieur au temps actuel.
  */
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

  /**
    * Cette méthode permet de gérer l'intervalle de validité des différentes instructions. Une instruction n'est conservé que si son temps de fin de validité est supérieur au temps actuel.
    * Si elle n'a pas pu être executé, nous renvoyons une failure pour arrêter l'exécution du programme.
    */
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