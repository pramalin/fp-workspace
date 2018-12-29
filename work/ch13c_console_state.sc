object ch13c_console_state {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  import fpinscala.iomonad.IO3._
  import fpinscala.iomonad.IO3.Console._
  

    val f1: Free[Console, Option[String]] = for {
      _ <- printLn("I can only interact with the console.")
      ln <- readLn
    } yield ln                                    //> f1  : fpinscala.iomonad.IO3.Free[fpinscala.iomonad.IO3.Console,Option[String
                                                  //| ]] = FlatMap(Suspend(PrintLine(I can only interact with the console.)),ch13c
                                                  //| _console_state$$$Lambda$8/1456208737@7a79be86)


  
  val out = runConsoleState(f1)                   //> out  : fpinscala.iomonad.IO3.ConsoleState[Option[String]] = ConsoleState(fpi
                                                  //| nscala.iomonad.IO3$ConsoleState$$Lambda$12/398887205@7e0ea639)

}