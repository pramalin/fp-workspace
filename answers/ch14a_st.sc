object ch14a_st {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  import fpinscala.localeffects._

  val p = new RunnableST[(Int, Int)] {
    def apply[S] = for {
      r1 <- STRef(1)
      r2 <- STRef(2)
      x <- r1.read
      y <- r2.read
      _ <- r1.write(y + 1)
      _ <- r2.write(x + 1)
      a <- r1.read
      b <- r2.read
    } yield (a, b)
  }                                               //> p  : fpinscala.localeffects.RunnableST[(Int, Int)] = ch14a_st$$anon$1@27f674
                                                  //| d

  val r = ST.runST(p)                             //> r  : (Int, Int) = (3,2)
}