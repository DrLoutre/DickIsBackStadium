/* -------------------------------------------------------------------------- 

   The BachT parser
   
   AUTHOR : J.-M. Jacquet and D. Darquennes
   DATE   : March 2016

----------------------------------------------------------------------------*/

import scala.util.parsing.combinator._
import scala.util.matching.Regex

/**
  * Cette class a été modifié de manière a pouvoir faire apparaître les valeurs de temps dans les différentes
  * primitives.
  */
class BachTParsers extends RegexParsers {

  def token 	: Parser[String] = ("[a-z][0-9a-zA-Z_]*").r ^^ {_.toString}
  def duration 	: Parser[Int] = ("[0-9]*").r ^^ {_.toInt}

  val opChoice  : Parser[String] = "+"
  val opPara    : Parser[String] = "||"
  val opSeq     : Parser[String] = ";"

  def primitive : Parser[Expr]   = "tell("~duration~")("~token~")" ^^ {
      case _ ~ vduration ~ _ ~ vtoken ~ _ => bacht_ast_primitive("tell",vduration,vtoken) }  |
                                   "ask("~duration~")("~token~")" ^^ {
      case _ ~ vduration ~ _ ~ vtoken ~ _  => bacht_ast_primitive("ask",vduration,vtoken) }   |
                                   "get("~duration~")("~token~")" ^^ {
      case _ ~ vduration ~ _ ~ vtoken ~ _  => bacht_ast_primitive("get",vduration,vtoken) }   |
                                   "nask("~duration~")("~token~")" ^^ {
      case _ ~ vduration ~ _ ~ vtoken ~ _ => bacht_ast_primitive("nask",vduration,vtoken) }

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
  * Cet object n'a pas été modifié.
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

}