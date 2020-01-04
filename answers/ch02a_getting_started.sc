object getting_started {
  println("Welcome to the Scala worksheet")

  def isSorted[A](as: Array[A], gt: (A, A) => Boolean): Boolean = {

    @annotation.tailrec
    def loop(n: Int): Boolean =
      if (n >= as.length - 1) true
      else if (gt(as(n), as(n + 1))) false
      else loop(n + 1)

    loop(0)
  }                                               //> isSorted: [A](as: Array[A], gt: (A, A) => Boolean)Boolean

  // Exercise 2: Implement a polymorphic function to check whether
  // an `Array[A]` is sorted
  def isSorted2[A](as: Array[A], gt: (A, A) => Boolean): Boolean = {
    @annotation.tailrec
    def go(n: Int): Boolean =
      if (n >= as.length - 1) true
      else if (gt(as(n), as(n + 1))) false
      else go(n + 1)

    go(0)
  }                                               //> isSorted2: [A](as: Array[A], gt: (A, A) => Boolean)Boolean

  isSorted(Array(1, 2, 4, 5), ((a: Int, b: Int) => a > b))
                                                  //> res0: Boolean = true

  def partial1[A, B, C](a: A, f: (A, B) => C): B => C =
    (b: B) => f(a, b)                             //> partial1: [A, B, C](a: A, f: (A, B) => C)B => C

  def curry[A, B, C](f: (A, B) => C): A => (B => C) =
    a => b => f(a, b)                             //> curry: [A, B, C](f: (A, B) => C)A => (B => C)

  def uncurry[A, B, C](f: A => B => C): (A, B) => C =
    (a, b) => f(a)(b)                             //> uncurry: [A, B, C](f: A => (B => C))(A, B) => C

  def compose[A, B, C](f: B => C, g: A => B): A => C =
    a => f(g(a))                                  //> compose: [A, B, C](f: B => C, g: A => B)A => C

  def mean(xs: Seq[Double]): Option[Double] =
    if (xs.isEmpty) None
    else Some(xs.sum / xs.length)                 //> mean: (xs: Seq[Double])Option[Double]

  def variance(xs: Seq[Double]): Option[Double] =
    mean(xs) flatMap (m => mean(xs.map(x => math.pow(x - m, 2))))
                                                  //> variance: (xs: Seq[Double])Option[Double]

  variance(List(2, 4, 6))                         //> res1: Option[Double] = Some(2.6666666666666665)

  case class Employee(name: String, department: String)

  val employees: Map[String, Employee] = Map(
    "Joe" -> Employee("Joe", "Marketing"),
    "kane" -> Employee("Kane", "Accounting"))     //> employees  : Map[String,getting_started.Employee] = Map(Joe -> Employee(Joe
                                                  //| ,Marketing), kane -> Employee(Kane,Accounting))

  def lookupByName(name: String): Option[Employee] =
    employees.get(name)                           //> lookupByName: (name: String)Option[getting_started.Employee]

  val joeDepartment: Option[String] =
    lookupByName("Joe").map(_.department)         //> joeDepartment  : Option[String] = Some(Marketing)

  val dept: String =
    lookupByName("Joe").
      map(_.department).
      filter(_ != "Accounting").
      getOrElse("Default Dept")                   //> dept  : String = Marketing

  def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
    a flatMap (aa => b map (bb => f(aa, bb)))     //> map2: [A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C)Option[C]

  /*
  Here's an explicit recursive version:
  */
  def sequence[A](a: List[Option[A]]): Option[List[A]] =
    a match {
      case Nil    => Some(Nil)
      case h :: t => h flatMap (hh => sequence(t) map (hh :: _))
    }                                             //> sequence: [A](a: List[Option[A]])Option[List[A]]

  /*
  It can also be implemented using `foldRight` and `map2`. The type annotation on `foldRight` is needed here; otherwise
  Scala wrongly infers the result type of the fold as `Some[Nil.type]` and reports a type error (try it!). This is an
  unfortunate consequence of Scala using subtyping to encode algebraic data types.
  */

  def sequence_1[A](a: List[Option[A]]): Option[List[A]] =
    a.foldRight[Option[List[A]]](Some(Nil))((x, y) => map2(x, y)(_ :: _))
                                                  //> sequence_1: [A](a: List[Option[A]])Option[List[A]]

  def traverse[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] =
    a match {
      case Nil    => Some(Nil)
      case h :: t => map2(f(h), traverse(t)(f))(_ :: _)
    }                                             //> traverse: [A, B](a: List[A])(f: A => Option[B])Option[List[B]]


}