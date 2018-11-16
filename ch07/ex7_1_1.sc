import java.util.concurrent._

object ParDef {
  type Par[A] = ExecutorService => Future[A]

  def unit[A](a: => A): Par[A] = ???              //> unit: [A](a: => A)ParDef.Par[A]

  def get[A](a: Par[A]): A = ???                  //> get: [A](a: ParDef.Par[A])A

  //  def map2[A](f: (Par[A], Par[A]) => A) = ???

  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] = ???
                                                  //> map2: [A, B, C](a: ParDef.Par[A], b: ParDef.Par[B])(f: (A, B) => C)ParDef.Pa
                                                  //| r[C]

  def fork[A](a: => Par[A]): Par[A] = ???         //> fork: [A](a: => ParDef.Par[A])ParDef.Par[A]

  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))//> lazyUnit: [A](a: => A)ParDef.Par[A]

  def run[A](a: Par[A]): A = ???                  //> run: [A](a: ParDef.Par[A])A

}

/*
	class ExecutorService {
	  def submit[A](a: Callable[A]): Future[A]
	}

	trait Callable[A] { def call: A }
	trait Future[A] {
	  def get: A
	  def get(timeout: Long, unit: TimeUnit): A
	  def cancel(evenIfRunning: Boolean): Boolean
	  def isDone: Boolean
	  def isCancelled: Boolean
	}
*/

object Par {
  type Par[A] = ExecutorService => Future[A]

  private case class UnitFuture[A](get: A) extends Future[A] {
    def isDone = true
    def get(timeout: Long, units: TimeUnit) = get
    def isCancelled = false
    def cancel(evenIfRunning: Boolean): Boolean = false
  }

  def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a)

  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] =
    (es: ExecutorService) => {
      val af = a(es)
      val bf = b(es)
      UnitFuture(f(af.get, bf.get))
    }

  // Exercise 7.3
  // Hard: Fix the implementation of map2 so that it respects the contract of
  // timeouts on Future.
  private case class Map2Future[A, B, C](fa: Future[A], fb: Future[B], f: (A, B) => C) extends Future[C] {
    @volatile var cache: Option[C] = None
    def isDone = cache.isDefined
    def get(timeout: Long, units: TimeUnit) = compute(TimeUnit.MILLISECONDS.convert(timeout, units))
    def get = compute(Long.MaxValue)
    def isCancelled = fa.isCancelled || fb.isCancelled
    def cancel(evenIfRunning: Boolean): Boolean = fa.cancel(evenIfRunning) || fb.cancel(evenIfRunning)

    private def compute(timeoutMs: Long): C = cache match {
      case Some(c) => c
      case None => {
        val start = System.currentTimeMillis
        val ra = fa.get(timeoutMs, TimeUnit.MILLISECONDS)
        val delta = System.currentTimeMillis - start
        val rb = fb.get(timeoutMs - delta, TimeUnit.MILLISECONDS)
        val r = f(ra, rb)
        cache = Some(r)
        r
      }
    }
  }

  /**
   * Exercise 3
   *
   * Fix the implementation of map2 so as to respect timeouts
   */
  def map2b[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] = { (es: ExecutorService) =>
    val af = a(es)
    val bf = b(es)

    new Future[C] {
      def cancel(mayInterruptIfRunning: Boolean): Boolean = true

      def isCancelled: Boolean = af.isCancelled || bf.isCancelled

      def isDone: Boolean = af.isDone && bf.isDone

      def get(): C = f(af.get, bf.get)

      def get(timeout: Long, unit: TimeUnit): C = {
        val started = System.currentTimeMillis

        val a = af.get(timeout, unit)
        val elapsed = System.currentTimeMillis - started
        val remaining = unit.toMillis(timeout) - elapsed
        val b = bf.get(remaining, unit)

        f(a, b)
      }
    }
  }

  def map[A, B](pa: Par[A])(f: A => B): Par[B] =
    map2(pa, unit(()))((a, _) => f(a))

  def sortPar(parList: Par[List[Int]]) = map(parList)(_.sorted)

	def sequence[A](ps: List[Par[A]]): Par[List[A]] = ???


  def parMap[A, B](ps: List[A])(f: A => B): Par[List[B]] = fork {
    val fbs: List[Par[B]] = ps.map(asyncF(f))
    sequence(fbs)
  }

	// Exercise 7.6
  def parFilter[A](as: List[A])(f: A => Boolean): Par[List[A]] = {
    val pars: List[Par[List[A]]] =
      as map (asyncF((a: A) => if (f(a)) List(a) else List()))
    map(sequence(pars))(_.flatten) 
  }


  def fork[A](a: => Par[A]): Par[A] =
    es => es.submit(new Callable[A] {
      def call = a(es).get
    })

  /*
		Using lazyUnit, write a function to convert any function A => B to one that evaluates its
		result asynchronously.
	*/
  def asyncF[A, B](f: A => B): A => Par[B] =
    a => lazyUnit(f(a))

  def get[A](a: Par[A]): A = ???

  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

  def run[A](a: Par[A]): A = ???

}

object ex7_1_1 {
  println("Welcome to the Scala worksheet")
  import Par._

  def sum(ints: IndexedSeq[Int]): Int =
    if (ints.size <= 1)
      ints.headOption.getOrElse(0)
    else {
      val (l, r) = ints.splitAt(ints.length / 2)
      val sumL: Par[Int] = Par.unit(sum(l))
      val sumR: Par[Int] = Par.unit(sum(r))
      Par.get(sumL) + Par.get(sumR)
    }

  def sum2(ints: IndexedSeq[Int]): Par[Int] =
    if (ints.length <= 1)
      Par.unit(ints.headOption getOrElse 0)
    else {
      val (l, r) = ints.splitAt(ints.length / 2)
      Par.map2(Par.fork(sum2(l)), Par.fork(sum2(r)))(_ + _)
    }

}