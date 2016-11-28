package eu.unicredit

import scala.scalajs.js
import js.Dynamic.{global => g}

import akka.actor._
import scala.concurrent.duration._
import scala.util.Try

object Main extends js.JSApp with Helper {

  var x = 1

  def bar() = {
    val ret = "recompile "+x
    x += 1
    ret
  }

  def main() = {

    val system = ActorSystem("node-server")

    val act = system.actorOf(Props(new Actor{
      def receive = {
        case str: String =>
          println(s"-> ${bar()} $str")
      }
    }))

    import system.dispatcher
    system.scheduler.schedule(2 seconds, 2 seconds)(
      act ! "ciao"
    )

    var last: String = readCompiled()

    system.scheduler.schedule(1 seconds, 1 seconds)(
      Try {
        val newComp = readCompiled()
        if (newComp.size > 0 && newComp != last) {
          last = newComp
          g.eval(getBar())
        }
      }
    )
  }

}
