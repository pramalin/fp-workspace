object state_test {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  import fpinscala.state._
  import fpinscala.state.Candy._


	val m = Machine(true, 5, 10)              //> m  : fpinscala.state.Machine = Machine(true,5,10)
	
	val m1 = update(Coin)                     //> m1  : fpinscala.state.Machine => fpinscala.state.Machine = fpinscala.state.C
                                                  //| andy$$$Lambda$10/873415566@30c7da1e
  val m2 = update(Turn)                           //> m2  : fpinscala.state.Machine => fpinscala.state.Machine = fpinscala.state.C
                                                  //| andy$$$Lambda$10/873415566@5b464ce8

	val s1 = simulateMachine(List(Coin, Turn, Coin, Turn, Coin, Turn, Coin, Turn))
                                                  //> s1  : fpinscala.state.State[fpinscala.state.Machine,(Int, Int)] = State(fpin
                                                  //| scala.state.State$$Lambda$16/796533847@6bf256fa)

	val r = s1.run(m)                         //> r  : ((Int, Int), fpinscala.state.Machine) = ((14,1),Machine(true,1,14))
	
}