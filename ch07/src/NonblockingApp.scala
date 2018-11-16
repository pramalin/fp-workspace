

object NonblockingApp extends App {
  import java.util.concurrent.Executors
  import fpinscala.parallelism.Nonblocking.Par._
 
 
   val p = parMap(List.range(1, 100000))(math.sqrt(_))
   val x = run(Executors.newFixedThreadPool(2))(p)
}