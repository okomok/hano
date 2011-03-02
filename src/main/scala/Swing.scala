

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.awt.{Component => AComponent, Container => AContainer}
import javax.swing.JComponent


object Swing {


    private[hano]
    trait EdtListener[A] extends listen.To[A] {
        final override def context = Edt
    }


// ActionEvent

    import java.awt.event.{ActionEvent, ActionListener}

    type ActionEventSource = {
        def addActionListener(l: ActionListener)
        def removeActionListener(l: ActionListener)
    }

    case class ActionPerformed(source: ActionEventSource) extends EdtListener[ActionEvent] {
        override protected def listen(env: Env) {
            val l = new ActionListener {
                override def actionPerformed(e: ActionEvent) = env(e)
            }
            env.add { source.addActionListener(l) }
            env.remove { source.removeActionListener(l) }
        }
    }


// AncestorEvent

    import javax.swing.event.{AncestorEvent, AncestorListener}

    private class AncestorAdapter extends AncestorListener {
        override def ancestorAdded(e: AncestorEvent) = ()
        override def ancestorMoved(e: AncestorEvent) = ()
        override def ancestorRemoved(e: AncestorEvent) = ()
    }

    case class Ancestor(source: JComponent) {

        private class Event extends EdtListener[AncestorEvent] {
            override protected def listen(env: Env) {
                val l = new AncestorAdapter {
                    override def ancestorAdded(e: AncestorEvent) = env(e)
                    override def ancestorMoved(e: AncestorEvent) = env(e)
                    override def ancestorRemoved(e: AncestorEvent) = env(e)
                }
                env.add { source.addAncestorListener(l) }
                env.remove { source.removeAncestorListener(l) }
            }
        }
        def Event: Seq[AncestorEvent] = new Event

        private class Added extends EdtListener[AncestorEvent] {
            override protected def listen(env: Env) {
                val l = new AncestorAdapter {
                    override def ancestorAdded(e: AncestorEvent) = env(e)
                }
                env.add { source.addAncestorListener(l) }
                env.remove { source.removeAncestorListener(l) }
            }
        }
        def Added: Seq[AncestorEvent] = new Added

        private class Moved extends EdtListener[AncestorEvent] {
            override protected def listen(env: Env) {
                val l = new AncestorAdapter {
                    override def ancestorMoved(e: AncestorEvent) = env(e)
                }
                env.add { source.addAncestorListener(l) }
                env.remove { source.removeAncestorListener(l) }
            }
        }
        def Moved: Seq[AncestorEvent] = new Moved

        private class Removed extends EdtListener[AncestorEvent] {
            override protected def listen(env: Env) {
                val l = new AncestorAdapter {
                    override def ancestorRemoved(e: AncestorEvent) = env(e)
                }
                env.add { source.addAncestorListener(l) }
                env.remove { source.removeAncestorListener(l) }
            }
        }
        def Removed: Seq[AncestorEvent] = new Removed

    }


// ChangeEvent

    import javax.swing.event.{ChangeEvent, ChangeListener}

    type ChangeEventSource = {
        def addChangeListener(l: ChangeListener)
        def removeChangeListener(l: ChangeListener)
    }

    case class StateChanged(source: ChangeEventSource) extends EdtListener[ChangeEvent] {
        override protected def listen(env: Env) {
            val l = new ChangeListener {
                override def stateChanged(e: ChangeEvent) = env(e)
            }
            env.add { source.addChangeListener(l) }
            env.remove { source.removeChangeListener(l) }
        }
    }


// ComponentEvent

    import java.awt.event.{ComponentEvent, ComponentAdapter}

    case class Component(source: AComponent) {

        private class Event extends EdtListener[ComponentEvent] {
            override protected def listen(env: Env) {
                val l = new ComponentAdapter {
                    override def componentHidden(e: ComponentEvent) = env(e)
                    override def componentMoved(e: ComponentEvent) = env(e)
                    override def componentResized(e: ComponentEvent) = env(e)
                    override def componentShown(e: ComponentEvent) = env(e)
                }
                env.add { source.addComponentListener(l) }
                env.remove { source.removeComponentListener(l) }
            }
        }
        def Event: Seq[ComponentEvent] = new Event

        private class Hidden extends EdtListener[ComponentEvent] {
            override protected def listen(env: Env) {
                val l = new ComponentAdapter {
                    override def componentHidden(e: ComponentEvent) = env(e)
                }
                env.add { source.addComponentListener(l) }
                env.remove { source.removeComponentListener(l) }
            }
        }
        def Hidden: Seq[ComponentEvent] = new Hidden

        private class Moved extends EdtListener[ComponentEvent] {
            override protected def listen(env: Env) {
                val l = new ComponentAdapter {
                    override def componentMoved(e: ComponentEvent) = env(e)
                }
                env.add { source.addComponentListener(l) }
                env.remove { source.removeComponentListener(l) }
            }
        }
        def Moved: Seq[ComponentEvent] = new Moved

        private class Resized extends EdtListener[ComponentEvent] {
            override protected def listen(env: Env) {
                val l = new ComponentAdapter {
                    override def componentResized(e: ComponentEvent) = env(e)
                }
                env.add { source.addComponentListener(l) }
                env.remove { source.removeComponentListener(l) }
            }
        }
        def Resized: Seq[ComponentEvent] = new Resized

        private class Shown extends EdtListener[ComponentEvent] {
            override protected def listen(env: Env) {
                val l = new ComponentAdapter {
                    override def componentShown(e: ComponentEvent) = env(e)
                }
                env.add { source.addComponentListener(l) }
                env.remove { source.removeComponentListener(l) }
            }
        }
        def Shown: Seq[ComponentEvent] = new Shown
    }


// ContainerEvent

    import java.awt.event.{ContainerEvent, ContainerAdapter}

    case class Container(source: AContainer) {

        private class Event extends EdtListener[ContainerEvent] {
            override protected def listen(env: Env) {
                val l = new ContainerAdapter {
                    override def componentAdded(e: ContainerEvent) = env(e)
                    override def componentRemoved(e: ContainerEvent) = env(e)
                }
                env.add { source.addContainerListener(l) }
                env.remove { source.removeContainerListener(l) }
            }
        }
        def Event: Seq[ContainerEvent] = new Event

        private class Added extends EdtListener[ContainerEvent] {
            override protected def listen(env: Env) {
                val l = new ContainerAdapter {
                    override def componentAdded(e: ContainerEvent) = env(e)
                }
                env.add { source.addContainerListener(l) }
                env.remove { source.removeContainerListener(l) }
            }
        }
        def Added: Seq[ContainerEvent] = new Added

        private class Removed extends EdtListener[ContainerEvent] {
            override protected def listen(env: Env) {
                val l = new ContainerAdapter {
                    override def componentRemoved(e: ContainerEvent) = env(e)
                }
                env.add { source.addContainerListener(l) }
                env.remove { source.removeContainerListener(l) }
            }
        }
        def Removed: Seq[ContainerEvent] = new Removed

    }


// FocusEvent

    import java.awt.event.{FocusEvent, FocusAdapter}

    case class Focus(source: AComponent) {

        private class Event extends EdtListener[FocusEvent] {
            override protected def listen(env: Env) {
                val l = new FocusAdapter {
                    override def focusGained(e: FocusEvent) = env(e)
                    override def focusLost(e: FocusEvent) = env(e)
                }
                env.add { source.addFocusListener(l) }
                env.remove { source.removeFocusListener(l) }
            }
        }
        def Event: Seq[FocusEvent] = new Event

        private class Gained extends EdtListener[FocusEvent] {
            override protected def listen(env: Env) {
                val l = new FocusAdapter {
                    override def focusGained(e: FocusEvent) = env(e)
                }
                env.add { source.addFocusListener(l) }
                env.remove { source.removeFocusListener(l) }
            }
        }
        def Gained: Seq[FocusEvent] = new Gained

        private class Lost extends EdtListener[FocusEvent] {
            override protected def listen(env: Env) {
                val l = new FocusAdapter {
                    override def focusLost(e: FocusEvent) = env(e)
                }
                env.add { source.addFocusListener(l) }
                env.remove { source.removeFocusListener(l) }
            }
        }
        def Lost: Seq[FocusEvent] = new Lost

    }


// HierarchyEvent

    import java.awt.event.{HierarchyEvent, HierarchyListener, HierarchyBoundsAdapter}

    case class Hierarchy(source: AComponent) {

        private class Changed extends EdtListener[HierarchyEvent] {
            override protected def listen(env: Env) {
                val l = new HierarchyListener {
                    override def hierarchyChanged(e: HierarchyEvent) = env(e)
                }
                env.add { source.addHierarchyListener(l) }
                env.remove { source.removeHierarchyListener(l) }
            }
        }
        def Changed: Seq[HierarchyEvent] = new Changed

        private class Bounds extends EdtListener[HierarchyEvent] {
            override protected def listen(env: Env) {
                val l = new HierarchyBoundsAdapter {
                    override def ancestorMoved(e: HierarchyEvent) = env(e)
                    override def ancestorResized(e: HierarchyEvent) = env(e)
                }
                env.add { source.addHierarchyBoundsListener(l) }
                env.remove { source.removeHierarchyBoundsListener(l) }
            }
        }
        def Bounds: Seq[HierarchyEvent] = new Bounds

        private class Moved extends EdtListener[HierarchyEvent] {
            override protected def listen(env: Env) {
                val l = new HierarchyBoundsAdapter {
                    override def ancestorMoved(e: HierarchyEvent) = env(e)
                }
                env.add { source.addHierarchyBoundsListener(l) }
                env.remove { source.removeHierarchyBoundsListener(l) }
            }
        }
        def Moved: Seq[HierarchyEvent] = new Moved

        private class Resized extends EdtListener[HierarchyEvent] {
            override protected def listen(env: Env) {
                val l = new HierarchyBoundsAdapter {
                    override def ancestorResized(e: HierarchyEvent) = env(e)
                }
                env.add { source.addHierarchyBoundsListener(l) }
                env.remove { source.removeHierarchyBoundsListener(l) }
            }
        }
        def Resized: Seq[HierarchyEvent] = new Resized

    }


/* ImageUpdate

    ImageObserver seems completely useless.

    import java.awt.Image
    import java.awt.image.ImageObserver

    case class ImageUpdateInfo(image: Image, flags: Int, x: Int, y: Int, width: Int, height: Int)

    case class ImageUpdate(image: Image, subscribe: ImageObserver => Boolean) extends EdtListener[ImageUpdateInfo] {
        private[this] var l: ImageObserver = null
        @volatile private[this] var go = true
        override protected def closeResource() = go = false
        override protected def listen(env: Env) {
            val l = new ImageObserver {
                override def imageUpdate(img: Image, flags: Int, x: Int, y: Int, width: Int, height: Int): Boolean = {
                    if (go) {
                        f(ImageUpdateInfo(img, flags, x, y, width, height))
                    }
                    go
                }
            }
            if (subscribe(l)) {
                f(ImageUpdateInfo(image, ImageObserver.ALLBITS, 0, 0, 0, 0))
            }
        }
    }
*/


// InputMethodEvent

    import java.awt.event.{InputMethodEvent, InputMethodListener}

    private class InputMethodAdapter extends InputMethodListener {
        override def caretPositionChanged(e: InputMethodEvent) = ()
        override def inputMethodTextChanged(e: InputMethodEvent) = ()
    }

    case class InputMethod(source: AComponent) {

        private class Event extends EdtListener[InputMethodEvent] {
            override protected def listen(env: Env) {
                val l = new InputMethodAdapter {
                        override def caretPositionChanged(e: InputMethodEvent) = env(e)
                        override def inputMethodTextChanged(e: InputMethodEvent) = env(e)
                }
                env.add { source.addInputMethodListener(l) }
                env.remove { source.removeInputMethodListener(l) }
            }
        }
        def Event: Seq[InputMethodEvent] = new Event

        private class CaretPositionChanged extends EdtListener[InputMethodEvent] {
            override protected def listen(env: Env) {
                val l = new InputMethodAdapter {
                    override def caretPositionChanged(e: InputMethodEvent) = env(e)
                }
                env.add { source.addInputMethodListener(l) }
                env.remove { source.removeInputMethodListener(l) }
            }
        }
        def CaretPositionChanged: Seq[InputMethodEvent] = new CaretPositionChanged

        private class TextChanged extends EdtListener[InputMethodEvent] {
            override protected def listen(env: Env) {
                val l = new InputMethodAdapter {
                    override def inputMethodTextChanged(e: InputMethodEvent) = env(e)
                }
                env.add { source.addInputMethodListener(l) }
                env.remove { source.removeInputMethodListener(l) }
            }
        }
        def TextChanged: Seq[InputMethodEvent] = new TextChanged

    }


// ItemEvent

    import java.awt.event.{ItemEvent, ItemListener}

    type ItemEventSource = {
        def addItemListener(l: ItemListener)
        def removeItemListener(l: ItemListener)
    }

    case class ItemStateChanged(source: ItemEventSource) extends EdtListener[ItemEvent] {
        override protected def listen(env: Env) {
            val l = new ItemListener {
                override def itemStateChanged(e: ItemEvent) = env(e)
            }
            env.add { source.addItemListener(l) }
            env.remove { source.removeItemListener(l) }
        }
    }


// KeyEvent

    import java.awt.event.{KeyEvent, KeyAdapter}

    case class Key(source: AComponent) {

        private class Event extends EdtListener[KeyEvent] {
            override protected def listen(env: Env) {
                val l = new KeyAdapter {
                    override def keyPressed(e: KeyEvent) = env(e)
                    override def keyReleased(e: KeyEvent) = env(e)
                    override def keyTyped(e: KeyEvent) = env(e)
                }
                env.add { source.addKeyListener(l) }
                env.remove { source.removeKeyListener(l) }
            }
        }
        def Event: Seq[KeyEvent] = new Event

        private class Pressed extends EdtListener[KeyEvent] {
            override protected def listen(env: Env) {
                val l = new KeyAdapter {
                    override def keyPressed(e: KeyEvent) = env(e)
                }
                env.add { source.addKeyListener(l) }
                env.remove { source.removeKeyListener(l) }
            }
        }
        def Pressed: Seq[KeyEvent] = new Pressed

        private class Released extends EdtListener[KeyEvent] {
            override protected def listen(env: Env) {
                val l = new KeyAdapter {
                    override def keyReleased(e: KeyEvent) = env(e)
                }
                env.add { source.addKeyListener(l) }
                env.remove { source.removeKeyListener(l) }
            }
        }
        def Released: Seq[KeyEvent] = new Released

        private class Typed extends EdtListener[KeyEvent] {
            override protected def listen(env: Env) {
                val l = new KeyAdapter {
                    override def keyTyped(e: KeyEvent) = env(e)
                }
                env.add { source.addKeyListener(l) }
                env.remove { source.removeKeyListener(l) }
            }
        }
        def Typed: Seq[KeyEvent] = new Typed

    }


// MenuEvent

    import javax.swing.event.{MenuEvent, MenuListener}

    private class MenuAdapter extends MenuListener {
        override def menuCanceled(e: MenuEvent) = ()
        override def menuDeselected(e: MenuEvent) = ()
        override def menuSelected(e: MenuEvent) = ()
    }

    type MenuEventSource = {
        def addMenuListener(l: MenuListener)
        def removeMenuListener(l: MenuListener)
    }

    case class Menu(source: MenuEventSource) {

        private class Event extends EdtListener[MenuEvent] {
            override protected def listen(env: Env) {
                val l = new MenuAdapter {
                    override def menuCanceled(e: MenuEvent) = env(e)
                    override def menuDeselected(e: MenuEvent) = env(e)
                    override def menuSelected(e: MenuEvent) = env(e)
                }
                env.add { source.addMenuListener(l) }
                env.remove { source.removeMenuListener(l) }
            }
        }
        def Event: Seq[MenuEvent] = new Event

        private class Canceled extends EdtListener[MenuEvent] {
            override protected def listen(env: Env) {
                val l = new MenuAdapter {
                    override def menuCanceled(e: MenuEvent) = env(e)
                }
                env.add { source.addMenuListener(l) }
                env.remove { source.removeMenuListener(l) }
            }
        }
        def Canceled: Seq[MenuEvent] = new Canceled

        private class Deselected extends EdtListener[MenuEvent] {
            override protected def listen(env: Env) {
                val l = new MenuAdapter {
                    override def menuDeselected(e: MenuEvent) = env(e)
                }
                env.add { source.addMenuListener(l) }
                env.remove { source.removeMenuListener(l) }
            }
        }
        def Deselected: Seq[MenuEvent] = new Deselected

        private class Selected extends EdtListener[MenuEvent] {
            override protected def listen(env: Env) {
                val l = new MenuAdapter {
                    override def menuSelected(e: MenuEvent) = env(e)
                }
                env.add { source.addMenuListener(l) }
                env.remove { source.removeMenuListener(l) }
            }
        }
        def Selected: Seq[MenuEvent] = new Selected

    }


// MenuDragMouseEvent

    import javax.swing.event.{MenuDragMouseEvent, MenuDragMouseListener}

    private class MenuDragMouseAdapter extends MenuDragMouseListener {
        override def menuDragMouseDragged(e: MenuDragMouseEvent) = ()
        override def menuDragMouseEntered(e: MenuDragMouseEvent) = ()
        override def menuDragMouseExited(e: MenuDragMouseEvent) = ()
        override def menuDragMouseReleased(e: MenuDragMouseEvent) = ()
    }

    type MenuDragMouseEventSource = {
        def addMenuDragMouseListener(l: MenuDragMouseListener)
        def removeMenuDragMouseListener(l: MenuDragMouseListener)
    }

    case class MenuDragMouse(source: MenuDragMouseEventSource) {

        private class Event extends EdtListener[MenuDragMouseEvent] {
            override protected def listen(env: Env) {
                val l = new MenuDragMouseAdapter {
                    override def menuDragMouseDragged(e: MenuDragMouseEvent) = env(e)
                    override def menuDragMouseEntered(e: MenuDragMouseEvent) = env(e)
                    override def menuDragMouseExited(e: MenuDragMouseEvent) = env(e)
                    override def menuDragMouseReleased(e: MenuDragMouseEvent) = env(e)
                }
                env.add { source.addMenuDragMouseListener(l) }
                env.remove { source.removeMenuDragMouseListener(l) }
            }
        }
        def Event: Seq[MenuDragMouseEvent] = new Event

        private class Dragged extends EdtListener[MenuDragMouseEvent] {
            override protected def listen(env: Env) {
                val l = new MenuDragMouseAdapter {
                    override def menuDragMouseDragged(e: MenuDragMouseEvent) = env(e)
                }
                env.add { source.addMenuDragMouseListener(l) }
                env.remove { source.removeMenuDragMouseListener(l) }
            }
        }
        def Dragged: Seq[MenuDragMouseEvent] = new Dragged

        private class Entered extends EdtListener[MenuDragMouseEvent] {
            override protected def listen(env: Env) {
                val l = new MenuDragMouseAdapter {
                    override def menuDragMouseEntered(e: MenuDragMouseEvent) = env(e)
                }
                env.add { source.addMenuDragMouseListener(l) }
                env.remove { source.removeMenuDragMouseListener(l) }
            }
        }
        def Entered: Seq[MenuDragMouseEvent] = new Entered

        private class Exited extends EdtListener[MenuDragMouseEvent] {
            override protected def listen(env: Env) {
                val l = new MenuDragMouseAdapter {
                    override def menuDragMouseExited(e: MenuDragMouseEvent) = env(e)
                }
                env.add { source.addMenuDragMouseListener(l) }
                env.remove { source.removeMenuDragMouseListener(l) }
            }
        }
        def Exited: Seq[MenuDragMouseEvent] = new Exited

        private class Released extends EdtListener[MenuDragMouseEvent] {
            override protected def listen(env: Env) {
                val l = new MenuDragMouseAdapter {
                    override def menuDragMouseReleased(e: MenuDragMouseEvent) = env(e)
                }
                env.add { source.addMenuDragMouseListener(l) }
                env.remove { source.removeMenuDragMouseListener(l) }
            }
        }
        def Released: Seq[MenuDragMouseEvent] = new Released

    }


// MenuKeyEvent

    import javax.swing.event.{MenuKeyEvent, MenuKeyListener}

    private class MenuKeyAdapter extends MenuKeyListener {
        override def menuKeyPressed(e: MenuKeyEvent) = ()
        override def menuKeyReleased(e: MenuKeyEvent) = ()
        override def menuKeyTyped(e: MenuKeyEvent) = ()
    }

    type MenuKeyEventSource = {
        def addMenuKeyListener(l: MenuKeyListener)
        def removeMenuKeyListener(l: MenuKeyListener)
    }

    case class MenuKey(source: MenuKeyEventSource) {

        private class Event extends EdtListener[MenuKeyEvent] {
            override protected def listen(env: Env) {
                val l = new MenuKeyAdapter {
                    override def menuKeyPressed(e: MenuKeyEvent) = env(e)
                    override def menuKeyReleased(e: MenuKeyEvent) = env(e)
                    override def menuKeyTyped(e: MenuKeyEvent) = env(e)
                }
                env.add { source.addMenuKeyListener(l) }
                env.remove { source.removeMenuKeyListener(l) }
            }
        }
        def Event: Seq[MenuKeyEvent] = new Event

        private class Pressed extends EdtListener[MenuKeyEvent] {
            override protected def listen(env: Env) {
                val l = new MenuKeyAdapter {
                    override def menuKeyPressed(e: MenuKeyEvent) = env(e)
                }
                env.add { source.addMenuKeyListener(l) }
                env.remove { source.removeMenuKeyListener(l) }
            }
        }
        def Pressed: Seq[MenuKeyEvent] = new Pressed

        private class Released extends EdtListener[MenuKeyEvent] {
            override protected def listen(env: Env) {
                val l = new MenuKeyAdapter {
                    override def menuKeyReleased(e: MenuKeyEvent) = env(e)
                }
                env.add { source.addMenuKeyListener(l) }
                env.remove { source.removeMenuKeyListener(l) }
            }
        }
        def Released: Seq[MenuKeyEvent] = new Released

        private class Typed extends EdtListener[MenuKeyEvent] {
            override protected def listen(env: Env) {
                val l = new MenuKeyAdapter {
                    override def menuKeyTyped(e: MenuKeyEvent) = env(e)
                }
                env.add { source.addMenuKeyListener(l) }
                env.remove { source.removeMenuKeyListener(l) }
            }
        }
        def Typed: Seq[MenuKeyEvent] = new Typed

    }


// MouseEvent

    import java.awt.event.{MouseEvent, MouseWheelEvent}
    import javax.swing.event.MouseInputAdapter

    case class Mouse(source: AComponent) {

        private class Event extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseClicked(e: MouseEvent) = env(e)
                    override def mouseEntered(e: MouseEvent) = env(e)
                    override def mouseExited(e: MouseEvent) = env(e)
                    override def mousePressed(e: MouseEvent) = env(e)
                    override def mouseReleased(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseListener(l) }
                env.remove { source.removeMouseListener(l) }
            }
        }
        def Event: Seq[MouseEvent] = new Event

        private class Clicked extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseClicked(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseListener(l) }
                env.remove { source.removeMouseListener(l) }
            }
        }
        def Clicked: Seq[MouseEvent] = new Clicked

        private class Entered extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseEntered(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseListener(l) }
                env.remove { source.removeMouseListener(l) }
            }
        }
        def Entered: Seq[MouseEvent] = new Entered

        private class Exited extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseExited(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseListener(l) }
                env.remove { source.removeMouseListener(l) }
            }
        }
        def Exited: Seq[MouseEvent] = new Exited

        private class Pressed extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mousePressed(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseListener(l) }
                env.remove { source.removeMouseListener(l) }
            }
        }
        def Pressed: Seq[MouseEvent] = new Pressed

        private class Released extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseReleased(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseListener(l) }
                env.remove { source.removeMouseListener(l) }
            }
        }
        def Released: Seq[MouseEvent] = new Released

        // Motion MouseEvent

        private class Motion extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseDragged(e: MouseEvent) = env(e)
                    override def mouseMoved(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseMotionListener(l) }
                env.remove { source.removeMouseMotionListener(l) }
            }
        }
        def Motion: Seq[MouseEvent] = new Motion

        private class Dragged extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseDragged(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseMotionListener(l) }
                env.remove { source.removeMouseMotionListener(l) }
            }
        }
        def Dragged: Seq[MouseEvent] = new Dragged

        private class Moved extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseMoved(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseMotionListener(l) }
                env.remove { source.removeMouseMotionListener(l) }
            }
        }
        def Moved: Seq[MouseEvent] = new Moved

        // MouseWheelEvent

        private class WheelMoved extends EdtListener[MouseWheelEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseWheelMoved(e: MouseWheelEvent) = env(e)
                }
                env.add { source.addMouseWheelListener(l) }
                env.remove { source.removeMouseWheelListener(l) }
            }
        }
        def WheelMoved: Seq[MouseEvent] = new WheelMoved

    }


// PopupMenuEvent

    import javax.swing.event.{PopupMenuEvent, PopupMenuListener}

    private class PopupMenuAdapter extends PopupMenuListener {
        override def popupMenuCanceled(e: PopupMenuEvent) = ()
        override def popupMenuWillBecomeInvisible(e: PopupMenuEvent) = ()
        override def popupMenuWillBecomeVisible(e: PopupMenuEvent) = ()
    }

    type PopupMenuEventSource = {
        def addPopupMenuListener(l: PopupMenuListener)
        def removePopupMenuListener(l: PopupMenuListener)
    }

    case class PopupMenu(source: PopupMenuEventSource) {

        private class Event extends EdtListener[PopupMenuEvent] {
            override protected def listen(env: Env) {
                val l = new PopupMenuAdapter {
                    override def popupMenuCanceled(e: PopupMenuEvent) = env(e)
                    override def popupMenuWillBecomeInvisible(e: PopupMenuEvent) = env(e)
                    override def popupMenuWillBecomeVisible(e: PopupMenuEvent) = env(e)
                }
                env.add { source.addPopupMenuListener(l) }
                env.remove { source.removePopupMenuListener(l) }
            }
        }
        def Event: Seq[PopupMenuEvent] = new Event

        private class Canceled extends EdtListener[PopupMenuEvent] {
            override protected def listen(env: Env) {
                val l = new PopupMenuAdapter {
                    override def popupMenuCanceled(e: PopupMenuEvent) = env(e)
                }
                env.add { source.addPopupMenuListener(l) }
                env.remove { source.removePopupMenuListener(l) }
            }
        }
        def Canceled: Seq[PopupMenuEvent] = new Canceled

        private class WillBecomeInvisible extends EdtListener[PopupMenuEvent] {
            override protected def listen(env: Env) {
                val l = new PopupMenuAdapter {
                    override def popupMenuWillBecomeVisible(e: PopupMenuEvent) = env(e)
                }
                env.add { source.addPopupMenuListener(l) }
                env.remove { source.removePopupMenuListener(l) }
            }
        }
        def WillBecomeInvisible: Seq[PopupMenuEvent] = new WillBecomeInvisible

        private class WillBecomeVisible extends EdtListener[PopupMenuEvent] {
            override protected def listen(env: Env) {
                val l = new PopupMenuAdapter {
                    override def popupMenuWillBecomeVisible(e: PopupMenuEvent) = env(e)
                }
                env.add { source.addPopupMenuListener(l) }
                env.remove { source.removePopupMenuListener(l) }
            }
        }
        def WillBecomeVisible: Seq[PopupMenuEvent] = new WillBecomeVisible

    }

}
