

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.madatest
package sequencetest.reactivetest.festtest.firstguitest


import org.testng.annotations._
import org.fest.swing.edt
import org.fest.swing.fixture.FrameFixture
import javax.swing
import com.github.okomok.hano.sequence.reactive


/*
class MyFrame extends swing.JFrame {
    val panel = new swing.JPanel
    panel.setLayout(new swing.BoxLayout(panel, swing.BoxLayout.PAGE_AXIS))
    val c1 = new swing.JTextField
    c1.setName("textToCopy")
    val c2 = new swing.JButton("CopyText To Label")
    c2.setName("copyButton")
    val c3 = new swing.JLabel("CopedText will be here")
    c3.setName("copiedText")
    panel.add(c1)
    panel.add(c2)
    panel.add(c3)
    getContentPane.add(panel)
    setDefaultCloseOperation(swing.JFrame.EXIT_ON_CLOSE)

    val rx = hano.Swing.ActionPerformed(c2)
    hano.block {
        val _ = rx.each
        c3.setText(c1.getText)
    }
}


class FirstGUITest extends org.scalatest.testng.TestNGSuite {

    private var window: FrameFixture = null

    @BeforeClass
    def setUpOnce {
        edt.FailOnThreadViolationRepaintManager.install();
    }

    @BeforeMethod
    def setUp {
        val frame: MyFrame = edt.GuiActionRunner.execute(new edt.GuiQuery[MyFrame] {
            override protected def executeInEDT = new MyFrame()
        })
        window = new FrameFixture(frame)
        window.show() // shows the frame to test
    }

    @AfterMethod
    def tearDown {
        window.cleanUp()
    }

    @Test
    def shouldCopyTextInLabelWhenClickingButton {
        window.textBox("textToCopy").enterText("Some random text")
        window.button("copyButton").click()
        window.label("copiedText").requireText("Some random text")
    }
}
*/