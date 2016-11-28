package eu.unicredit

import scala.scalajs.js
import js.Dynamic.{global => g}

import akka.actor._
import scala.concurrent.duration._
import scala.util.Try

trait Helper {

  val fs = g.require("fs")

  def readCompiled(): String =
    fs.readFileSync("target/scala-2.12/root-fastopt.js", "utf8").toString

  def getBar(): String = {
    val code = readCompiled()
    val splitted = code.split("\n").toList
    val start =
      splitted
      .indexWhere{s: String => s.startsWith("$c_Leu_unicredit_Main$.prototype.bar__T")}

    val result = splitted.drop(start).take(5).mkString("\n").replace(
      "$c_Leu_unicredit_Main$.prototype",
      """global["eu"]["unicredit"]["Main"]()"""
    )

    //println("new code will be:\n"+result)

    result
  }

}
