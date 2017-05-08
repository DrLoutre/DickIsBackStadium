class Extension
case class tell(token: String, time: Int) extends Extension
case class ask(token: String, time: Int) extends Extension
case class nask(token: String, time: Int) extends Extension
case class get(token: String, time: Int) extends Extension
case class &&(agenti: Extension, agentii: Extension) extends Extension
case class |(agenti: Extension, agentii: Extension) extends Extension
case class |||(agenti: Extension, agentii: Extension) extends Extension

object api {

  def apply(agent: Extension) {
    val agent_parsed = toStringAgent(agent)
    //ag run(agent_parsed) //lien vers bachtT
  }

  def eval(agent: Extension) {
    apply(agent)
  }

  def run(agent: Extension) {
    apply(agent)
  }

  /**
    * Permet de transformer un instruction écrite en scala en un String.
    * Ex : &&(tell(3)("t"), get(4)("t")) => tell(3)(t);(4)get(t)
    */
  def toStringAgent(agent: Extension): String = {
    agent match {
      case tell(token, time) => {
        "tell("+ time + ")(" + token + ")"
      }
      case ask(token, time) => {
        "ask("+ time + ")(" + token + ")"
      }
      case nask(token, time) => {
        "nask("+ time + ")(" + token + ")"
      }
      case get(token, time) => {
        "get("+ time + ")(" + token + ")"
      }
      case |||(ag_1, ag_2) => {
        "(" + toStringAgent(ag_1) + ")+(" + toStringAgent(ag_2) + ")"
      }
      case &&(ag_1, ag_2) => {
        toStringAgent(ag_1) + ";" + toStringAgent(ag_2)
      }
      case |(ag_1, ag_2) => {
        "(" + toStringAgent(ag_1) + ")||(" + toStringAgent(ag_2) + ")"
      }
    }
  }
}