

// Copyright Shunsuke Sogame 2008-2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


trait Category {
    def traversal: Category.Traversal
    def elementAccess: Category.ElementAccess
    def evaluation: Category.Evaluation

    final def <:<(that: Category.Traversal): Boolean = traversal <:< that
    final def <:<(that: Category.ElementAccess): Boolean = elementAccess <:< that
    final def <:<(that: Category.Evaluation): Boolean = evaluation <:< that

    final def isReactive: Boolean = true
    final def isIterable: Boolean = this <:< Category.Iterable
    final def isRandomAccess: Boolean = this <:< Category.RandomAccess
    final def isView: Boolean = this <:< Category.View
    final def isWritable: Boolean = this <:< Category.Writable
}


object Category {

    def apply(t: Traversal, a: ElementAccess = Readable, e: Evaluation = View) = new Category {
        override def traversal = t
        override def elementAccess = a
        override def evaluation = e
    }


// Traversal

    sealed abstract class Traversal {
        protected def order: Int
        final def <:<(that: Traversal): Boolean = order <= that.order
    }

    sealed abstract class Reactive extends Traversal {
        override protected def order = 0
    }
    val Reactive = new Reactive{}

    sealed abstract class Iterable extends Traversal {
        override protected def order = -1
    }
    val Iterable = new Iterable{}

    sealed abstract class RandomAccess extends Traversal {
        override protected def order = -2
    }
    val RandomAccess = new RandomAccess{}


// ElementAccess

    sealed abstract class ElementAccess {
        def <:<(that: ElementAccess): Boolean
    }

    sealed abstract class Readable extends ElementAccess {
        override def <:<(that: ElementAccess) = (that eq this) || (that eq ReadWrite)
    }
    val Readable = new Readable{}

    sealed abstract class Writable extends ElementAccess {
        override def <:<(that: ElementAccess) = (that eq this) || (that eq ReadWrite)
    }
    val Writable = new Writable{}

    sealed abstract class ReadWrite extends ElementAccess {
        override def <:<(that: ElementAccess) = that eq this
    }
    val ReadWrite = new ReadWrite{}


// Evaluation

    sealed abstract class Evaluation {
        final def <:<(that: Evaluation): Boolean = this eq that
    }

    sealed abstract class View extends Evaluation
    val View = new View{}

    sealed abstract class Strict extends Evaluation
    val Strict = new Strict{}


    class RequiresException(msg: String) extends RuntimeException(msg)
    class RequiresReactiveException(msg: String) extends RequiresException(msg)
    class RequiresIterableException(msg: String) extends RequiresReactiveException(msg)
    class RequiresRandomAccessException(msg: String) extends RequiresIterableException(msg)

}


/*
case class Category(traversal: Category.Traversal,
    elementAccess: Category.ElementAccess, evaluation: Category.Evaluation)

    Categery(CategoryReactive(), _, _)
*/

