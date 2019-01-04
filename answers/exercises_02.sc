object exercises_02 {

  // 2.1
  // def fib(n: Int): Int
  import fpinscala.gettingstarted.MyModule._
  fib(10)                                         //> res0: Int = 55
  
  // 2.2
  // def isSorted[A](as: Array[A], ordered: (A,A) => Boolean): Boolean
  import fpinscala.gettingstarted.PolymorphicFunctions._
	isSorted(Array(1,2,3), (a: Int, b: Int) => a > b)
                                                  //> res1: Boolean = true
	isSorted(Array(2,1,3), (a: Int, b: Int) => a > b)
                                                  //> res2: Boolean = false

  // 2.3
  // def curry[A,B,C](f: (A, B) => C): A => (B => C)
  def addx(a: Int, b: Int) = a + b                //> addx: (a: Int, b: Int)Int
  addx(1,2)                                       //> res3: Int = 3
  def currx = curry(addx)                         //> currx: => Int => (Int => Int)
  currx(1)(2)                                     //> res4: Int = 3
  
  // 2.4
  // def uncurry[A,B,C](f: A => B => C): (A, B) => C
  def uncurrx = uncurry(currx)                    //> uncurrx: => (Int, Int) => Int
  uncurrx(2,3)                                    //> res5: Int = 5

  // 2.5
  // def compose[A,B,C](f: B => C, g: A => B): A => C
  def compx = compose((a: Int) => a + 1, (b: Int) => b * 2)
                                                  //> compx: => Int => Int
  // f(g(a))
  compx(3)                                        //> res6: Int = 7
 
}