package parser

import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.CharSequenceReader
import scala.collection.immutable.StringOps

object SchemeParser extends Parsers {

  type Elem = Char

  def oneOf(a:String) = {
    val accepted = new StringOps(a)
    acceptIf(c => (accepted.toList.contains(c)))(c => c + " not oneOf " + a)
  }

  def symbol:Parser[Char] = oneOf("!#$%&|*+-/:<=>?@^_~")

  def spaces = rep1(elem(' '))

  def parse (s:String) = {
    val input = new CharSequenceReader(s)
    symbol(input)
  }

  def test (s:String) {
    parse(s) match {
      case Success(result, _) => println("parsed " + result)
      case e:NoSuccess => println("parsing failed on " + s)
    }
  }
}

