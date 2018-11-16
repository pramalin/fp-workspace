object range_test {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  import fpinscala.state.RNG._

  val rng = Simple(42)                            //> rng  : fpinscala.state.RNG.Simple = Simple(42)

  val (n1, rng2) = rng.nextInt                    //> n1  : Int = 16159453
                                                  //| rng2  : fpinscala.state.RNG = Simple(1059025964525)

  val (n2, rng3) = rng2.nextInt                   //> n2  : Int = -1281479697
                                                  //| rng3  : fpinscala.state.RNG = Simple(197491923327988)

  val is = ints(4)(Simple(10))                    //> is  : (List[Int], fpinscala.state.RNG) = (List(3847489, 1334288366, 14868620
                                                  //| 10, 711662464),Simple(46639511274414))

  val (v1, r1) = nonNegativeInt(Simple(12))       //> v1  : Int = 4616986
                                                  //| r1  : fpinscala.state.RNG = Simple(302578847015)
  val (v2, r2) = r1.nextInt                       //> v2  : Int = -976680786
                                                  //| r2  : fpinscala.state.RNG = Simple(217467224744870)

  val (d3a, r3a) = double3(Simple(12))            //> d3a  : (Double, Double, Double) = (0.00214995164424181,0.4548024316318333,0.
                                                  //| 01419649226590991)
                                                  //| r3a  : fpinscala.state.RNG = Simple(1997978702265)
  val (d3b, r3b) = double3(r3a)                   //> d3b  : (Double, Double, Double) = (0.8729846654459834,0.9579336186870933,0.7
                                                  //| 302256654947996)
                                                  //| r3b  : fpinscala.state.RNG = Simple(102770126095626)

  def rollDie: Rand[Int] = nonNegativeLessThan(6) //> rollDie: => fpinscala.state.RNG.Rand[Int]

  def rollDie2: Rand[Int] = map(nonNegativeLessThan(6))(_ + 1)
                                                  //> rollDie2: => fpinscala.state.RNG.Rand[Int]

  val dies = rollDie2(Simple(5))._1               //> dies  : Int = 1
  dies.intValue()                                 //> res0: Int = 1
  dies.intValue()                                 //> res1: Int = 1

  def nonNegativeEven: Rand[Int] =
    map(nonNegativeInt)(i => i - i % 2)           //> nonNegativeEven: => fpinscala.state.RNG.Rand[Int]

  val pos1 = nonNegativeEven(Simple(0))           //> pos1  : (Int, fpinscala.state.RNG) = (0,Simple(11))

  //	def u1: Rand[Int] = unit(3)(Simple(10))

  val die2 = map(rollDie2)(_ * 10)                //> die2  : fpinscala.state.RNG.Rand[Int] = fpinscala.state.RNG$$$Lambda$12/1286
                                                  //| 783232@50f8360d

	ints(4)(rng)                              //> res2: (List[Int], fpinscala.state.RNG) = (List(16159453, -1281479697, -34030
                                                  //| 5902, -2015756020),Simple(149370390209998))

}