/* --------------------------------------------------------------------------

   The BachT parser

   AUTHOR : J.-M. Jacquet and D. Darquennes
   DATE   : March 2016

----------------------------------------------------------------------------*/

import scala.util.parsing.combinator._

/**
  * Le code suivant a été modifié de manière à faire reconnaitre la primitive delay au parser.
  */
class BachTParsers extends RegexParsers {

  def token 	: Parser[String] = ("[a-z][0-9a-zA-Z_]*").r ^^ {_.toString}
  def duration 	: Parser[Int] = ("[0-9]*").r ^^ {_.toInt}

  val opChoice  : Parser[String] = "+"
  val opPara    : Parser[String] = "||"
  val opSeq     : Parser[String] = ";"

  def primitive : Parser[Expr]   = "tell("~token~")" ^^ {
      case _ ~ vtoken ~ _  => bacht_ast_primitive("tell",vtoken) }  |
                                   "ask("~token~")" ^^ {
      case _ ~ vtoken ~ _  => bacht_ast_primitive("ask",vtoken) }   |
                                   "get("~token~")" ^^ {
      case _ ~ vtoken ~ _  => bacht_ast_primitive("get",vtoken) }   |
                                   "nask("~token~")" ^^ {
      case _ ~ vtoken ~ _  => bacht_ast_primitive("nask",vtoken) } |
                                   "delay("~duration~")" ^^ {
      case _ ~ vtoken ~ _  => bacht_ast_delay("delay",vtoken) }

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

/**
  * Nous avons donc créé un nouveau parse_delay en rapport avec la primitive que nous avons ajouté précédemment.
  */
object BachTSimulParser extends BachTParsers {

  def parse_primitive(prim: String) = parseAll(primitive,prim) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }

  def parse_agent(ag: String) = parseAll(agent,ag) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }

  def parse_delay(prim: String) = parseAll(primitive,prim) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }
}