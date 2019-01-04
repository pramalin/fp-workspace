object ch15b_process {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  import fpinscala.streamingio._
  import fpinscala.streamingio.SimpleStreamTransducers.Process._

  val p = liftOne((x: Int) => x * 2)              //> p  : fpinscala.streamingio.SimpleStreamTransducers.Process[Int,Int] = Await(
                                                  //| fpinscala.streamingio.SimpleStreamTransducers$Process$$$Lambda$11/1435804085
                                                  //| @6615435c)
  val xs = p(Stream(1, 2, 3)).toList              //> xs  : List[Int] = List(2)

  val units = Stream.continually(())              //> units  : scala.collection.immutable.Stream[Unit] = Stream((), ?)
  val ones = lift((_: Unit) => 1)(units)          //> ones  : Stream[Int] = Stream(1, ?)

  val even = filter((x: Int) => x % 2 == 0)       //> even  : fpinscala.streamingio.SimpleStreamTransducers.Process[Int,Int] = Awa
                                                  //| it(fpinscala.streamingio.SimpleStreamTransducers$Process$$Lambda$12/17846620
                                                  //| 07@2344fc66)
  val evens = even(Stream(1, 2, 3, 4)).toList     //> evens  : List[Int] = List(2, 4)
  val s = sum(Stream(1.0, 2.0, 3.0, 4.0)).toList  //> s  : List[Double] = List(1.0, 3.0, 6.0, 10.0)

   val stream = Stream(1.0, 2.0, 3.0, 4.0, 5.0, 1.0)
                                                  //> stream  : scala.collection.immutable.Stream[Double] = Stream(1.0, ?)
  
  (id[Double]).zipWithIndex(stream).toList        //> res0: List[(Double, Int)] = List((1.0,0), (2.0,1), (3.0,2), (4.0,3), (5.0,4)
                                                  //| , (1.0,5))
   
  exists((_: Int) % 2 == 0)(Stream(1,3,5,6,7)).toList
                                                  //> res1: List[Boolean] = List(false, false, false, true, true)
  
}