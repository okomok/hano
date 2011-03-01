
package com.github.okomok.hanotest.example
import com.github.okomok.hano

/**
 * `listen` can convert a listener to sequence.
 */
class ListenTest extends org.scalatest.junit.JUnit3Suite {

    import java.awt.event.MouseEvent
    import javax.swing.event.MouseInputAdapter

    /**
     * Creates a sequence of mouse clicks.
     */
    def clicks(source: java.awt.Component): hano.Seq[MouseEvent] = {
        hano.listen[MouseEvent]() { * =>
            val l = new MouseInputAdapter {
                override def mouseClicked(e: MouseEvent) = *(e)
            }
            *.add {
                source.addMouseListener(l)
            }
            *.remove {
                source.removeMouseListener(l)
            }
        }
    }

    /**
     * Fortunately `hano.Swing` has already defined swing event sequences.
     */
    def test_{}
}
