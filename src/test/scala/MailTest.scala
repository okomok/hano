

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class MailTest extends org.scalatest.junit.JUnit3Suite {

    def testNth {
        val xs = hano.async.pull(Seq(3,1,8,6,7,4,2,9))
        val nth = hano.Val(xs.nth(6).mail.nth(1))
        expect(2)(nth().element)
    }

    def testNoNth {
        val xs = hano.async.pull(Seq(3,1,8,6,7,4,2,9))
        val nth = new hano.Val[hano.Mail[Int]]
        nth := xs.nth(10).mail.nth(1)
        nth() match {
            case hano.ExitMail(hano.Exit.Failure(_)) => ()
            case _ => fail("doh")
        }
    }
}
