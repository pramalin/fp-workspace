object reference_parser {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  import fpinscala.parsing.ReferenceTypes._
  import fpinscala.parsing.Parsers
  import fpinscala.parsing.Reference

  val R: Parsers[Parser] = Reference.asInstanceOf[Parsers[Parser]]
                                                  //> R  : fpinscala.parsing.Parsers[fpinscala.parsing.ReferenceTypes.Parser] = fp
                                                  //| inscala.parsing.Reference$@2d209079

  import R._

  val spaces = " ".many                           //> spaces  : fpinscala.parsing.ReferenceTypes.Parser[List[String]] = fpinscala.
                                                  //| parsing.Reference$$$Lambda$10/1277181601@27f674d

  // infix syntax
  run("abra" ** spaces ** "babba")("abra   babba")//> res0: Either[fpinscala.parsing.ParseError,((String, List[String]), String)] 
                                                  //| = Right(((abra,List( ,  ,  )),babba))
  run("abra" ** spaces ** "cadabra")("abra cAdabra")
                                                  //> res1: Either[fpinscala.parsing.ParseError,((String, List[String]), String)] 
                                                  //| = Left(1.7 'cadabra'
                                                  //| 
                                                  //| abra cAdabra
                                                  //|       ^)
  // regex example
  run(regex("[0-9]".r))("1234")                   //> res2: Either[fpinscala.parsing.ParseError,String] = Right(1)

  val p1 = scope("magic spell") {
    "abra" ** spaces ** "cadabra"
  }                                               //> p1  : fpinscala.parsing.ReferenceTypes.Parser[((String, List[String]), Strin
                                                  //| g)] = fpinscala.parsing.Reference$$$Lambda$50/1364614850@482f8f11

  val p2 = scope("gibberish") {
    "abba" ** spaces ** "babba"
  }                                               //> p2  : fpinscala.parsing.ReferenceTypes.Parser[((String, List[String]), Strin
                                                  //| g)] = fpinscala.parsing.Reference$$$Lambda$50/1364614850@4cc0edeb

  val p = p1 or p2                                //> p  : fpinscala.parsing.ReferenceTypes.Parser[((String, List[String]), String
                                                  //| )] = fpinscala.parsing.Reference$$$Lambda$55/1551870003@39aeed2f

  run(p)("abra cAdabra")                          //> res3: Either[fpinscala.parsing.ParseError,((String, List[String]), String)] 
                                                  //| = Left(1.1 magic spell
                                                  //| 1.7 'cadabra'
                                                  //| 
                                                  //| abra cAdabra
                                                  //|       ^)
  run(p)("abba baBba")                            //> res4: Either[fpinscala.parsing.ParseError,((String, List[String]), String)] 
                                                  //| = Left(1.1 magic spell
                                                  //| 1.3 'abra'
                                                  //| 
                                                  //| abba baBba
                                                  //|   ^)

  val p3 = (attempt("abra" ** spaces ** "abra") ** "cadabra") or
  ("abba" ** spaces ** "cadabra!")                //> p3  : fpinscala.parsing.ReferenceTypes.Parser[((java.io.Serializable, java.i
                                                  //| o.Serializable), String)] = fpinscala.parsing.Reference$$$Lambda$55/15518700
                                                  //| 03@2b98378d
  // throws execption
   run(p3) ("abba abra")                          //> res5: Either[fpinscala.parsing.ParseError,((java.io.Serializable, java.io.Se
                                                  //| rializable), String)] = Left(1.6 'cadabra!'
                                                  //| 
                                                  //| abba abra
                                                  //|      ^)

}