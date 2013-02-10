import org.scalatest.FlatSpec
import org.scalatest.Assertions

import scala.util.parsing.input.CharSequenceReader

import parser.SchemeParser._

trait ParserAssertions extends Assertions {

  val assertSuccess = (result:ParseResult[_]) => {
    result match {
      case Success(_,_)     => assert(true)
      case NoSuccess(msg,_) => fail(msg)
    }
  }

  val assertNoSuccess = (result:ParseResult[_]) => {
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
    val oneSpace = new CharSequenceReader(" abc")
    val manySpaces = new CharSequenceReader("      123")
    assertSuccess(spaces(oneSpace))
    assertSuccess(spaces(manySpaces))
  }

  it should "reject strings that don't start with a space" in {
    val noSpace = new CharSequenceReader("hello")
    assertNoSuccess(spaces(noSpace))
  }

  behavior of "The SchemeParser atom parser"

  it should "accept Lisp Atoms" in {
    val atoms = List("atom", "a70m5", "*a/t%o!m*")
    atoms.map(a => assertSuccess(phrase(parseAtom)(new CharSequenceReader(a))))
  }

  it should "return a LispVal Atom object" is (pending)
}
