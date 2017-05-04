class Extension
case class tell(token: String) extends Extension
case class ask(token: String) extends Extension
case class nask(token: String) extends Extension
case class get(token: String) extends Extension
case class delay(time: Int) extends Extension
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
        case delay(time) => {
          "delay(" + time + ")"
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