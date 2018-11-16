

object ActorApp extends App {
  object actor {
    import fpinscala.parallelism._
    import java.util.concurrent.Executors

    val S = Executors.newFixedThreadPool(4)

    val echoer = Actor[String](S) {
      msg => println(s"Got message: '$msg'")
    }

    echoer ! "hello"

  }
}