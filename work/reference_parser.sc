object reference_parser {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  import fpinscala.parsing.ReferenceTypes._
	import fpinscala.parsing.Parsers
	import fpinscala.parsing.Reference
	
  val R: Parsers[Parser] = Reference.asInstanceOf[Parsers[Parser]]
                                                  //> R  : fpinscala.parsing.Parsers[fpinscala.parsing.ReferenceTypes.Parser] = fp
                                                  //| inscala.parsing.Reference$@2d209079

  import R._

	val spaces = " ".many                     //> spaces  : fpinscala.parsing.ReferenceTypes.Parser[List[String]] = fpinscala.
                                                  //| parsing.Reference$$$Lambda$10/1277181601@27f674d

   run("abra" ** spaces ** "babba")("abra   babba")
                                                  //> res0: Either[fpinscala.parsing.ParseError,((String, List[String]), String)] 
                                                  //| = Right(((abra,List( ,  ,  )),babba))
}