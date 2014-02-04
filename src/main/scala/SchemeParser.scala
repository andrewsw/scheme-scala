package parser

import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.CharSequenceReader
import syntax._

object SchemeParser extends Parsers {

  type Elem = Char

  val oneOf = (a:String) => {
    acceptIf(a.toList.contains(_))(_ + " not oneOf " + a)
  }

  val noneOf = (a:String) => {
    acceptIf(!a.toList.contains(_))(_ + " is one of " + a)
  }

  val symbol:Parser[Char] = oneOf("!#$%&|*+-/:<=>?@^_~")

  val spaces = rep1(elem(' '))

  val letter = acceptIf(_.isLetter)(_ + " not a letter")
  val digit = acceptIf(_.isDigit)(_ + " not a digit")

  val atom:Parser[LispVal] = for {
    first <- (letter | symbol)
    rest <- rep(letter | digit | symbol)
  } yield { (first + rest.mkString("")) match {
              case "#t" => LispBool(true)
              case "#f" => LispBool(false)
              case a    => Atom(a)
            }
  }

  val escapedChars = '\\' ~> oneOf("\\\"nrt") ^^ { x => println("here I am " + x); x match {
    case '\\' => x
    case '"'  => x
    case 'n'  => '\n'
    case 'r'  => '\r'
    case 't'  => '\t'}
  }

  val string = '"' ~> rep(escapedChars | noneOf ("\\\"")) <~ '"' ^^ { xs => LispString(xs.mkString("")) }

  val number = rep1(digit) ^^ { n => Number(Integer.parseInt(n.mkString(""))) }

  val parse = (s:String) => {
    val input = new CharSequenceReader(s)
    symbol(input)
  }

  val test = (s:String) => {
    parse(s) match {
      case Success(result, _) => println("parsed " + result)
      case e:NoSuccess => println("parsing failed on " + s)
    }
  }
}

