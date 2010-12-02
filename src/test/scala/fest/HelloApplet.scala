

// Copyright Shunsuke Sogame 2008-2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest
package festtest
package helloapplet

import javax.swing
import com.github.okomok.hano
import org.fest.{swing => fswing}
import org.testng.annotations._


class HelloApplet extends swing.JApplet {
    override def init {
        hano.eval.InEdt {
            add(new swing.JLabel("Hello World"))
        } apply
    }
}


class HelloAppletTest extends
    NotFestSuite
//    FestTestNGSuite
{
    private var viewer: fswing.applet.AppletViewer = null
    private var applet: fswing.fixture.FrameFixture = null

    @Test def testUntitled {
        // ...
    }

    override protected def onSetUp {
        viewer = fswing.launcher.AppletLauncher.applet(classOf[HelloApplet]).start()
        applet = new fswing.fixture.FrameFixture(robot, viewer)
        applet.show()
    }
}

