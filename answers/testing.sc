object testing {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  import fpinscala.testing._
  import fpinscala.testing.Prop._
  import fpinscala.testing.Gen._
  import fpinscala.state._
  import fpinscala.testing.SGen._
  import fpinscala.parallelism.Par


  val smallInt = Gen.choose(-10, 10)              //> smallInt  : fpinscala.testing.Gen[Int] = Gen(State(fpinscala.state.State$$La
                                                  //| mbda$14/509886383@5afa04c))
  val maxProp = forAll(listOf1(smallInt)) { ns =>
    val max = ns.max
    !ns.exists(_ > max)
  }                                               //> maxProp  : fpinscala.testing.Prop = Prop(fpinscala.testing.Prop$$$Lambda$41/
                                                  //| 592179046@174d20a)

  val r1 = maxProp.run(100, 100, RNG.Simple(System.currentTimeMillis))
                                                  //> r1  : fpinscala.testing.Prop.Result = Passed
  r1.isFalsified                                  //> res0: Boolean = false

  run(maxProp)                                    //> + OK, passed 100 tests.

  val u1 = Gen.unit(10)                           //> u1  : fpinscala.testing.Gen[Int] = Gen(State(fpinscala.state.State$$$Lambda$
                                                  //| 17/818403870@5e3a8624))

  u1.sample.run(RNG.Simple(1))                    //> res1: (Int, fpinscala.state.RNG) = (10,Simple(1))

  smallInt.sample.run(RNG.Simple(1))              //> res2: (Int, fpinscala.state.RNG) = (-2,Simple(25214903928))

  val l1 = Gen.listOfN(3, u1)                     //> l1  : fpinscala.testing.Gen[List[Int]] = Gen(State(fpinscala.state.State$$$L
                                                  //| ambda$67/1211076369@7cd62f43))
  l1.sample.run(RNG.Simple(1))                    //> res3: (List[Int], fpinscala.state.RNG) = (List(10, 10, 10),Simple(1))

  val l2 = Gen.listOfN(10, smallInt)              //> l2  : fpinscala.testing.Gen[List[Int]] = Gen(State(fpinscala.state.State$$$L
                                                  //| ambda$67/1211076369@39c0f4a))
  l2.sample.run(RNG.Simple(1))                    //> res4: (List[Int], fpinscala.state.RNG) = (List(-2, 8, -4, -9, -9, -1, 2, -3,
                                                  //|  -5, -1),Simple(147838658590923))

  // pair of Gen
  val u2 = Gen.unit((10, 20))                     //> u2  : fpinscala.testing.Gen[(Int, Int)] = Gen(State(fpinscala.state.State$$$
                                                  //| Lambda$17/818403870@42e26948))
  u2.sample.run(RNG.Simple(1))                    //> res5: ((Int, Int), fpinscala.state.RNG) = ((10,20),Simple(1))

  val fm1 = smallInt.flatMap(a => Gen.unit(a * 10))
                                                  //> fm1  : fpinscala.testing.Gen[Int] = Gen(State(fpinscala.state.State$$Lambda$
                                                  //| 14/509886383@343f4d3d))
  fm1.sample.run(RNG.Simple(1))                   //> res6: (Int, fpinscala.state.RNG) = (-20,Simple(25214903928))

  trait PropEx83 {
    def check: Boolean
    def &&(b: PropEx83): Boolean =
      this.check && b.check
  }

  val ou1 = u1 map (Option(_))                    //> ou1  : fpinscala.testing.Gen[Option[Int]] = Gen(State(fpinscala.state.State
                                                  //| $$Lambda$14/509886383@548e7350))

  //	val vu1 = ou1 map(_)

  val p2 = Prop.check {
    val p = Par.map(Par.unit(1))(_ + 1)
    val p2 = Par.unit(2)
    p(ES).get == p2(ES).get
  }                                               //> p2  : fpinscala.testing.Prop = Prop(fpinscala.testing.Prop$$$Lambda$22/7182
                                                  //| 31523@4667ae56)

}