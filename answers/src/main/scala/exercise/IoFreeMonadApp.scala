package exercise

object IoFreeMonadApp extends App {
  //
  // 13.4.2 A monad that supports only console I/O
  //
  import fpinscala.iomonad.IO3._
  import fpinscala.iomonad.IO3.Console._

  /*
   * Note that these aren’t Scala’s standard readLine and println, but
   * the monadic methods we defined earlier.
   */
  val f1: Free[Console, Option[String]] = for {
    _ <- printLn("I can only interact with the console.")
    ln <- readLn
  } yield ln

  println("Got " + runConsole(f1))

}