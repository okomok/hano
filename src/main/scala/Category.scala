

// Copyright Shunsuke Sogame 2008-2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


sealed abstract class Category


object Category {

    abstract class Reactive extends Category

    abstract class SinglePass extends Reactive

    abstract class Iterable extends SinglePass

    abstract class RandomAccess extends Iterable

}
