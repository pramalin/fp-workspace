package work

object testio {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

	import fpinscala.iomonad.IO1._
	
	
	val echo = ReadLine.flatMap(PrintLine)    //> echo  : fpinscala.iomonad.IO1.IO[Unit] = fpinscala.iomonad.IO1$IO$$anon$8@2c
                                                  //| b4c3ab
	
}