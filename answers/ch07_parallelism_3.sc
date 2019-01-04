object ch07_step_3 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  import java.util.concurrent._
  import language.implicitConversions

  /*
		7.3 Refining the API

		What follows is a simplistic implementation using the representation of Par that we’ve chosen.
	*/

  object Par {
    type Par[A] = ExecutorService => Future[A]

    def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)

    def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a)
    // `unit` is represented as a function that returns a `UnitFuture`, which is a simple implementation of
    // `Future` that just wraps a constant value. It doesn't use the `ExecutorService` at all. It's always
    // done and can't be cancelled. Its `get` method simply returns the value that we gave it.

    def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

    private case class UnitFuture[A](get: A) extends Future[A] {
      def isDone = true
      def get(timeout: Long, units: TimeUnit) = get
      def isCancelled = false
      def cancel(evenIfRunning: Boolean): Boolean = false
    }

    def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] =
      (es: ExecutorService) => {
        val af = a(es)
        val bf = b(es)
        UnitFuture(f(af.get, bf.get))
      }

    def fork[A](a: => Par[A]): Par[A] =
      es => es.submit(new Callable[A] {
        def call = a(es).get
      })

    // Exercise 7.4
    def asyncF[A, B](f: A => B): A => Par[B] =
      a => lazyUnit(f(a))
   
    // Exercise 7.5
    def sequence[A](l: List[Par[A]]): Par[List[A]] =
      l.foldRight[Par[List[A]]](unit(List()))((h,t) => map2(h,t)(_ :: _))



  }

  /*
		map2 doesn’t evaluate the call to f in a separate logical thread, in accord
		with our design choice of having fork be the sole function in the API for
		controlling parallelism. We can always do fork(map2(a,b)(f)) if we
		want the evaluation of f to occur in a separate thread.

		This implementation of map2 does not respect timeouts. It simply passes the
		ExecutorService on to both Par values, waits for the results of the Futures af and
		bf, applies f to them, and wraps them in a UnitFuture. In order to respect timeouts,
		we’d need a new Future implementation that records the amount of time spent evaluating
		af, and then subtracts that time from the available time allocated for evaluating bf.

		This is the simplest and most natural implementation of fork,but there are some
		problems with it—for one, the outer Callable will block waiting for the “inner”
		task to complete. Since this blocking occupies a thread in our thread pool, or
		whatever resource backs the ExecutorService, this implies that we’re losing out
		on some potential parallelism. Essentially, we’re using two threads when one should
		suffice. This is a symptom of a more serious problem with the implementation that
		we’ll discuss later in the chapter.
*/

  import Par._

  def sum(ints: IndexedSeq[Int]): Par[Int] =
    if (ints.length <= 1)
      Par.unit(ints.headOption getOrElse 0)
    else {
      val (l, r) = ints.splitAt(ints.length / 2)
      Par.map2(Par.fork(sum(l)), Par.fork(sum(r)))(_ + _)
    }                                             //> sum: (ints: IndexedSeq[Int])ch07_step_3.Par.Par[Int]

  val psum = sum(Vector(1, 2, 3))                 //> psum  : ch07_step_3.Par.Par[Int] = ch07_step_3$Par$2$$$Lambda$13/2017354584
                                                  //| @60addb54
//  val sumResult = run(Executors.newFixedThreadPool(2))(psum)
//  sumResult.get
  /*
	EXERCISE 7.4
	This API already enables a rich set of operations. Here’s a simple example: using
	lazyUnit, write a function to convert any function A => B to one that evaluates its
	result asynchronously.

			def asyncF[A,B](f: A => B): A => Par[B]
	*/

  /*

	Suppose we have a Par[List[Int]] representing a parallel computation that produces
	a List[Int], and we’d like to convert this to a Par[List[Int]] whose result is sorted:
			def sortPar(parList: Par[List[Int]]): Par[List[Int]]

	We could of course run the Par, sort the resulting list, and repackage it in a Par with
	unit. But we want to avoid calling run. The only other combinator we have that allows
	us to manipulate the value of a Par in any way is map2. So if we passed parList to one
	side of map2, we’d be able to gain access to the List inside and sort it. And we can pass
	whatever we want to the other side of map2, so let’s just pass a no-op:
  */

  def sortPar(parList: Par[List[Int]]): Par[List[Int]] =
    map2(parList, unit(()))((a, _) => a.sorted)   //> sortPar: (parList: ch07_step_3.Par.Par[List[Int]])ch07_step_3.Par.Par[List[
                                                  //| Int]]

  /*
	That was easy. We can now tell a Par[List[Int]] that we’d like that list sorted. But we
	might as well generalize this further. We can “lift” any function of type A => B to
	become a function that takes Par[A] and returns Par[B]; we can map any function
	over a Par:
	*/
	
	def map[A,B](pa: Par[A])(f: A => B): Par[B] =
		map2(pa, unit(()))((a,_) => f(a)) //> map: [A, B](pa: ch07_step_3.Par.Par[A])(f: A => B)ch07_step_3.Par.Par[B]
	/*
	For instance, sortPar is now simply this:
	*/
  
  def sortPar1(parList: Par[List[Int]]) = map(parList)(_.sorted)
                                                  //> sortPar1: (parList: ch07_step_3.Par.Par[List[Int]])ch07_step_3.Par.Par[List
                                                  //| [Int]]
 	// test
  val parList = unit(List(2,1,3))                 //> parList  : ch07_step_3.Par.Par[List[Int]] = ch07_step_3$Par$2$$$Lambda$14/1
                                                  //| 796488937@1936f0f5
  val listToSort = sortPar1(parList)              //> listToSort  : ch07_step_3.Par.Par[List[Int]] = ch07_step_3$Par$2$$$Lambda$1
                                                  //| 3/2017354584@67b6d4ae
  val sortResult = run(Executors.newFixedThreadPool(2))(listToSort)
                                                  //> sortResult  : java.util.concurrent.Future[List[Int]] = UnitFuture(List(1, 2
                                                  //| , 3))
	/*
	What else can we implement using our API? Could we map over a list in parallel?
	Unlike map2, which combines two parallel computations, parMap (let’s call it) needs to
	combine Nparallel computations. It seems like this should somehow be expressible:

			def parMap[A,B](ps: List[A])(f: A => B): Par[List[B]]
	We could always just write parMap as a new primitive. Remember that Par[A] is simply
	an alias for ExecutorService => Future[A].
	
	But right	now we’re interested in exploring what operations are expressible using our existing
	API, and grasping the relationships between the various operations we’ve defined.

	Let’s see how far we can get implementing parMap in terms of existing combinators:

		def parMap[A,B](ps: List[A])(f: A => B): Par[List[B]] = {
			val fbs: List[Par[B]] = ps.map(asyncF(f))
			...
		}

	Remember, asyncF converts an A => B to an A => Par[B] by forking a parallel computation
	to produce the result. So we can fork off our N parallel computations pretty
	easily, but we need some way of collecting their results. Are we stuck? Well, just from
	inspecting the types, we can see that we need some way of converting our
	List[Par[B]] to the Par[List[B]] required by the return type of parMap.

	EXERCISE 7.5
	Hard: Write this function, called sequence. No additional primitives are required. Do
	not call run.
		def sequence[A](ps: List[Par[A]]): Par[List[A]]
	
	Once we have sequence, we can complete our implementation of parMap:
	*/
	
	def parMap[A,B](ps: List[A])(f: A => B): Par[List[B]] = fork {
		val fbs: List[Par[B]] = ps.map(asyncF(f))
		sequence(fbs)
	}                                         //> parMap: [A, B](ps: List[A])(f: A => B)ch07_step_3.Par.Par[List[B]]
	
	val squared = parMap(List(1,2,3))(a => a * a)
                                                  //> squared  : ch07_step_3.Par.Par[List[Int]] = ch07_step_3$Par$2$$$Lambda$10/9
                                                  //| 40060004@7f560810
	val squaredResult = run(Executors.newFixedThreadPool(2))(squared)
                                                  //> squaredResult  : java.util.concurrent.Future[List[Int]] = java.util.concurr
                                                  //| ent.FutureTask@7ca48474
	squaredResult.get                         //> res0: List[Int] = List(1, 4, 9)
	
	/*
	Note that we’ve wrapped our implementation in a call to fork. With this implementation,
	parMap will return immediately, even for a huge input list. When we later call
	run, it will fork a single asynchronous computation which itself spawns N parallel computations,
	and then waits for these computations to finish, collecting their results into
	a list.
	
	EXERCISE 7.6
	Implement parFilter, which filters elements of a list in parallel.
	*/
  def parFilter[A](l: List[A])(f: A => Boolean): Par[List[A]] = {
    val pars: List[Par[List[A]]] =
      l map (asyncF((a: A) => if (f(a)) List(a) else List()))
    map(sequence(pars))(_.flatten)
      // convenience method on `List` for concatenating a list of lists
  }                                               //> parFilter: [A](l: List[A])(f: A => Boolean)ch07_step_3.Par.Par[List[A]]
	val toFilter = parFilter(List(1,2,3,4))(a => a % 2 == 0)
                                                  //> toFilter  : ch07_step_3.Par.Par[List[Int]] = ch07_step_3$Par$2$$$Lambda$13/
                                                  //| 2017354584@6ea12c19
	val filteredResult = run(Executors.newFixedThreadPool(2))(toFilter)
                                                  //> filteredResult  : java.util.concurrent.Future[List[Int]] = UnitFuture(List(
                                                  //| 2, 4))
	
	filteredResult.get                        //> res1: List[Int] = List(2, 4)
	
}