

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object annotation {


    /**
     * Contains implicit conversions.
     */
    class compatibles extends StaticAnnotation

    /**
     * An explicit conversion
     */
    class conversion extends StaticAnnotation

    /**
     * A lightweight conversion
     */
    class compatibleConversion extends conversion


    /**
     * Implementation detail. Don't use the name.
     */
    class detail extends StaticAnnotation


    /**
     * An equivalent expression
     */
    class equivalentTo(expr: String) extends StaticAnnotation

    /**
     * An alias of the expression
     */
    class aliasOf(expr: String) extends equivalentTo(expr)


    /**
     * Thread-safe.
     */
    class threadSafe extends StaticAnnotation

    /**
     * Not thread-safe.
     */
    class notThreadSafe extends StaticAnnotation


    /**
     * Overrides only for optimization.
     */
    class optimize extends StaticAnnotation


    /**
     * Not override but overload.
     */
    class overload extends StaticAnnotation


    /**
     * Pre
     */
    class pre(expr: String) extends StaticAnnotation

    /**
     * Postcondition
     */
    class post(expr: String) extends StaticAnnotation


    /**
     * Returns <code>this</code>.
     */
    class returnThis extends StaticAnnotation

    /**
     * Returns the passed argument as is. (Useful to trigger implicit conversion "explicitly".)
     */
    class returnThat extends StaticAnnotation


    /**
     * An annotation that designates the definition
     * to which it is used to associate type with value.
     */
    class specializer extends StaticAnnotation


    /**
     * Marks an unfixable compiler bug.
     */
    class compilerBug(version: String) extends StaticAnnotation

    /**
     * An annotation that designates the definition
     * to which it is used to work around a compiler bug.
     */
    class compilerWorkaround(version: String) extends StaticAnnotation


    /**
     * Visible only for testing; don't touch this.
     */
    class visibleForTesting extends StaticAnnotation


    /**
     * Mixin trait; should not be used as parameter types.
     */
    class mixin extends StaticAnnotation


    /**
     * Marks as an idempotent method.
     */
    class idempotent extends StaticAnnotation


    /**
     * Describes method time-complexity.
     */
    sealed trait TimeComplexity extends StaticAnnotation

    class constantTime extends TimeComplexity
    class logarithmicTime extends TimeComplexity
    class linearTime extends TimeComplexity
    class polynomialTime extends TimeComplexity
    class exponentialTime extends TimeComplexity
    class factorialTime extends TimeComplexity

}
