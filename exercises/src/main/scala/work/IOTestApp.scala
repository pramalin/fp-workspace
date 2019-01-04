
package work

import fpinscala.iomonad.IO1._
object IOTestApp extends App {
  
  
//  converter.run
  
//  IO1.factorialREPL.run

/*
  Here are some other example usages of IO:
  val echo = ReadLine.flatMap(PrintLine): An IO[Unit] that reads a line from the
  console and echoes it back.

  val readInt = ReadLine.map(_.toInt): An IO[Int] that parses an Int by reading a
  line from the console.

  val readInts = readInt ** readInt: An IO[(Int,Int)] that parses an (Int,Int)
  by reading two lines from the console.

  replicateM_(5)(converter): An IO[Unit] that will repeat converter 5 times,
  discarding the results (which are just Unit). We can replace here with any 3 converter IO
  action we wished to repeat 5 times (for instance, echo or readInts).

 [ Footnote Recall that replicateM(3)(fa) is the same as sequence(List(fa,fa,fa)).]
 
  replicateM(10)(ReadLine): An IO[List[String]] that will read 10 lines from the
  console and return the list of results.

 */
  
  
  val echo = ReadLine.flatMap(PrintLine)
//  echo.run
  	
  val readInt = ReadLine.map(_.toInt)
//  println(s"I got:  ${readInt.run}")
  
  val readInts = readInt ** readInt
  println(s"I got:  ${readInts.run}")

//  import IO._
//  replicateM_(5)(converter).run
//  println(s"10 repeats: ${replicateM(10)(ReadLine).run}")

}