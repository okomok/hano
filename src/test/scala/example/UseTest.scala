
package com.github.okomok.hanotest.example
import com.github.okomok.hano

/**
 * `use` returns a single-element sequence whose element is its argument.
 * Its reaction is wrapped by "try-finally-close" so that resource management is automatic.
 */
class UseTest extends org.scalatest.junit.JUnit3Suite {

    import java.nio.channels
    import java.nio.channels.Channels

    def usingResource {
        for {
            source <- hano.use(Channels.newChannel(System.in))
            dest <- hano.use(Channels.newChannel(System.out))
        } {
            channelCopy(source, dest)
        }
    }

    def channelCopy(src: channels.ReadableByteChannel, dest: channels.WritableByteChannel) {
        // exercise.
    }

    def test_ {}
}
