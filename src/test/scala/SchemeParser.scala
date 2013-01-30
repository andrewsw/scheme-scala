import org.scalatest.FlatSpec
import parser.SchemeParser._

class ParserSpec extends FlatSpec {

  behavior of "A Scheme Parser"

  it should "accept symbols" in {
    val symbols = "!#$%&|*+-/:<=>?@^_~"
    symbols.toCharArray.map(c => assertSuccess(parse(c.toString)))
  }

  it must "do something else" is (pending)

  def assertSuccess(result:ParseResult[Elem]) = {
    result match {
      case Success(_,_)     => assert(true)
      case NoSuccess(msg,_) => fail(msg)
    }
  }

}

