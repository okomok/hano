

// Copyright Shunsuke Sogame 2008-2009.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import junit.framework.Assert.{assertEquals, fail}
import junit.framework.AssertionFailedError


object AssertNotEquals {
    private def _apply(message: String, unexpected: AnyRef, actual: AnyRef) {
        val sb = new StringBuilder
        if (message ne null) {
            sb.append(message).append(' ')
        }

        val isEqual =
            try {
                assertEquals(unexpected, actual); true
            } catch {
                case _: AssertionFailedError => false
            }

        if (isEqual)
            fail(sb.
                append("unexpected:<").append(unexpected).append('>').
                append(" but was:<").append(actual).append('>').toString )
    }

    def apply(message: String, unexpected: Any, actual: Any) {
        _apply(message, unexpected.asInstanceOf[AnyRef], actual.asInstanceOf[AnyRef])
    }

    def apply(unexpected: Any, actual: Any) {
        apply(null, unexpected, actual)
    }
}
