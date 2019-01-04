package exercise

object StringParserApp extends App {

  trait Parsers[Parser[+_]] { self => // so inner classes may call methods of trait

    case class ParserOps[A](p: Parser[A]) {

    }

    object Laws {
    }
  }

  case class Location(input: String, offset: Int = 0) {

    lazy val line = input.slice(0, offset + 1).count(_ == '\n') + 1
    lazy val col = input.slice(0, offset + 1).reverse.indexOf('\n')

    def toError(msg: String): ParseError =
      ParseError(List((this, msg)))

    def advanceBy(n: Int) = copy(offset = offset + n)

    /* Returns the line corresponding to this location */
    def currentLine: String =
      if (input.length > 1) input.lines.drop(line - 1).next
      else ""
  }

  case class ParseError(
    stack:         List[(Location, String)] = List(),
    otherFailures: List[ParseError]         = List()) {
  }

  type Parser[+A] = String => Either[ParseError, A]

  object MyParsers extends Parsers[Parser] {

    def run[A](p: Parser[A])(s: String): Either[ParseError, A] = {
      val s0 = s
      p(s0)
    }

    // implementations of primitives go here
    def string(s: String): Parser[String] =
      (input: String) =>
        if (input.startsWith(s))
          Right(s)
        else
          Left(Location(input).toError("Expected: " + s))

  }

	println(MyParsers.run(MyParsers.string("c"))("ab"))
	
}