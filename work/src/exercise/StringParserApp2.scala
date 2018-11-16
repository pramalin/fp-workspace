package exercise

object MyParser2 {
   trait Parsers[Parser[+_]] { self => // so inner classes may call methods of trait
  
    def succeed[A](a: A): Parser[A]
  
  	def flatMap[A,B](p: Parser[A])(f: A => Parser[B]): Parser[B]
    
  	def map[A,B](a: Parser[A])(f: A => B): Parser[B] =
    	flatMap(a)(f andThen succeed)
    
	  /*
	  These can be implemented using a for-comprehension, which delegates to the `flatMap` and `map` implementations we've provided on `ParserOps`, or they can be implemented in terms of these functions directly.
	  */
	  def product[A,B](p: Parser[A], p2: => Parser[B]): Parser[(A,B)] =
	    flatMap(p)(a => map(p2)(b => (a,b)))

    case class ParserOps[A](p: Parser[A]) {
      def **[B](p2: => Parser[B]): Parser[(A, B)] =
        self.product(p, p2)
      def product[B](p2: => Parser[B]): Parser[(A, B)] =
        self.product(p, p2)
    
      def flatMap[B](f: A => Parser[B]): Parser[B] =
        self.flatMap(p)(f)
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

//  type Parser[+A] = String => Either[ParseError, A]
  type Parser[+A] = Location => Result[A]
  
  trait Result[+A]
  case class Success[+A](get: A, charsConsumed: Int) extends Result[A]
  case class Failure(get: ParseError) extends Result[Nothing]

  /** Returns -1 if s1.startsWith(s2), otherwise returns the
    * first index where the two strings differed. If s2 is
    * longer than s1, returns s1.length. */
  def firstNonmatchingIndex(s1: String, s2: String, offset: Int): Int = {
    var i = 0
    while (i < s1.length && i < s2.length) {
      if (s1.charAt(i+offset) != s2.charAt(i)) return i
      i += 1
    }
    if (s1.length-offset >= s2.length) -1
    else s1.length-offset
  }
  
  object MyParsers extends Parsers[Parser] {

    def run[A](p: Parser[A])(s: String): Either[ParseError, A] = {
      val s0 = s
      p(Location(s0)) match {
        case Failure(e) => Left(e)
        case Success(a,_) => Right(a)
      }
    }

      // consume no characters and succeed with the given value
    def succeed[A](a: A): Parser[A] = s => Success(a, 0)
  
    def flatMap[A,B](f: Parser[A])(g: A => Parser[B]): Parser[B] =
      s => f(s) match {
        case Success(a,n) => g(a)(s.advanceBy(n))
        case f@Failure(_) => f
    }
    
      // implementations of primitives go here
    def string(w: String): Parser[String] = {
      val msg = "'" + w + "'"
      s => {
        val i = firstNonmatchingIndex(s.input, w, s.offset)
        if (i == -1) // they matched
          Success(w, w.length)
        else
          Failure(s.advanceBy(i).toError(msg))
      }
    }
  
  }
}


import language.higherKinds

import exercise.MyParser2.MyParsers._
import exercise.MyParser2.MyParsers.ParserOps._

object StringParserApp2 extends App {

	println(run(product(string("abra"), string ("cadabra")))("abra cAdabra"))
}