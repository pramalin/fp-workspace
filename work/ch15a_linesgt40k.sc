object ch15a_linesgt40k {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  import fpinscala.iomonad.IO3._

  def linesGt40k(filename: String): IO[Boolean] = IO {
    val src = io.Source.fromFile(filename)
    try {
      var count = 0
      val lines: Iterator[String] = src.getLines
      while (count <= 40000 && lines.hasNext) {
        lines.next
        count += 1
      }
      count > 40000
    } finally src.close
  }                                               //> linesGt40k: (filename: String)fpinscala.iomonad.IO3.IO[Boolean]

  import java.util.concurrent._
  import fpinscala.parallelism.Nonblocking.Par

  def unsafePerformIO[A](a: IO[A])(pool: ExecutorService): A =
    Par.run(pool)(run(a)(parMonad))               //> unsafePerformIO: [A](a: fpinscala.iomonad.IO3.IO[A])(pool: java.util.concurr
                                                  //| ent.ExecutorService)A

  val pool = Executors.newFixedThreadPool(8)      //> pool  : java.util.concurrent.ExecutorService = java.util.concurrent.ThreadPo
                                                  //| olExecutor@6bdf28bb[Running, pool size = 0, active threads = 0, queued tasks
                                                  //|  = 0, completed tasks = 0]
  unsafePerformIO(linesGt40k("c:\\tmp\\lines.txt"))(pool)
                                                  //> res0: Boolean = true

}