

// Copyright Shunsuke Sogame 2008-2009.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest
package armtest


    import com.github.okomok.hano
    import java.nio.channels
    import java.nio.channels.Channels

    class DocTezt { // extends org.scalatest.junit.JUnit3Suite {
        def testTrivial {
            hano.block {
                val source = hano.use(Channels.newChannel(System.in)).!
                val dest = hano.use(Channels.newChannel(System.out)).!
                channelCopy(source, dest)
            }
        }

        def channelCopy(src: channels.ReadableByteChannel, dest: channels.WritableByteChannel) {
            // exercise.
        }
    }
