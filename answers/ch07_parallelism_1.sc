object ch07_step_1 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  // 7.1.1
  trait Par[A]
  object Par {
    def unit[A](a: => A): Par[A] = ???
    def get[A](a: Par[A]): A = ???

    // (7.1.2)
    // Exercise 7.1
    /*
			Par.map2 is a new higher-order function for combining the result of two parallel computations.
			What is its signature? Give the most general signature possible (don’t
			assume it works only for Int).
		*/
    def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] = ???

		// (7.1.3)
		def fork[A](a: => Par[A]): Par[A] = ???
  }

  // 7.1 Choosing data types and functions
  // plain recursive
  def sum1(ints: IndexedSeq[Int]): Int =
    if (ints.size <= 1)
      ints.headOption getOrElse 0
    else {
      val (l, r) = ints.splitAt(ints.length / 2)
      sum1(l) + sum1(r)
    }                                             //> sum1: (ints: IndexedSeq[Int])Int

  // 7.1.1 A data type for parallel computations
  // parallelized using basic Par.unit and Par.get
  def sum2(ints: IndexedSeq[Int]): Int =
    if (ints.size <= 1)
      ints.headOption.getOrElse(0)
    else {
      val (l, r) = ints.splitAt(ints.length / 2)
      val sumL: Par[Int] = Par.unit(sum2(l))
      val sumR: Par[Int] = Par.unit(sum2(r))
      Par.get(sumL) + Par.get(sumR)
    }                                             //> sum2: (ints: IndexedSeq[Int])Int

  /*
		Can unit and get—unit could begin evaluating its argument immediately in a separate	(logical) thread,
		or it could simply hold onto its argument until get is called and begin evaluation then?

		If unit begins evaluating its argument concurrently, then calling get arguably
		breaks referential transparency. We can see this by replacing sumL and sumR with their
		definitions—if we do so, we still get the same result, but our program is no longer
		parallel:

		   Par.get(Par.unit(sum(l))) + Par.get(Par.unit(sum(r)))

		If unit starts evaluating its argument right away, the next thing to happen is that get
		will wait for that evaluation to complete.

		So it seems that we want to avoid calling get, or at least delay calling it until the very end.
		*/

  /*
	7.1.2 Combining parallel computations

	Let’s see if we can avoid the aforementioned pitfall of combining unit and get. If we
	don’t call get, that implies that our sum function must return a Par[Int]. What consequences
	does this change reveal? Again, let’s just invent functions with the required
	signatures:
	*/
  def sum3(ints: IndexedSeq[Int]): Par[Int] =
    if (ints.size <= 1)
      Par.unit(ints.headOption getOrElse 0)
    else {
      val (l, r) = ints.splitAt(ints.length / 2)
      Par.map2(sum3(l), sum3(r))(_ + _)
    }                                             //> sum3: (ints: IndexedSeq[Int])ch07_step_1.Par[Int]

  /*
	7.1.3 Explicit forking
	
	What if we make the forking more explicit? We can do that by inventing
	another function, def fork[A](a: => Par[A]): Par[A], which we can take to mean
	that the given Par should be run in a separate logical thread:
	*/

  def sum4(ints: IndexedSeq[Int]): Par[Int] =
    if (ints.length <= 1)
      Par.unit(ints.headOption getOrElse 0)
    else {
      val (l, r) = ints.splitAt(ints.length / 2)
      Par.map2(Par.fork(sum4(l)), Par.fork(sum4(r)))(_ + _)
    }                                             //> sum4: (ints: IndexedSeq[Int])ch07_step_1.Par[Int]

		/*
		With fork, we can now make unit strict without any loss of expressiveness. A non-strict version
		of it, let’s call it lazyUnit, can be implemented using unit and fork:

				def unit[A](a: A): Par[A]
				def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

		continued on nex worksheet as the Par changes ..
		*/

}