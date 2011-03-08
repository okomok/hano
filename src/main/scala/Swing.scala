

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.awt.{Component => AComponent, Container => AContainer}
import javax.swing.JComponent


object Swing {


    private[hano]
    trait EdtListener[A] extends listen.To[A] {
        final override def process = Edt
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

        object Event extends EdtListener[AncestorEvent] {
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

        object Added extends EdtListener[AncestorEvent] {
            override protected def listen(env: Env) {
                val l = new AncestorAdapter {
                    override def ancestorAdded(e: AncestorEvent) = env(e)
                }
                env.add { source.addAncestorListener(l) }
                env.remove { source.removeAncestorListener(l) }
            }
        }

        object Moved extends EdtListener[AncestorEvent] {
            override protected def listen(env: Env) {
                val l = new AncestorAdapter {
                    override def ancestorMoved(e: AncestorEvent) = env(e)
                }
                env.add { source.addAncestorListener(l) }
                env.remove { source.removeAncestorListener(l) }
            }
        }

        object Removed extends EdtListener[AncestorEvent] {
            override protected def listen(env: Env) {
                val l = new AncestorAdapter {
                    override def ancestorRemoved(e: AncestorEvent) = env(e)
                }
                env.add { source.addAncestorListener(l) }
                env.remove { source.removeAncestorListener(l) }
            }
        }

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

        object Event extends EdtListener[ComponentEvent] {
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

        object Hidden extends EdtListener[ComponentEvent] {
            override protected def listen(env: Env) {
                val l = new ComponentAdapter {
                    override def componentHidden(e: ComponentEvent) = env(e)
                }
                env.add { source.addComponentListener(l) }
                env.remove { source.removeComponentListener(l) }
            }
        }

        object Moved extends EdtListener[ComponentEvent] {
            override protected def listen(env: Env) {
                val l = new ComponentAdapter {
                    override def componentMoved(e: ComponentEvent) = env(e)
                }
                env.add { source.addComponentListener(l) }
                env.remove { source.removeComponentListener(l) }
            }
        }

        object Resized extends EdtListener[ComponentEvent] {
            override protected def listen(env: Env) {
                val l = new ComponentAdapter {
                    override def componentResized(e: ComponentEvent) = env(e)
                }
                env.add { source.addComponentListener(l) }
                env.remove { source.removeComponentListener(l) }
            }
        }

        object Shown extends EdtListener[ComponentEvent] {
            override protected def listen(env: Env) {
                val l = new ComponentAdapter {
                    override def componentShown(e: ComponentEvent) = env(e)
                }
                env.add { source.addComponentListener(l) }
                env.remove { source.removeComponentListener(l) }
            }
        }
    }


// ContainerEvent

    import java.awt.event.{ContainerEvent, ContainerAdapter}

    case class Container(source: AContainer) {

        object Event extends EdtListener[ContainerEvent] {
            override protected def listen(env: Env) {
                val l = new ContainerAdapter {
                    override def componentAdded(e: ContainerEvent) = env(e)
                    override def componentRemoved(e: ContainerEvent) = env(e)
                }
                env.add { source.addContainerListener(l) }
                env.remove { source.removeContainerListener(l) }
            }
        }

        object Added extends EdtListener[ContainerEvent] {
            override protected def listen(env: Env) {
                val l = new ContainerAdapter {
                    override def componentAdded(e: ContainerEvent) = env(e)
                }
                env.add { source.addContainerListener(l) }
                env.remove { source.removeContainerListener(l) }
            }
        }

        object Removed extends EdtListener[ContainerEvent] {
            override protected def listen(env: Env) {
                val l = new ContainerAdapter {
                    override def componentRemoved(e: ContainerEvent) = env(e)
                }
                env.add { source.addContainerListener(l) }
                env.remove { source.removeContainerListener(l) }
            }
        }

    }


// FocusEvent

    import java.awt.event.{FocusEvent, FocusAdapter}

    case class Focus(source: AComponent) {

        object Event extends EdtListener[FocusEvent] {
            override protected def listen(env: Env) {
                val l = new FocusAdapter {
                    override def focusGained(e: FocusEvent) = env(e)
                    override def focusLost(e: FocusEvent) = env(e)
                }
                env.add { source.addFocusListener(l) }
                env.remove { source.removeFocusListener(l) }
            }
        }

        object Gained extends EdtListener[FocusEvent] {
            override protected def listen(env: Env) {
                val l = new FocusAdapter {
                    override def focusGained(e: FocusEvent) = env(e)
                }
                env.add { source.addFocusListener(l) }
                env.remove { source.removeFocusListener(l) }
            }
        }

        object Lost extends EdtListener[FocusEvent] {
            override protected def listen(env: Env) {
                val l = new FocusAdapter {
                    override def focusLost(e: FocusEvent) = env(e)
                }
                env.add { source.addFocusListener(l) }
                env.remove { source.removeFocusListener(l) }
            }
        }

    }


// HierarchyEvent

    import java.awt.event.{HierarchyEvent, HierarchyListener, HierarchyBoundsAdapter}

    case class Hierarchy(source: AComponent) {

        object Changed extends EdtListener[HierarchyEvent] {
            override protected def listen(env: Env) {
                val l = new HierarchyListener {
                    override def hierarchyChanged(e: HierarchyEvent) = env(e)
                }
                env.add { source.addHierarchyListener(l) }
                env.remove { source.removeHierarchyListener(l) }
            }
        }

        object Bounds extends EdtListener[HierarchyEvent] {
            override protected def listen(env: Env) {
                val l = new HierarchyBoundsAdapter {
                    override def ancestorMoved(e: HierarchyEvent) = env(e)
                    override def ancestorResized(e: HierarchyEvent) = env(e)
                }
                env.add { source.addHierarchyBoundsListener(l) }
                env.remove { source.removeHierarchyBoundsListener(l) }
            }
        }

        object Moved extends EdtListener[HierarchyEvent] {
            override protected def listen(env: Env) {
                val l = new HierarchyBoundsAdapter {
                    override def ancestorMoved(e: HierarchyEvent) = env(e)
                }
                env.add { source.addHierarchyBoundsListener(l) }
                env.remove { source.removeHierarchyBoundsListener(l) }
            }
        }

        object Resized extends EdtListener[HierarchyEvent] {
            override protected def listen(env: Env) {
                val l = new HierarchyBoundsAdapter {
                    override def ancestorResized(e: HierarchyEvent) = env(e)
                }
                env.add { source.addHierarchyBoundsListener(l) }
                env.remove { source.removeHierarchyBoundsListener(l) }
            }
        }

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

        object Event extends EdtListener[InputMethodEvent] {
            override protected def listen(env: Env) {
                val l = new InputMethodAdapter {
                        override def caretPositionChanged(e: InputMethodEvent) = env(e)
                        override def inputMethodTextChanged(e: InputMethodEvent) = env(e)
                }
                env.add { source.addInputMethodListener(l) }
                env.remove { source.removeInputMethodListener(l) }
            }
        }

        object CaretPositionChanged extends EdtListener[InputMethodEvent] {
            override protected def listen(env: Env) {
                val l = new InputMethodAdapter {
                    override def caretPositionChanged(e: InputMethodEvent) = env(e)
                }
                env.add { source.addInputMethodListener(l) }
                env.remove { source.removeInputMethodListener(l) }
            }
        }

        object TextChanged extends EdtListener[InputMethodEvent] {
            override protected def listen(env: Env) {
                val l = new InputMethodAdapter {
                    override def inputMethodTextChanged(e: InputMethodEvent) = env(e)
                }
                env.add { source.addInputMethodListener(l) }
                env.remove { source.removeInputMethodListener(l) }
            }
        }

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

        object Event extends EdtListener[KeyEvent] {
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

        object Pressed extends EdtListener[KeyEvent] {
            override protected def listen(env: Env) {
                val l = new KeyAdapter {
                    override def keyPressed(e: KeyEvent) = env(e)
                }
                env.add { source.addKeyListener(l) }
                env.remove { source.removeKeyListener(l) }
            }
        }

        object Released extends EdtListener[KeyEvent] {
            override protected def listen(env: Env) {
                val l = new KeyAdapter {
                    override def keyReleased(e: KeyEvent) = env(e)
                }
                env.add { source.addKeyListener(l) }
                env.remove { source.removeKeyListener(l) }
            }
        }

        object Typed extends EdtListener[KeyEvent] {
            override protected def listen(env: Env) {
                val l = new KeyAdapter {
                    override def keyTyped(e: KeyEvent) = env(e)
                }
                env.add { source.addKeyListener(l) }
                env.remove { source.removeKeyListener(l) }
            }
        }

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

        object Event extends EdtListener[MenuEvent] {
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

        object Canceled extends EdtListener[MenuEvent] {
            override protected def listen(env: Env) {
                val l = new MenuAdapter {
                    override def menuCanceled(e: MenuEvent) = env(e)
                }
                env.add { source.addMenuListener(l) }
                env.remove { source.removeMenuListener(l) }
            }
        }

        object Deselected extends EdtListener[MenuEvent] {
            override protected def listen(env: Env) {
                val l = new MenuAdapter {
                    override def menuDeselected(e: MenuEvent) = env(e)
                }
                env.add { source.addMenuListener(l) }
                env.remove { source.removeMenuListener(l) }
            }
        }

        object Selected extends EdtListener[MenuEvent] {
            override protected def listen(env: Env) {
                val l = new MenuAdapter {
                    override def menuSelected(e: MenuEvent) = env(e)
                }
                env.add { source.addMenuListener(l) }
                env.remove { source.removeMenuListener(l) }
            }
        }

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

        object Event extends EdtListener[MenuDragMouseEvent] {
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

        object Dragged extends EdtListener[MenuDragMouseEvent] {
            override protected def listen(env: Env) {
                val l = new MenuDragMouseAdapter {
                    override def menuDragMouseDragged(e: MenuDragMouseEvent) = env(e)
                }
                env.add { source.addMenuDragMouseListener(l) }
                env.remove { source.removeMenuDragMouseListener(l) }
            }
        }

        object Entered extends EdtListener[MenuDragMouseEvent] {
            override protected def listen(env: Env) {
                val l = new MenuDragMouseAdapter {
                    override def menuDragMouseEntered(e: MenuDragMouseEvent) = env(e)
                }
                env.add { source.addMenuDragMouseListener(l) }
                env.remove { source.removeMenuDragMouseListener(l) }
            }
        }

        object Exited extends EdtListener[MenuDragMouseEvent] {
            override protected def listen(env: Env) {
                val l = new MenuDragMouseAdapter {
                    override def menuDragMouseExited(e: MenuDragMouseEvent) = env(e)
                }
                env.add { source.addMenuDragMouseListener(l) }
                env.remove { source.removeMenuDragMouseListener(l) }
            }
        }

        object Released extends EdtListener[MenuDragMouseEvent] {
            override protected def listen(env: Env) {
                val l = new MenuDragMouseAdapter {
                    override def menuDragMouseReleased(e: MenuDragMouseEvent) = env(e)
                }
                env.add { source.addMenuDragMouseListener(l) }
                env.remove { source.removeMenuDragMouseListener(l) }
            }
        }

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

        object Event extends EdtListener[MenuKeyEvent] {
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

        object Pressed extends EdtListener[MenuKeyEvent] {
            override protected def listen(env: Env) {
                val l = new MenuKeyAdapter {
                    override def menuKeyPressed(e: MenuKeyEvent) = env(e)
                }
                env.add { source.addMenuKeyListener(l) }
                env.remove { source.removeMenuKeyListener(l) }
            }
        }

        object Released extends EdtListener[MenuKeyEvent] {
            override protected def listen(env: Env) {
                val l = new MenuKeyAdapter {
                    override def menuKeyReleased(e: MenuKeyEvent) = env(e)
                }
                env.add { source.addMenuKeyListener(l) }
                env.remove { source.removeMenuKeyListener(l) }
            }
        }

        object Typed extends EdtListener[MenuKeyEvent] {
            override protected def listen(env: Env) {
                val l = new MenuKeyAdapter {
                    override def menuKeyTyped(e: MenuKeyEvent) = env(e)
                }
                env.add { source.addMenuKeyListener(l) }
                env.remove { source.removeMenuKeyListener(l) }
            }
        }

    }


// MouseEvent

    import java.awt.event.{MouseEvent, MouseWheelEvent}
    import javax.swing.event.MouseInputAdapter

    case class Mouse(source: AComponent) {

        object Event extends EdtListener[MouseEvent] {
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

        object Clicked extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseClicked(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseListener(l) }
                env.remove { source.removeMouseListener(l) }
            }
        }

        object Entered extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseEntered(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseListener(l) }
                env.remove { source.removeMouseListener(l) }
            }
        }

        object Exited extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseExited(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseListener(l) }
                env.remove { source.removeMouseListener(l) }
            }
        }

        object Pressed extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mousePressed(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseListener(l) }
                env.remove { source.removeMouseListener(l) }
            }
        }

        object Released extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseReleased(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseListener(l) }
                env.remove { source.removeMouseListener(l) }
            }
        }

        // Motion MouseEvent

        object Motion extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseDragged(e: MouseEvent) = env(e)
                    override def mouseMoved(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseMotionListener(l) }
                env.remove { source.removeMouseMotionListener(l) }
            }
        }

        object Dragged extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseDragged(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseMotionListener(l) }
                env.remove { source.removeMouseMotionListener(l) }
            }
        }

        object Moved extends EdtListener[MouseEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseMoved(e: MouseEvent) = env(e)
                }
                env.add { source.addMouseMotionListener(l) }
                env.remove { source.removeMouseMotionListener(l) }
            }
        }

        // MouseWheelEvent

        object WheelMoved extends EdtListener[MouseWheelEvent] {
            override protected def listen(env: Env) {
                val l = new MouseInputAdapter {
                    override def mouseWheelMoved(e: MouseWheelEvent) = env(e)
                }
                env.add { source.addMouseWheelListener(l) }
                env.remove { source.removeMouseWheelListener(l) }
            }
        }

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

        object Event extends EdtListener[PopupMenuEvent] {
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

        object Canceled extends EdtListener[PopupMenuEvent] {
            override protected def listen(env: Env) {
                val l = new PopupMenuAdapter {
                    override def popupMenuCanceled(e: PopupMenuEvent) = env(e)
                }
                env.add { source.addPopupMenuListener(l) }
                env.remove { source.removePopupMenuListener(l) }
            }
        }

        object WillBecomeInvisible extends EdtListener[PopupMenuEvent] {
            override protected def listen(env: Env) {
                val l = new PopupMenuAdapter {
                    override def popupMenuWillBecomeVisible(e: PopupMenuEvent) = env(e)
                }
                env.add { source.addPopupMenuListener(l) }
                env.remove { source.removePopupMenuListener(l) }
            }
        }

        object WillBecomeVisible extends EdtListener[PopupMenuEvent] {
            override protected def listen(env: Env) {
                val l = new PopupMenuAdapter {
                    override def popupMenuWillBecomeVisible(e: PopupMenuEvent) = env(e)
                }
                env.add { source.addPopupMenuListener(l) }
                env.remove { source.removePopupMenuListener(l) }
            }
        }

    }

}
