package exercise

object IoApp extends App {
  import fpinscala.iomonad.IO1._
  //
  // 13.2.1 Handling input effects
  //
  
  
  // Fahrenheit to Celsius test
  converter.run

  // echo test
  println("Type something to echo.")
  echo.run
  
  // readInt test
  println("type an Integer")
  println(s"read one Int: ${readInt.run}")
  
  // readInt test
  println("type two Integers")
  println(s"read two Int: ${readInts.run}")
}