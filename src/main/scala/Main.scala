import parser.SchemeParser

object Main {

    def main(args : Array[String]) : Unit = {
      SchemeParser.test(args(0))
    }

}
