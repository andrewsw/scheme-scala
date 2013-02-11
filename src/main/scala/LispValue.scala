package parser

sealed abstract class LispVal

case class Atom(s:String)                          extends LispVal
case class LispList(l:List[LispVal])               extends LispVal
case class DottedList(l:List[LispVal], v: LispVal) extends LispVal
case class Number(i:Int)                           extends LispVal
case class LispString(s:String)                    extends LispVal
case class LispBool(b:Boolean)                     extends LispVal
