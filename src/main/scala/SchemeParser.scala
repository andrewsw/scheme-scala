package parser

import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.CharSequenceReader
import scala.collection.immutable.StringOps
import scala.Nothing

object SchemeParser extends Parsers {

  type Elem = Char

  val oneOf = (a:String) => {
    val accepted = new StringOps(a)
    acceptIf(accepted.toList.contains(_))(_ + " not oneOf " + a)
  }

  val symbol:Parser[Char] = oneOf("!#$%&|*+-/:<=>?@^_~")

  val spaces = rep1(elem(' '))

  val letter = acceptIf(_.isLetter)(_ + " not a letter")
  val digit = acceptIf(_.isDigit)(_ + " not a digit")

  val atom = for {
    first <- (letter | symbol)
    rest <- rep(letter | digit | symbol)
  } yield { Atom(first + rest.mkString("")) }

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

