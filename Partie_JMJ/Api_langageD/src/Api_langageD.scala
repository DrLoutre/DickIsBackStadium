import scala.util.parsing.combinator.RegexParsers

class BachTParsersAPI extends RegexParsers {

  def token 	: Parser[String] = ("[a-z][0-9a-zA-Z_]*").r ^^ {_.toString}
  def duration 	: Parser[Int] = ("[0-9]*").r ^^ {_.toInt}

  val opChoice  : Parser[String] = "|||"
  val opPara    : Parser[String] = "|"
  val opSeq     : Parser[String] = "//"

  def primitive : Parser[String]   = "tell("~token~")" ^^ {
    case _ ~ vtoken ~ _  => "tell(" + vtoken + ")" }  |
    "ask("~token~")" ^^ {
      case _ ~ vtoken ~ _  => "ask(" + vtoken + ")" }   |
    "get("~token~")" ^^ {
      case _ ~ vtoken ~ _  => "get(" + vtoken + ")" }   |
    "nask("~token~")" ^^ {
      case _ ~ vtoken ~ _  => "nask(" + vtoken + ")" } |
    "delay("~duration~")" ^^ {
      case _ ~ vtoken ~ _  => "delay(" + vtoken + ")" }
  def agent = compositionChoice

  def compositionChoice : Parser[String] = compositionPara~rep(opChoice~compositionChoice) ^^ {
    case ag ~ List() => ag
    case agi ~ List(op~agii)  => agi + "+" + agii }

  def compositionPara : Parser[String] = compositionSeq~rep(opPara~compositionPara) ^^ {
    case ag ~ List() => ag
    case agi ~ List(op~agii)  => agi + "||" + agii }

  def compositionSeq : Parser[String] = simpleAgent~rep(opSeq~compositionSeq) ^^ {
    case ag ~ List() => ag
    case agi ~ List(op~agii)  => agi + ";" + agii }

  def simpleAgent : Parser[String] = primitive | parenthesizedAgent

  def parenthesizedAgent : Parser[String] = "("~>agent<~")"

}

object BachTSimulParserAPI extends BachTParsersAPI {

  def parse_primitive(prim: String) = parseAll(primitive,prim) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }

  def parse_agent(ag: String) = parseAll(agent,ag) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }

}

object api {

  def apply(agent: String) {
    val agent_parsed = BachTSimulParserAPI.parse_agent(agent)
    //ag run(agent_parsed) lien vers BachtT
  }

  def eval(agent:String) { apply(agent) }
  def run(agent:String) { apply(agent) }
}