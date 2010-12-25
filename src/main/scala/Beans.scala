

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

    case class PropertyChange(source: PropertyChangeEventSource) extends NoExitResource[PropertyChangeEvent] {
        private[this] var l: PropertyChangeListener = null
        override protected def closeResource() = source.removePropertyChangeListener(l)
        override protected def openResource(f: PropertyChangeEvent => Unit) {
            l = new PropertyChangeListener {
                override def propertyChange(e: PropertyChangeEvent) = f(e)
            }
            source.addPropertyChangeListener(l)
        }
    }

}
