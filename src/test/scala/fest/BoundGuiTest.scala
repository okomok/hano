

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest
package festtest
package boundtest


import org.testng.annotations._
import org.fest.swing.fixture.FrameFixture
import org.fest.swing.core.ComponentDragAndDrop

import javax.swing
import java.awt.{Color, BorderLayout}
import com.github.okomok.hano
import hano.{Swing, Beans}
import java.lang.Math
import FestUtil.textAsName


class BoundGuiTest extends
    NotFestSuite
//    FestTestNGSuite
{
    private var fx: FrameFixture = null

    override protected def onSetUp {
        val f = hano.future.inEdt {
            val f = new swing.JFrame("Button Sample")
            val b1 = new swing.JButton("Select Me").textAsName
            val b2 = new swing.JButton("No Select Me").textAsName

            for (e <- Swing.ActionPerformed(b1) merge Swing.ActionPerformed(b2)) {
                val s = e.getSource.asInstanceOf[swing.JButton]
                val r = Math.random.asInstanceOf[Float]
                val g = Math.random.asInstanceOf[Float]
                val b = Math.random.asInstanceOf[Float]
                s.setBackground(new Color(r, g, b))
            }

            for (e <- Beans.PropertyChange(b1)) {
                if (e.getPropertyName == "background") {
                    b2.setBackground(e.getNewValue.asInstanceOf[Color])
                }
            }

            f.getContentPane
            f.add(b1, BorderLayout.NORTH)
            f.add(b2, BorderLayout.SOUTH)
            f.setSize(300, 100)
            f.setVisible(true)
            f
        }

        fx = new FrameFixture(robot, f())
        fx.show()
    }

    @Test def testTrivial {
        for (_ <- 0 until 5) {
            fx.button("Select Me").click
            val bk1 = fx.button("Select Me").background
            bk1.requireEqualTo(bk1.target)
            fx.button("No Select Me").background.requireEqualTo(bk1.target)
        }
    }
}