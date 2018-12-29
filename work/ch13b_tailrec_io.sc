object ch13b_tailrec_io {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  //
  // 13.3.2 Trampolining: a general solution to stack overflow
  //
  
  // tail rec version of IO
  import fpinscala.iomonad.IO2a._

  val f1 = (x: Int) => x                          //> f1  : Int => Int = ch13b_tailrec_io$$$Lambda$8/1456208737@1cd072a9
  val g1 = List.fill(100000)(f1).foldLeft(f1)(_ compose _)
                                                  //> g1  : Int => Int = scala.Function1$$Lambda$11/2137211482@36d64342
  // g1(42) java.lang.StackOverflowError

  val f: Int => IO[Int] = (i: Int) => Return(i)   //> f  : Int => fpinscala.iomonad.IO2a.IO[Int] = ch13b_tailrec_io$$$Lambda$12/13
                                                  //| 60767589@340f438e

  val g: Int => IO[Int] =
    List.fill(100000)(f).foldLeft(f) {
      (a: Function1[Int, IO[Int]],
      b: Function1[Int, IO[Int]]) =>
        {
          (x: Int) => IO.suspend(a(x).flatMap(b))
        }
    }                                             //> g  : Int => fpinscala.iomonad.IO2a.IO[Int] = ch13b_tailrec_io$$$Lambda$15/43
                                                  //| 4091818@17c68925

  val gFortyTwo = g(42)                           //> gFortyTwo  : fpinscala.iomonad.IO2a.IO[Int] = FlatMap(Suspend(fpinscala.iomo
                                                  //| nad.IO2a$IO$$$Lambda$17/1873653341@6cd8737),fpinscala.iomonad.IO2a$IO$$$Lamb
                                                  //| da$18/718231523@22f71333)
  run(gFortyTwo)                                  //> res0: Int = 42

  val r = Return(println(""))                     //> 
                                                  //| r  : fpinscala.iomonad.IO2a.Return[Unit] = Return(())
  val s = Suspend(() => Return(2))                //> s  : fpinscala.iomonad.IO2a.Suspend[fpinscala.iomonad.IO2a.Return[Int]] = Su
                                                  //| spend(ch13b_tailrec_io$$$Lambda$29/403424356@1324409e)

  def printLine(s: String): IO[Unit] =
    Suspend(() => Return(println(s)))             //> printLine: (s: String)fpinscala.iomonad.IO2a.IO[Unit]

  val p = IO.forever(printLine("Still going...")) //> p  : fpinscala.iomonad.IO2a.IO[Nothing] = FlatMap(Suspend(ch13b_tailrec_io$$
                                                  //| $Lambda$30/745160567@246ae04d),fpinscala.iomonad.Monad$$Lambda$21/940553268@
                                                  //| 62043840)

  // run(p) // see IoFreeMonadApp
  
}