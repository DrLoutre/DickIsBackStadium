class Extension
case class tell(token: String, begin: Int, end: Int) extends Extension
case class ask(token: String, begin: Int, end: Int) extends Extension
case class nask(token: String, begin: Int, end: Int) extends Extension
case class get(token: String, begin: Int, end: Int) extends Extension
case class &&(agenti: Extension, agentii: Extension) extends Extension
case class |(agenti: Extension, agentii: Extension) extends Extension
case class |||(agenti: Extension, agentii: Extension) extends Extension

object api {

  def apply(agent: Extension) {
    val agent_parsed = toStringAgent(agent)
  }

  def eval(agent: Extension) {
    apply(agent)
  }

  def run(agent: Extension) {
    apply(agent)
  }

  def toStringAgent(agent: Extension): String = {
    agent match {
      case tell(token, begin, end) => {
        "tell(" + begin + ")("+ end + ")(" + token + ")"
      }
      case ask(token, begin, end) => {
        "ask(" + begin + ")("+ end + ")(" + token + ")"
      }
      case nask(token, begin, end) => {
        "nask(" + begin + ")("+ end + ")(" + token + ")"
      }
      case get(token, begin, end) => {
        "get(" + begin + ")("+ end + ")(" + token + ")"
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