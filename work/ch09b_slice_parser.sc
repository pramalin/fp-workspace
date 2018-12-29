object slice_parser {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  import fpinscala.parsing.SliceableTypes._
	import fpinscala.parsing.Parsers
	import fpinscala.parsing.Sliceable
	
  val P: Parsers[Parser] = Sliceable.asInstanceOf[Parsers[Parser]]
                                                  //> P  : fpinscala.parsing.Parsers[fpinscala.parsing.SliceableTypes.Parser] = fp
                                                  //| inscala.parsing.Sliceable$@2d209079

  import P._

	val spaces = " ".many                     //> spaces  : fpinscala.parsing.SliceableTypes.Parser[List[String]] = fpinscala.
                                                  //| parsing.Sliceable$$$Lambda$10/1277181601@27f674d

   run("abra" ** spaces ** "babba")("abra   babba")
                                                  //> res0: Either[fpinscala.parsing.ParseError,((String, List[String]), String)] 
                                                  //| = Right(((abra,List( ,  ,  )),babba))

   run("abra" ** spaces ** "cadabra")("abra cAdabra")
                                                  //> res1: Either[fpinscala.parsing.ParseError,((String, List[String]), String)] 
                                                  //| = Left(1.7 'cadabra'
                                                  //| 
                                                  //| abra cAdabra
                                                  //|       ^)

}