

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/*
import org.jboss.netty.channel.{NChannelEvent, NChannelPipeLine, NChannelHandlerContext, NChannelStateEvent}
import org.jboss.netty.bootstrap.ClientBootstrap


object Netty {

    private[hano]
    trait ChannelResource[A] extends NoExitResource[A] {
        final override def context = ???
    }

    trait ChannelHandlerEvent {
        def context: NChannelHandlerContext
        def event: NChannelEvent
    }

    case class ChannelUpstream(pipeline: NChannelPipeline) extends ChannelResource[Pair[NChannelHandlerContext, NChannelEvent]] {
        private[this] var l: NChannelUpstreamHandler = null
        override protected def closeResource() = pipeline.remove(l)
        override protected def openResource(f: Pair[NChannelHandlerContext, NChannelEvent] => Unit) {
            l = new ChannelUpstreamHandler {
                override def handleUpstream(ctx: NChannelHandlerContext, e: NChannelEvent) = f(ctx, e)
            }
            pipeline.addLast("", l)
        }
    }

    case class ChannelSimple


    class ListenerToSeq[A, Listener <: AnyRef](listener: Listener, add: L => Unit, remove: L => Unit) extends Seq[A] {
        private[this] var l: L = null
        override protected def closeResource() = pipeline.remove(l)
        override protected def openResource(f: Pair[NChannelHandlerContext, NChannelEvent] => Unit) {
            l = new ChannelUpstreamHandler {
                override def handleUpstream(ctx: NChannelHandlerContext, e: NChannelEvent) = f(ctx, e)
            }
            pipeline.addLast("", l)
        }
    }

}
*/
