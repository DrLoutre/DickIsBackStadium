class Extension
case class tell(token: String) extends Extension
case class ask(token: String) extends Extension
case class nask(token: String) extends Extension
case class get(token: String) extends Extension
case class waiting(time: Int) extends Extension
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
    * Ex : &&(tell("t"), get("t")) => tell(t);get(t)
    */
  def toStringAgent(agent: Extension): String = {
    agent match {
      case tell(token) => {
        "tell(" + token + ")"
      }
      case ask(token) => {
        "ask(" + token + ")"
      }
      case nask(token) => {
        "nask(" + token + ")"
      }
      case get(token) => {
        "get(" + token + ")"
      }
      case waiting(time) => {
        "wait(" + time + ")"
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