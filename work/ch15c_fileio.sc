object ch15c_fileio {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  import fpinscala.streamingio._
  import fpinscala.streamingio.SimpleStreamTransducers.Process._

  val f = "c:\\tmp\\lines.txt"                    //> f  : String = c:\tmp\lines.txt

  val p = processFile(f, count |> exists(_ > 40000), false)(_ || _)
                                                  //> p  : fpinscala.iomonad.IO[Boolean] = Suspend(fpinscala.parallelism.Nonblocki
                                                  //| ng$Par$$$Lambda$22/1096283470@52af6cff)
  import fpinscala.iomonad.IO
  import GeneralizedStreamTransducers._
  import Process._

  val r = eval(p)                                 //> r  : fpinscala.streamingio.GeneralizedStreamTransducers.Process[fpinscala.io
                                                  //| monad.IO,Boolean] = Await(Suspend(fpinscala.parallelism.Nonblocking$Par$$$La
                                                  //| mbda$22/1096283470@52af6cff),fpinscala.streamingio.GeneralizedStreamTransduc
                                                  //| ers$Process$$$Lambda$29/329611835@6cc4c815)


import fpinscala.streamingio.GeneralizedStreamTransducers._
    import fpinscala.iomonad.IO0.fahrenheitToCelsius

    val converter: Process[IO,Unit] =
      lines("c:\\tmp\\fahrenheit.txt").
      filter(line => !line.startsWith("#") && !line.trim.isEmpty).
      map(line => fahrenheitToCelsius(line.toDouble).toString).
      pipe(intersperse("\n")).
      to(fileW("c:\\tmp\\celsius.txt")).
      drain                                       //> converter  : fpinscala.streamingio.GeneralizedStreamTransducers.Process[fpin
                                                  //| scala.iomonad.IO,Unit] = Await(Suspend(fpinscala.parallelism.Nonblocking$Par
                                                  //| $$$Lambda$22/1096283470@643b1d11),scala.Function1$$Lambda$32/1508395126@2ef5
                                                  //| e5e3)
 
    runLog(converter)                             //> res0: fpinscala.iomonad.IO[IndexedSeq[Unit]] = Suspend(fpinscala.parallelism
                                                  //| .Nonblocking$Par$$$Lambda$22/1096283470@51efea79)
      
}