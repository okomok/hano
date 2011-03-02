

// Copyright Shunsuke Sogame 2008-2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest
package festtest
package helloapplet

import javax.swing
import com.github.okomok.hano
import org.fest.{swing => fswing}
import org.testng.annotations._
import java.awt.image.ImageObserver._


class HelloApplet extends swing.JApplet {
    private var image: java.awt.Image = null
    private var loaded = false

    override def init {
        /*
        val tk = java.awt.Toolkit.getDefaultToolkit
        image = tk.getImage("BLUE.jpg")
        assert(image != null)
        val xs = hano.Swing.ImageUpdate(image, tk.prepareImage(image, -1, -1, _))
        for (x <- xs) {
            val infoflags = x.flags
            if ((infoflags & ERROR) != 0) {
                println("ERRORED")
            } else if ((infoflags & ABORT) != 0) {
                println("ABORTED")
            } else if ((infoflags & (ALLBITS | FRAMEBITS)) != 0) {
                println("COMPLETE")
            } else if ((infoflags & SOMEBITS) != 0) {
                println("SOMEBITS")
            }

            if ((x.flags & ALLBITS|FRAMEBITS) != 0) {
                loaded = true
                repaint() // BTW, thead-safe
                xs.close()
            }
        }
        */
    }

    override def paint(g: java.awt.Graphics) {
        if (loaded) {
            g.drawImage(image, 0, 0, this)
        }
    }
    override def update(g: java.awt.Graphics) {
        paint(g)
    }
}


class HelloAppletTest extends
    NotFestSuite
//    FestTestNGSuite
{
    private var viewer: fswing.applet.AppletViewer = null
    private var applet: fswing.fixture.FrameFixture = null

    @Test def testUntitled {
        Thread.sleep(10000)
        // ...
    }

    override protected def onSetUp {
        viewer = fswing.launcher.AppletLauncher.applet(classOf[HelloApplet]).start()
        applet = new fswing.fixture.FrameFixture(robot, viewer)
        applet.show()
    }
}

