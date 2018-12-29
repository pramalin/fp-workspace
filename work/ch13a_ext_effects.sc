object ch13a_ext_effects {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  case class Player(name: String, score: Int)

  val p1 = Player("player-1", 1)                  //> p1  : ch13a_ext_effects.Player = Player(player-1,1)
  val p2 = Player("player-2", 2)                  //> p2  : ch13a_ext_effects.Player = Player(player-2,2)

  def contest(p1: Player, p2: Player): Unit =
    if (p1.score > p2.score)
      println(s"${p1.name} is the winner!")
    else if (p2.score > p1.score)
      println(s"${p2.name} is the winner!")
    else
      println("It's a draw.")                     //> contest: (p1: ch13a_ext_effects.Player, p2: ch13a_ext_effects.Player)Unit

  contest(p1, p2)                                 //> player-2 is the winner!

  /*
The contest function couples the I/O code for displaying the result to the pure logic
for computing the winner. We can factor the logic into its own pure function, winner:
*/

  def winner(p1: Player, p2: Player): Option[Player] =
    if (p1.score > p2.score) Some(p1)
    else if (p1.score < p2.score) Some(p2)
    else None                                     //> winner: (p1: ch13a_ext_effects.Player, p2: ch13a_ext_effects.Player)Option[c
                                                  //| h13a_ext_effects.Player]

  def contest1(p1: Player, p2: Player): Unit = winner(p1, p2) match {
    case Some(Player(name, _)) => println(s"$name is the winner!")
    case None                  => println("It's a draw.")
  }                                               //> contest1: (p1: ch13a_ext_effects.Player, p2: ch13a_ext_effects.Player)Unit

  contest1(p1, p2)                                //> player-2 is the winner!

  /*
We can refactor this even further. The contest function still has two responsibilities:
it’s computing which message to display and then printing that message to the
console. We could factor out a pure function here as well, which might be beneficial if
we later decide to display the result in some sort of UI or write it to a file instead. Let’s
perform this refactoring now:
*/

  def winnerMsg(p: Option[Player]): String = p map {
    case Player(name, _) => s"$name is the winner!"
  } getOrElse "It's a draw."                      //> winnerMsg: (p: Option[ch13a_ext_effects.Player])String

  def contest2(p1: Player, p2: Player): Unit =
    println(winnerMsg(winner(p1, p2)))            //> contest2: (p1: ch13a_ext_effects.Player, p2: ch13a_ext_effects.Player)Unit
                                                  //| 

  contest2(p1, p2)                                //> player-2 is the winner!

  trait IO { def run: Unit }
  def PrintLine(msg: String): IO =
    new IO { def run = println(msg) }             //> PrintLine: (msg: String)ch13a_ext_effects.IO
  def contest3(p1: Player, p2: Player): IO =
    PrintLine(winnerMsg(winner(p1, p2)))          //> contest3: (p1: ch13a_ext_effects.Player, p2: ch13a_ext_effects.Player)ch13a
                                                  //| _ext_effects.IO

  contest3(p1, p2).run                            //> player-2 is the winner!
}