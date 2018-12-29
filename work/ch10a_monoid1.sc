object monoid1 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet


	import fpinscala.monoids.Monoid._
	import fpinscala.monoids.Monoid
	
  val words = List("Hic", "Est", "Index")         //> words  : List[String] = List(Hic, Est, Index)
	val s = words.foldRight(stringMonoid.zero)(stringMonoid.op)
                                                  //> s  : String = HicEstIndex

	count("lorem ipsum dolor sit amet, ");    //> res0: Int = 5

  def foldMap[A,B](as: List[A], m: Monoid[B])(f: A => B): B =
  	as.foldLeft(m. zero)((a, b) => m.op(a, f(b)))
                                                  //> foldMap: [A, B](as: List[A], m: fpinscala.monoids.Monoid[B])(f: A => B)B
}