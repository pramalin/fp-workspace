object monoid2 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

 import java.util.concurrent.Executors
  import fpinscala.parallelism.Nonblocking.Par._
 	import fpinscala.monoids.Monoid._
 
   val p = parFoldMap(List.range(1, 10).toIndexedSeq, intAddition )(_)
                                                  //> p  : (Int => Int) => fpinscala.parallelism.Nonblocking.Par[Int] = monoid2$$$
                                                  //| Lambda$8/796684896@48140564
    
//   val x = run(Executors.newFixedThreadPool(2))(p)
  count("For example, counting over the string")  //> res0: Int = 6
  
  import fpinscala.monoids._
  val M: Monoid[Map[String, Map[String, Int]]] = mapMergeMonoid(mapMergeMonoid(intAddition))
                                                  //> M  : fpinscala.monoids.Monoid[Map[String,Map[String,Int]]] = fpinscala.monoi
                                                  //| ds.Monoid$$anon$15@180bc464

  val m1 = Map("o1" -> Map("i1" -> 1, "i2" -> 2)) //> m1  : scala.collection.immutable.Map[String,scala.collection.immutable.Map[S
                                                  //| tring,Int]] = Map(o1 -> Map(i1 -> 1, i2 -> 2))
  val m2 = Map("o1" -> Map("i2" -> 3))            //> m2  : scala.collection.immutable.Map[String,scala.collection.immutable.Map[S
                                                  //| tring,Int]] = Map(o1 -> Map(i2 -> 3))
  val m3 = M.op(m1, m2)                           //> m3  : Map[String,Map[String,Int]] = Map(o1 -> Map(i1 -> 1, i2 -> 5))

  val m = productMonoid(intAddition, intAddition) //> m  : fpinscala.monoids.Monoid[(Int, Int)] = fpinscala.monoids.Monoid$$anon$1
                                                  //| 3@59e84876
  val p1 = ListFoldable.foldMap(List(1,2,3,4))(a => (1, a))(m)
                                                  //> p1  : (Int, Int) = (4,10)
  val mean = p1._2 / p1._1.toDouble               //> mean  : Double = 2.5
  
}