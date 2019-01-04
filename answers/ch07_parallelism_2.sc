object ch07_step_2 {
  println("Welcome to the Scala worksheet")

  /*
			continued
			7.1.3 Explicit forking

		If fork begins evaluating its argument immediately in parallel, the implementation
		must clearly know something, either directly or indirectly, about how to create
		threads or submit tasks to some sort of thread pool.

		In contrast, if fork simply holds on to its unevaluated argument until later, it
		requires no access to the mechanism for implementing parallelism.

		With this model, Par itself doesn’t need to know how to actually
		implement the parallelism. It’s more a description of a parallel computation that gets
		interpreted at a later time by something like the get function. This is a shift from
		before, where we were considering Par to be a container of a value that we could simply
		get when it becomes available. Now it’s more of a first-class program that we can run. So
		let’s rename our get function to run, and dictate that this is where the parallelism
		actually gets implemented:

			def run[A](a: Par[A]): A

	*/

  /*
		7.2 Picking a representation
		
		Let’s see if we can come up with a representation. We know run needs to execute
		asynchronous tasks somehow. We could write our own low-level API, but there’s
		already a class that we can use in the Java Standard Library, java.util.concurrent
		.ExecutorService. Here is its API, excerpted and transcribed to Scala:
	*/
	
  type TimeUnit = Int
  class ExecutorService {
    def submit[A](a: Callable[A]): Future[A]
  }
  
  trait Callable[A] { def call: A } // Essentially just a lazy A
  
  trait Future[A] {
    def get: A
    def get(timeout: Long, unit: TimeUnit): A
    def cancel(evenIfRunning: Boolean): Boolean
    def isDone: Boolean
    def isCancelled: Boolean
  }
 
	/*
	Let’s try assuming that our run function has access to an ExecutorService and see
	if that suggests anything about the representation for Par:
			def run[A](s: ExecutorService)(a: Par[A]): A

	The simplest possible model for Par[A] might be ExecutorService => A.
	So Par[A] becomes ExecutorService => Future[A], and run simply returns the Future:
	*/

	type Par[A] = ExecutorService => Future[A]
  object Par {
    def unit[A](a: A): Par[A] = ???
    def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] = ???
    def fork[A](a: => Par[A]): Par[A] = ???
    def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

    // (7.1.3)
    // change 'get' to run
    //def get[A](a: Par[A]): A = ???
    def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)
  }
  
}