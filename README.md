scheme-scala
============

Working through [Write Yourself a Scheme in 48 Hours][https://en.wikibooks.org/wiki/Write_Yourself_a_Scheme_in_48_Hours] using scala.

The Plan
========

The plan is simple, follow the text of [Write Yourself a Scheme in 48 Hours][https://en.wikibooks.org/wiki/Write_Yourself_a_Scheme_in_48_Hours], but write it in [Scala][http://www.scala-lang.org/]. This was conceived by rmogstad and I as a means to learn some Scala.

Testing
=======

The text doesn't make use of testing, but we both practice TDD in our real jobs. So we'll be writing some tests as we go and try to get a handle on TDD in Scala as part of this process.

Running
=======

At this point, I've started this using [SBT][https://github.com/sbt/sbt] to control the build dependencies and such. There are many choices for this and I just picked one. So, if you have sbt installed, things should "just work" with

    sbt run

Right now, the code expects(not really) to receive a scheme expression as an argument, so

    sbt run "<expr>"

might be in order, but don't expect any results for a while...

