import org.scalatest.FlatSpec
import org.scalatest.Assertions

import scala.util.parsing.input.CharSequenceReader

import parser.SchemeParser._

trait ParserAssertions extends Assertions {

  def assertSuccess(result:ParseResult[_]) = {
    result match {
      case Success(_,_)     => assert(true)
      case NoSuccess(msg,_) => fail(msg)
    }
  }

  def assertNoSuccess(result:ParseResult[_]) = {
    result match {
      case Success(a,_)   => fail("parser accepted: " + a)
      case NoSuccess(_,_) => assert(true)
    }
  }
}

class ParserSpec extends FlatSpec with ParserAssertions{

  behavior of "The SchemeParser symbol parser"

  it should "accept a single symbol" in {
    val symbols = "!#$%&|*+-/:<=>?@^_~".toCharArray
    symbols.map(c => assertSuccess(symbol(new CharSequenceReader(c.toString))))
  }

  it should "reject things that aren't symbols" in {
    val failing = new CharSequenceReader("hello")
    assertNoSuccess(symbol(failing))
  }

  behavior of "The SchemeParser spaces parser"

  it should "accept any number of leading spaces" in {
    val one_space = new CharSequenceReader(" abc")
    val many_spaces = new CharSequenceReader("      123")
    assertSuccess(spaces(one_space))
    assertSuccess(spaces(many_spaces))
  }

  it should "reject strings that don't start with a space" in {
    val no_space = new CharSequenceReader("hello")
    assertNoSuccess(spaces(no_space))
  }

}

