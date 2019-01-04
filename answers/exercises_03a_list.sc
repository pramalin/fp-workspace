object exercises_03a_list {

  import fpinscala.datastructures._
  import fpinscala.datastructures.List._

  // 3.1 What will be the result of the following match expression?
  val x = List(1, 2, 3, 4, 5) match {
    case Cons(x, Cons(2, Cons(4, _)))          => x
    case Nil                                   => 42
    case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
    case Cons(h, t)                            => h + sum(t)
    case _                                     => 101
  }                                               //> x  : Int = 3

  // 3.2 def tail[A](l: List[A]): List[A]
  tail(List(1, 2, 3, 4))                          //> res0: fpinscala.datastructures.List[Int] = Cons(2,Cons(3,Cons(4,Nil)))

  // 3.3 def setHead[A](l: List[A], h: A): List[A]
  setHead(List(-1, 1, 2), 0)                      //> res1: fpinscala.datastructures.List[Int] = Cons(0,Cons(1,Cons(2,Nil)))

  // 3.4 def drop[A](l: List[A], n: Int): List[A]]
  drop(List(1, 2, 3, 4, 5), 2)                    //> res2: fpinscala.datastructures.List[Int] = Cons(3,Cons(4,Cons(5,Nil)))

  // 3.5 def dropWhile[A](l: List[A], f: A => Boolean): List[A]
  dropWhile(List(1, 2, 3, 4, 5, 6), (a: Int) => a < 4)
                                                  //> res3: fpinscala.datastructures.List[Int] = Cons(4,Cons(5,Cons(6,Nil)))

  // 3.6 def init[A](l: List[A]): List[A]
  init(List(1, 2, 3))                             //> res4: fpinscala.datastructures.List[Int] = Cons(1,Cons(2,Nil))
  
  // 3.7
	/*
	Can product, implemented using foldRight, immediately halt the recursion and
	return 0.0 if it encounters a 0.0? Why or why not? Consider how any short-circuiting
	might work if you call foldRight with a large list. This is a deeper question that we’ll
	return to in chapter 5.
	*/
	product(List(1,2,3))                      //> res5: Double = 6.0
	product(List(1,2,3,0))                    //> res6: Double = 0.0

	// 3.8
	/*
	See what happens when you pass Nil and Cons themselves to foldRight, like this:
	foldRight(List(1,2,3), Nil:List[Int])(Cons(_,_)) What do you think this
	says about the relationship between foldRight and the data constructors of List?
	*/
	foldRight(List(1,2,3), Nil:List[Int])(Cons(_,_))
                                                  //> res7: fpinscala.datastructures.List[Int] = Cons(1,Cons(2,Cons(3,Nil)))
	// 3.9
  // Compute the length of a list using foldRight.
  // def length[A](as: List[A]): Int
  length(List(1,2,3,4))                           //> res8: Int = 4
  
   // 3.10
   /*
    Our implementation of foldRight is not tail-recursive and will result in a StackOverflowError
		for large lists (we say it’s not stack-safe). Convince yourself that this is the
		case, and then write another general list-recursion function, foldLeft, that is
		
		def foldLeft[A,B](as: List[A], z: B)(f: (B, A) => B): B
   */
  foldLeft(List(1,2,3), 0)(_ + _)                 //> res9: Int = 6


}