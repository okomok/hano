

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object Beans {

// PropertyChangeEvent

    import java.beans.{PropertyChangeEvent, PropertyChangeListener}

    type PropertyChangeEventSource = {
        def addPropertyChangeListener(l: PropertyChangeListener)
        def removePropertyChangeListener(l: PropertyChangeListener)
    }

    case class PropertyChange(source: PropertyChangeEventSource) extends Swing.EdtListener[PropertyChangeEvent] {
        override protected def listen(env: Env) {
            val l = new PropertyChangeListener {
                override def propertyChange(e: PropertyChangeEvent) = env(e)
            }
            env.add { source.addPropertyChangeListener(l) }
            env.remove { source.removePropertyChangeListener(l) }
        }
    }

}
