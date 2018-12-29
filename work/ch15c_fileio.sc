object ch15c_fileio {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  import java.util.concurrent._
  import fpinscala.parallelism.Nonblocking.Par
  import fpinscala.iomonad.IO3._
  
  def unsafePerformIO[A](a: IO[A])(pool: ExecutorService): A =
    Par.run(pool)(run(a)(parMonad))               //> unsafePerformIO: [A](a: fpinscala.iomonad.IO3.IO[A])(pool: java.util.concurr
                                                  //| ent.ExecutorService)A

  val pool = Executors.newFixedThreadPool(8)      //> pool  : java.util.concurrent.ExecutorService = java.util.concurrent.ThreadPo
                                                  //| olExecutor@6bdf28bb[Running, pool size = 0, active threads = 0, queued tasks
                                                  //|  = 0, completed tasks = 0]
  import fpinscala.streamingio._
  import fpinscala.streamingio.SimpleStreamTransducers.Process._

  val f = "c:\\tmp\\lines.txt"                    //> f  : String = c:\tmp\lines.txt

  val p = processFile(f, count |> exists(_ > 40000), false)(_ || _)
                                                  //> p  : fpinscala.iomonad.IO[Boolean] = Suspend(fpinscala.parallelism.Nonblocki
                                                  //| ng$Par$$$Lambda$22/759156157@2096442d)
  import fpinscala.iomonad.IO
  import GeneralizedStreamTransducers._
  import Process._

  unsafePerformIO(p)(pool)                        //> res0: Boolean = true

//
//
//
  import fpinscala.streamingio.GeneralizedStreamTransducers._
  import fpinscala.iomonad.IO0.fahrenheitToCelsius

  val converter: Process[IO, Unit] =
    lines("c:\\tmp\\fahrenheit.txt").
      filter(line => !line.startsWith("#") && !line.trim.isEmpty).
      map(line => fahrenheitToCelsius(line.toDouble).toString).
      pipe(intersperse("\n")).
      to(fileW("c:\\tmp\\celsius.txt")).
      drain                                       //> converter  : fpinscala.streamingio.GeneralizedStreamTransducers.Process[fpi
                                                  //| nscala.iomonad.IO,Unit] = Await(Suspend(fpinscala.parallelism.Nonblocking$P
                                                  //| ar$$$Lambda$22/759156157@6767c1fc),scala.Function1$$Lambda$36/1144648478@29
                                                  //| ee9faa)
  val prog = runLog(converter)                    //> prog  : fpinscala.iomonad.IO[IndexedSeq[Unit]] = Suspend(fpinscala.parallel
                                                  //| ism.Nonblocking$Par$$$Lambda$22/759156157@55f3ddb1)

  unsafePerformIO(prog)(pool)                     //> java.lang.StackOverflowError
                                                  //| 	at fpinscala.streamingio.GeneralizedStreamTransducers$Process.$bar$great
                                                  //| er(StreamingIO.scala:619)
                                                  //| 	at fpinscala.streamingio.GeneralizedStreamTransducers$Process.$bar$great
                                                  //| er$(StreamingIO.scala:617)
                                                  //| 	at fpinscala.streamingio.GeneralizedStreamTransducers$Process$Halt.$bar$
                                                  //| greater(StreamingIO.scala:712)
                                                  //| 	at fpinscala.streamingio.GeneralizedStreamTransducers$Process.$bar$great
                                                  //| er(StreamingIO.scala:622)
                                                  //| 	at fpinscala.streamingio.GeneralizedStreamTransducers$Process.$bar$great
                                                  //| er$(StreamingIO.scala:617)
                                                  //| 	at fpinscala.streamingio.GeneralizedStreamTransducers$Process$Halt.$bar$
                                                  //| greater(StreamingIO.scala:712)
                                                  //| 	at fpinscala.streamingio.GeneralizedStreamTransducers$Process.$bar$great
                                                  //| er(StreamingIO.scala:622)
                                                  //| 	at fpinscala.streamingio.GeneralizedStreamTransducers$Process.$bar$great
                                                  //| er$(StreamingIO.scala:617)
                                                  //| 	at fpinscala.streamingio.GeneralizedStreamTransducers$Proc
                                                  //| Output exceeds cutoff limit.


}