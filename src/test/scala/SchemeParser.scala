import org.scalatest.FlatSpec
import org.scalatest.Assertions

import scala.util.parsing.input.CharSequenceReader

import parser.SchemeParser._
import syntax._

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

  val runParser = (p:Parser[_], s:String) => {
    phrase(p)(new CharSequenceReader(s))
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
    atoms.map(a => assertSuccess(runParser(atom, a)))
  }

  it should "accept an atom and return a LispVal Atom object" in {
    val result = runParser(atom, "a7om5")

    result match {
      case Success(Atom(a),_) => assert(a == "a7om5")
      case e                  => fail(e + ", did not parse an Atom")
    }
  }

  it should "accept #t and #f but return LispBools instead of Atoms" in {
    val trueResult = runParser(atom, "#t")
    val falseResult = runParser(atom, "#f")

    trueResult match {
      case Success(LispBool(r), _) => assert(r)
      case e                       => fail(e + ", did not parse #t as true")
    }

    falseResult match {
      case Success(LispBool(r), _) => assert(!r)
      case e                       => fail(e + ", did not parse #f as false")
    }


  }

  behavior of "The SchemeParser string parser"

  it should "accept a quoted string and return a LispVal String object" in {
    val result = runParser(string, "\"test string\"")

    result match {
      case Success(LispString(s), _) => assert(s == "test string")
      case e                         => fail (e + ", did not parse a String")
    }
  }

  it should "accept a string that contains an escaped quote" in {
    val result = runParser(string, "\"test \\\"string\\\"\"")

    result match {
      case Success(LispString(s), _) => assert(s == "test \"string\"")
      case e                         => fail (e + ", did not accept quotes in a String")
    }
  }

  it should "accept a string that contains an escaped newline" in {
    val result = runParser(string, "\"test\nstring\"")

    result match {
      case Success(LispString(s), _) => assert(s == "test\nstring")
      case e                         => fail(e +  ", did not accept newline in a String")
    }
  }


  it should "accept a string that contains an escaped tab" in {
    val result = runParser(string, "\"test\tstring\"")

    result match {
      case Success(LispString(s), _) => assert(s == "test\tstring")
      case e                         => fail(e +  ", did not accept tab in a String")
    }
  }

  it should "accept a string that contains an escaped carriage return" in {
    val result = runParser(string, "\"test\rstring\"")

    result match {
      case Success(LispString(s), _) => assert(s == "test\rstring")
      case e                         => fail(e +  ", did not accept carriage return in a String")
    }
  }

  it should "accept a string that contains an escaped backslash" in {
    val result = runParser(string, "\"test\\\\string\"")

    result match {
      case Success(LispString(s), _) => assert(s == "test\\string")
      case e                         => fail(e +  ", did not accept backslash in a String")
    }
  }

  behavior of "The SchemeParser number parser"

  it should "accept integers and return a matching LispVal Number object" in {
    runParser(number, "123") match {
      case Success(Number(n), _) => assert(n == 123)
      case e                     => fail (e + ", did not parse a Number")
    }
  }

  it should "reject integers followed by non-integers" in {
    runParser(number, "12a") match {
      case Success(Number(n), _) => fail ("accepted 12a but should have rejected")
      case e                     => assert(true)
    }
  }

  it should "reject integers preceded by non-integers" in {
    runParser(number, "a12") match {
      case Success(Number(n), _) => fail ("accepted a12 but should have rejected")
      case e                     => assert(true)
    }
  }

}
