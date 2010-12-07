

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.awt.{Component => AComponent, Container => AContainer}
import javax.swing.JComponent


object Swing {


// ActionEvent

    import java.awt.event.{ActionEvent, ActionListener}

    type ActionEventSource = {
        def addActionListener(l: ActionListener)
        def removeActionListener(l: ActionListener)
    }

    case class ActionPerformed(source: ActionEventSource) extends NoEndResource[ActionEvent] {
        private[this] var l: ActionListener = null
        override protected def closeResource() = source.removeActionListener(l)
        override protected def openResource(f: ActionEvent => Unit) {
            l = new ActionListener {
                override def actionPerformed(e: ActionEvent) = f(e)
            }
            source.addActionListener(l)
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

        private class Event extends NoEndResource[AncestorEvent] {
            private[this] var l: AncestorAdapter = null
            override protected def closeResource() = source.removeAncestorListener(l)
            override protected def openResource(f: AncestorEvent => Unit) {
                l = new AncestorAdapter {
                    override def ancestorAdded(e: AncestorEvent) = f(e)
                    override def ancestorMoved(e: AncestorEvent) = f(e)
                    override def ancestorRemoved(e: AncestorEvent) = f(e)
                }
                source.addAncestorListener(l)
            }
        }
        def Event: Seq[AncestorEvent] = new Event

        private class Added extends NoEndResource[AncestorEvent] {
            private[this] var l: AncestorAdapter = null
            override protected def closeResource() = source.removeAncestorListener(l)
            override protected def openResource(f: AncestorEvent => Unit) {
                l = new AncestorAdapter {
                    override def ancestorAdded(e: AncestorEvent) = f(e)
                }
                source.addAncestorListener(l)
            }
        }
        def Added: Seq[AncestorEvent] = new Added

        private class Moved extends NoEndResource[AncestorEvent] {
            private[this] var l: AncestorAdapter = null
            override protected def closeResource() = source.removeAncestorListener(l)
            override protected def openResource(f: AncestorEvent => Unit) {
                l = new AncestorAdapter {
                    override def ancestorMoved(e: AncestorEvent) = f(e)
                }
                source.addAncestorListener(l)
            }
        }
        def Moved: Seq[AncestorEvent] = new Moved

        private class Removed extends NoEndResource[AncestorEvent] {
            private[this] var l: AncestorAdapter = null
            override protected def closeResource() = source.removeAncestorListener(l)
            override protected def openResource(f: AncestorEvent => Unit) {
                l = new AncestorAdapter {
                    override def ancestorRemoved(e: AncestorEvent) = f(e)
                }
                source.addAncestorListener(l)
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

    case class StateChanged(source: ChangeEventSource) extends NoEndResource[ChangeEvent] {
        private[this] var l: ChangeListener = null
        override protected def closeResource() = source.removeChangeListener(l)
        override protected def openResource(f: ChangeEvent => Unit) {
            l = new ChangeListener {
                override def stateChanged(e: ChangeEvent) = f(e)
            }
            source.addChangeListener(l)
        }
    }


// ComponentEvent

    import java.awt.event.{ComponentEvent, ComponentAdapter}

    case class Component(source: AComponent) {

        private class Event extends NoEndResource[ComponentEvent] {
            private[this] var l: ComponentAdapter = null
            override protected def closeResource() = source.removeComponentListener(l)
            override protected def openResource(f: ComponentEvent => Unit) {
                l = new ComponentAdapter {
                    override def componentHidden(e: ComponentEvent) = f(e)
                    override def componentMoved(e: ComponentEvent) = f(e)
                    override def componentResized(e: ComponentEvent) = f(e)
                    override def componentShown(e: ComponentEvent) = f(e)
                }
                source.addComponentListener(l)
            }
        }
        def Event: Seq[ComponentEvent] = new Event

        private class Hidden extends NoEndResource[ComponentEvent] {
            private[this] var l: ComponentAdapter = null
            override protected def closeResource() = source.removeComponentListener(l)
            override protected def openResource(f: ComponentEvent => Unit) {
                l = new ComponentAdapter {
                    override def componentHidden(e: ComponentEvent) = f(e)
                }
                source.addComponentListener(l)
            }
        }
        def Hidden: Seq[ComponentEvent] = new Hidden

        private class Moved extends NoEndResource[ComponentEvent] {
            private[this] var l: ComponentAdapter = null
            override protected def closeResource() = source.removeComponentListener(l)
            override protected def openResource(f: ComponentEvent => Unit) {
                l = new ComponentAdapter {
                    override def componentMoved(e: ComponentEvent) = f(e)
                }
                source.addComponentListener(l)
            }
        }
        def Moved: Seq[ComponentEvent] = new Moved

        private class Resized extends NoEndResource[ComponentEvent] {
            private[this] var l: ComponentAdapter = null
            override protected def closeResource() = source.removeComponentListener(l)
            override protected def openResource(f: ComponentEvent => Unit) {
                l = new ComponentAdapter {
                    override def componentResized(e: ComponentEvent) = f(e)
                }
                source.addComponentListener(l)
            }
        }
        def Resized: Seq[ComponentEvent] = new Resized

        private class Shown extends NoEndResource[ComponentEvent] {
            private[this] var l: ComponentAdapter = null
            override protected def closeResource() = source.removeComponentListener(l)
            override protected def openResource(f: ComponentEvent => Unit) {
                l = new ComponentAdapter {
                    override def componentShown(e: ComponentEvent) = f(e)
                }
                source.addComponentListener(l)
            }
        }
        def Shown: Seq[ComponentEvent] = new Shown
    }


// ContainerEvent

    import java.awt.event.{ContainerEvent, ContainerAdapter}

    case class Container(source: AContainer) {

        private class Event extends NoEndResource[ContainerEvent] {
            private[this] var l: ContainerAdapter = null
            override protected def closeResource() = source.removeContainerListener(l)
            override protected def openResource(f: ContainerEvent => Unit) {
                l = new ContainerAdapter {
                    override def componentAdded(e: ContainerEvent) = f(e)
                    override def componentRemoved(e: ContainerEvent) = f(e)
                }
                source.addContainerListener(l)
            }
        }
        def Event: Seq[ContainerEvent] = new Event

        private class Added extends NoEndResource[ContainerEvent] {
            private[this] var l: ContainerAdapter = null
            override protected def closeResource() = source.removeContainerListener(l)
            override protected def openResource(f: ContainerEvent => Unit) {
                l = new ContainerAdapter {
                    override def componentAdded(e: ContainerEvent) = f(e)
                }
                source.addContainerListener(l)
            }
        }
        def Added: Seq[ContainerEvent] = new Added

        private class Removed extends NoEndResource[ContainerEvent] {
            private[this] var l: ContainerAdapter = null
            override protected def closeResource() = source.removeContainerListener(l)
            override protected def openResource(f: ContainerEvent => Unit) {
                l = new ContainerAdapter {
                    override def componentRemoved(e: ContainerEvent) = f(e)
                }
                source.addContainerListener(l)
            }
        }
        def Removed: Seq[ContainerEvent] = new Removed

    }


// FocusEvent

    import java.awt.event.{FocusEvent, FocusAdapter}

    case class Focus(source: AComponent) {

        private class Event extends NoEndResource[FocusEvent] {
            private[this] var l: FocusAdapter = null
            override protected def closeResource() = source.removeFocusListener(l)
            override protected def openResource(f: FocusEvent => Unit) {
                l = new FocusAdapter {
                    override def focusGained(e: FocusEvent) = f(e)
                    override def focusLost(e: FocusEvent) = f(e)
                }
                source.addFocusListener(l)
            }
        }
        def Event: Seq[FocusEvent] = new Event

        private class Gained extends NoEndResource[FocusEvent] {
            private[this] var l: FocusAdapter = null
            override protected def closeResource() = source.removeFocusListener(l)
            override protected def openResource(f: FocusEvent => Unit) {
                l = new FocusAdapter {
                    override def focusGained(e: FocusEvent) = f(e)
                }
                source.addFocusListener(l)
            }
        }
        def Gained: Seq[FocusEvent] = new Gained

        private class Lost extends NoEndResource[FocusEvent] {
            private[this] var l: FocusAdapter = null
            override protected def closeResource() = source.removeFocusListener(l)
            override protected def openResource(f: FocusEvent => Unit) {
                l = new FocusAdapter {
                    override def focusLost(e: FocusEvent) = f(e)
                }
                source.addFocusListener(l)
            }
        }
        def Lost: Seq[FocusEvent] = new Lost

    }


// HierarchyEvent

    import java.awt.event.{HierarchyEvent, HierarchyListener, HierarchyBoundsAdapter}

    case class Hierarchy(source: AComponent) {

        private class Changed extends NoEndResource[HierarchyEvent] {
            private[this] var l: HierarchyListener = null
            override protected def closeResource() = source.removeHierarchyListener(l)
            override protected def openResource(f: HierarchyEvent => Unit) {
                l = new HierarchyListener {
                    override def hierarchyChanged(e: HierarchyEvent) = f(e)
                }
                source.addHierarchyListener(l)
            }
        }
        def Changed: Seq[HierarchyEvent] = new Changed

        private class Bounds extends NoEndResource[HierarchyEvent] {
            private[this] var l: HierarchyBoundsAdapter = null
            override protected def closeResource() = source.removeHierarchyBoundsListener(l)
            override protected def openResource(f: HierarchyEvent => Unit) {
                l = new HierarchyBoundsAdapter {
                    override def ancestorMoved(e: HierarchyEvent) = f(e)
                    override def ancestorResized(e: HierarchyEvent) = f(e)
                }
                source.addHierarchyBoundsListener(l)
            }
        }
        def Bounds: Seq[HierarchyEvent] = new Bounds

        private class Moved extends NoEndResource[HierarchyEvent] {
            private[this] var l: HierarchyBoundsAdapter = null
            override protected def closeResource() = source.removeHierarchyBoundsListener(l)
            override protected def openResource(f: HierarchyEvent => Unit) {
                l = new HierarchyBoundsAdapter {
                    override def ancestorMoved(e: HierarchyEvent) = f(e)
                }
                source.addHierarchyBoundsListener(l)
            }
        }
        def Moved: Seq[HierarchyEvent] = new Moved

        private class Resized extends NoEndResource[HierarchyEvent] {
            private[this] var l: HierarchyBoundsAdapter = null
            override protected def closeResource() = source.removeHierarchyBoundsListener(l)
            override protected def openResource(f: HierarchyEvent => Unit) {
                l = new HierarchyBoundsAdapter {
                    override def ancestorResized(e: HierarchyEvent) = f(e)
                }
                source.addHierarchyBoundsListener(l)
            }
        }
        def Resized: Seq[HierarchyEvent] = new Resized

    }


// InputMethodEvent

    import java.awt.event.{InputMethodEvent, InputMethodListener}

    private class InputMethodAdapter extends InputMethodListener {
        override def caretPositionChanged(e: InputMethodEvent) = ()
        override def inputMethodTextChanged(e: InputMethodEvent) = ()
    }

    case class InputMethod(source: AComponent) {

        private class Event extends NoEndResource[InputMethodEvent] {
            private[this] var l: InputMethodAdapter = null
            override protected def closeResource() = source.removeInputMethodListener(l)
            override protected def openResource(f: InputMethodEvent => Unit) {
                l = new InputMethodAdapter {
                        override def caretPositionChanged(e: InputMethodEvent) = f(e)
                        override def inputMethodTextChanged(e: InputMethodEvent) = f(e)
                }
                source.addInputMethodListener(l)
            }
        }
        def Event: Seq[InputMethodEvent] = new Event

        private class CaretPositionChanged extends NoEndResource[InputMethodEvent] {
            private[this] var l: InputMethodAdapter = null
            override protected def closeResource() = source.removeInputMethodListener(l)
            override protected def openResource(f: InputMethodEvent => Unit) {
                l = new InputMethodAdapter {
                    override def caretPositionChanged(e: InputMethodEvent) = f(e)
                }
                source.addInputMethodListener(l)
            }
        }
        def CaretPositionChanged: Seq[InputMethodEvent] = new CaretPositionChanged

        private class TextChanged extends NoEndResource[InputMethodEvent] {
            private[this] var l: InputMethodAdapter = null
            override protected def closeResource() = source.removeInputMethodListener(l)
            override protected def openResource(f: InputMethodEvent => Unit) {
                l = new InputMethodAdapter {
                    override def inputMethodTextChanged(e: InputMethodEvent) = f(e)
                }
                source.addInputMethodListener(l)
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

    case class ItemStateChanged(source: ItemEventSource) extends NoEndResource[ItemEvent] {
        private[this] var l: ItemListener = null
        override protected def closeResource() = source.removeItemListener(l)
        override protected def openResource(f: ItemEvent => Unit) {
            l = new ItemListener {
                override def itemStateChanged(e: ItemEvent) = f(e)
            }
            source.addItemListener(l)
        }
    }


// KeyEvent

    import java.awt.event.{KeyEvent, KeyAdapter}

    case class Key(source: AComponent) {

        private class Event extends NoEndResource[KeyEvent] {
            private[this] var l: KeyAdapter = null
            override protected def closeResource() = source.removeKeyListener(l)
            override protected def openResource(f: KeyEvent => Unit) {
                l = new KeyAdapter {
                    override def keyPressed(e: KeyEvent) = f(e)
                    override def keyReleased(e: KeyEvent) = f(e)
                    override def keyTyped(e: KeyEvent) = f(e)
                }
                source.addKeyListener(l)
            }
        }
        def Event: Seq[KeyEvent] = new Event

        private class Pressed extends NoEndResource[KeyEvent] {
            private[this] var l: KeyAdapter = null
            override protected def closeResource() = source.removeKeyListener(l)
            override protected def openResource(f: KeyEvent => Unit) {
                l = new KeyAdapter {
                    override def keyPressed(e: KeyEvent) = f(e)
                }
                source.addKeyListener(l)
            }
        }
        def Pressed: Seq[KeyEvent] = new Pressed

        private class Released extends NoEndResource[KeyEvent] {
            private[this] var l: KeyAdapter = null
            override protected def closeResource() = source.removeKeyListener(l)
            override protected def openResource(f: KeyEvent => Unit) {
                l = new KeyAdapter {
                    override def keyReleased(e: KeyEvent) = f(e)
                }
                source.addKeyListener(l)
            }
        }
        def Released: Seq[KeyEvent] = new Released

        private class Typed extends NoEndResource[KeyEvent] {
            private[this] var l: KeyAdapter = null
            override protected def closeResource() = source.removeKeyListener(l)
            override protected def openResource(f: KeyEvent => Unit) {
                l = new KeyAdapter {
                    override def keyTyped(e: KeyEvent) = f(e)
                }
                source.addKeyListener(l)
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

        private class Event extends NoEndResource[MenuEvent] {
            private[this] var l: MenuAdapter = null
            override protected def closeResource() = source.removeMenuListener(l)
            override protected def openResource(f: MenuEvent => Unit) {
                l = new MenuAdapter {
                    override def menuCanceled(e: MenuEvent) = f(e)
                    override def menuDeselected(e: MenuEvent) = f(e)
                    override def menuSelected(e: MenuEvent) = f(e)
                }
                source.addMenuListener(l)
            }
        }
        def Event: Seq[MenuEvent] = new Event

        private class Canceled extends NoEndResource[MenuEvent] {
            private[this] var l: MenuAdapter = null
            override protected def closeResource() = source.removeMenuListener(l)
            override protected def openResource(f: MenuEvent => Unit) {
                l = new MenuAdapter {
                    override def menuCanceled(e: MenuEvent) = f(e)
                }
                source.addMenuListener(l)
            }
        }
        def Canceled: Seq[MenuEvent] = new Canceled

        private class Deselected extends NoEndResource[MenuEvent] {
            private[this] var l: MenuAdapter = null
            override protected def closeResource() = source.removeMenuListener(l)
            override protected def openResource(f: MenuEvent => Unit) {
                l = new MenuAdapter {
                    override def menuDeselected(e: MenuEvent) = f(e)
                }
                source.addMenuListener(l)
            }
        }
        def Deselected: Seq[MenuEvent] = new Deselected

        private class Selected extends NoEndResource[MenuEvent] {
            private[this] var l: MenuAdapter = null
            override protected def closeResource() = source.removeMenuListener(l)
            override protected def openResource(f: MenuEvent => Unit) {
                l = new MenuAdapter {
                    override def menuSelected(e: MenuEvent) = f(e)
                }
                source.addMenuListener(l)
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

        private class Event extends NoEndResource[MenuDragMouseEvent] {
            private[this] var l: MenuDragMouseAdapter = null
            override protected def closeResource() = source.removeMenuDragMouseListener(l)
            override protected def openResource(f: MenuDragMouseEvent => Unit) {
                l = new MenuDragMouseAdapter {
                    override def menuDragMouseDragged(e: MenuDragMouseEvent) = f(e)
                    override def menuDragMouseEntered(e: MenuDragMouseEvent) = f(e)
                    override def menuDragMouseExited(e: MenuDragMouseEvent) = f(e)
                    override def menuDragMouseReleased(e: MenuDragMouseEvent) = f(e)
                }
                source.addMenuDragMouseListener(l)
            }
        }
        def Event: Seq[MenuDragMouseEvent] = new Event

        private class Dragged extends NoEndResource[MenuDragMouseEvent] {
            private[this] var l: MenuDragMouseAdapter = null
            override protected def closeResource() = source.removeMenuDragMouseListener(l)
            override protected def openResource(f: MenuDragMouseEvent => Unit) {
                l = new MenuDragMouseAdapter {
                    override def menuDragMouseDragged(e: MenuDragMouseEvent) = f(e)
                }
                source.addMenuDragMouseListener(l)
            }
        }
        def Dragged: Seq[MenuDragMouseEvent] = new Dragged

        private class Entered extends NoEndResource[MenuDragMouseEvent] {
            private[this] var l: MenuDragMouseAdapter = null
            override protected def closeResource() = source.removeMenuDragMouseListener(l)
            override protected def openResource(f: MenuDragMouseEvent => Unit) {
                l = new MenuDragMouseAdapter {
                    override def menuDragMouseEntered(e: MenuDragMouseEvent) = f(e)
                }
                source.addMenuDragMouseListener(l)
            }
        }
        def Entered: Seq[MenuDragMouseEvent] = new Entered

        private class Exited extends NoEndResource[MenuDragMouseEvent] {
            private[this] var l: MenuDragMouseAdapter = null
            override protected def closeResource() = source.removeMenuDragMouseListener(l)
            override protected def openResource(f: MenuDragMouseEvent => Unit) {
                l = new MenuDragMouseAdapter {
                    override def menuDragMouseExited(e: MenuDragMouseEvent) = f(e)
                }
                source.addMenuDragMouseListener(l)
            }
        }
        def Exited: Seq[MenuDragMouseEvent] = new Exited

        private class Released extends NoEndResource[MenuDragMouseEvent] {
            private[this] var l: MenuDragMouseAdapter = null
            override protected def closeResource() = source.removeMenuDragMouseListener(l)
            override protected def openResource(f: MenuDragMouseEvent => Unit) {
                l = new MenuDragMouseAdapter {
                    override def menuDragMouseReleased(e: MenuDragMouseEvent) = f(e)
                }
                source.addMenuDragMouseListener(l)
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

        private class Event extends NoEndResource[MenuKeyEvent] {
            private[this] var l: MenuKeyAdapter = null
            override protected def closeResource() = source.removeMenuKeyListener(l)
            override protected def openResource(f: MenuKeyEvent => Unit) {
                l = new MenuKeyAdapter {
                    override def menuKeyPressed(e: MenuKeyEvent) = f(e)
                    override def menuKeyReleased(e: MenuKeyEvent) = f(e)
                    override def menuKeyTyped(e: MenuKeyEvent) = f(e)
                }
                source.addMenuKeyListener(l)
            }
        }
        def Event: Seq[MenuKeyEvent] = new Event

        private class Pressed extends NoEndResource[MenuKeyEvent] {
            private[this] var l: MenuKeyAdapter = null
            override protected def closeResource() = source.removeMenuKeyListener(l)
            override protected def openResource(f: MenuKeyEvent => Unit) {
                l = new MenuKeyAdapter {
                    override def menuKeyPressed(e: MenuKeyEvent) = f(e)
                }
                source.addMenuKeyListener(l)
            }
        }
        def Pressed: Seq[MenuKeyEvent] = new Pressed

        private class Released extends NoEndResource[MenuKeyEvent] {
            private[this] var l: MenuKeyAdapter = null
            override protected def closeResource() = source.removeMenuKeyListener(l)
            override protected def openResource(f: MenuKeyEvent => Unit) {
                l = new MenuKeyAdapter {
                    override def menuKeyReleased(e: MenuKeyEvent) = f(e)
                }
                source.addMenuKeyListener(l)
            }
        }
        def Released: Seq[MenuKeyEvent] = new Released

        private class Typed extends NoEndResource[MenuKeyEvent] {
            private[this] var l: MenuKeyAdapter = null
            override protected def closeResource() = source.removeMenuKeyListener(l)
            override protected def openResource(f: MenuKeyEvent => Unit) {
                l = new MenuKeyAdapter {
                    override def menuKeyTyped(e: MenuKeyEvent) = f(e)
                }
                source.addMenuKeyListener(l)
            }
        }
        def Typed: Seq[MenuKeyEvent] = new Typed

    }


// MouseEvent

    import java.awt.event.{MouseEvent, MouseWheelEvent}
    import javax.swing.event.MouseInputAdapter

    case class Mouse(source: AComponent) {

        private class Event extends NoEndResource[MouseEvent] {
            private[this] var l: MouseInputAdapter = null
            override protected def closeResource() = source.removeMouseListener(l)
            override protected def openResource(f: MouseEvent => Unit) {
                l = new MouseInputAdapter {
                    override def mouseClicked(e: MouseEvent) = f(e)
                    override def mouseEntered(e: MouseEvent) = f(e)
                    override def mouseExited(e: MouseEvent) = f(e)
                    override def mousePressed(e: MouseEvent) = f(e)
                    override def mouseReleased(e: MouseEvent) = f(e)
                }
                source.addMouseListener(l)
            }
        }
        def Event: Seq[MouseEvent] = new Event

        private class Clicked extends NoEndResource[MouseEvent] {
            private[this] var l: MouseInputAdapter = null
            override protected def closeResource() = source.removeMouseListener(l)
            override protected def openResource(f: MouseEvent => Unit) {
                l = new MouseInputAdapter {
                    override def mouseClicked(e: MouseEvent) = f(e)
                }
                source.addMouseListener(l)
            }
        }
        def Clicked: Seq[MouseEvent] = new Clicked

        private class Entered extends NoEndResource[MouseEvent] {
            private[this] var l: MouseInputAdapter = null
            override protected def closeResource() = source.removeMouseListener(l)
            override protected def openResource(f: MouseEvent => Unit) {
                l = new MouseInputAdapter {
                    override def mouseEntered(e: MouseEvent) = f(e)
                }
                source.addMouseListener(l)
            }
        }
        def Entered: Seq[MouseEvent] = new Entered

        private class Exited extends NoEndResource[MouseEvent] {
            private[this] var l: MouseInputAdapter = null
            override protected def closeResource() = source.removeMouseListener(l)
            override protected def openResource(f: MouseEvent => Unit) {
                l = new MouseInputAdapter {
                    override def mouseExited(e: MouseEvent) = f(e)
                }
                source.addMouseListener(l)
            }
        }
        def Exited: Seq[MouseEvent] = new Exited

        private class Pressed extends NoEndResource[MouseEvent] {
            private[this] var l: MouseInputAdapter = null
            override protected def closeResource() = source.removeMouseListener(l)
            override protected def openResource(f: MouseEvent => Unit) {
                l = new MouseInputAdapter {
                    override def mousePressed(e: MouseEvent) = f(e)
                }
                source.addMouseListener(l)
            }
        }
        def Pressed: Seq[MouseEvent] = new Pressed

        private class Released extends NoEndResource[MouseEvent] {
            private[this] var l: MouseInputAdapter = null
            override protected def closeResource() = source.removeMouseListener(l)
            override protected def openResource(f: MouseEvent => Unit) {
                l = new MouseInputAdapter {
                    override def mouseReleased(e: MouseEvent) = f(e)
                }
                source.addMouseListener(l)
            }
        }
        def Released: Seq[MouseEvent] = new Released

        // Motion MouseEvent

        private class Motion extends NoEndResource[MouseEvent] {
            private[this] var l: MouseInputAdapter = null
            override protected def closeResource() = source.removeMouseMotionListener(l)
            override protected def openResource(f: MouseEvent => Unit) {
                l = new MouseInputAdapter {
                    override def mouseDragged(e: MouseEvent) = f(e)
                    override def mouseMoved(e: MouseEvent) = f(e)
                }
                source.addMouseMotionListener(l)
            }
        }
        def Motion: Seq[MouseEvent] = new Motion

        private class Dragged extends NoEndResource[MouseEvent] {
            private[this] var l: MouseInputAdapter = null
            override protected def closeResource() = source.removeMouseMotionListener(l)
            override protected def openResource(f: MouseEvent => Unit) {
                l = new MouseInputAdapter {
                    override def mouseDragged(e: MouseEvent) = f(e)
                }
                source.addMouseMotionListener(l)
            }
        }
        def Dragged: Seq[MouseEvent] = new Dragged

        private class Moved extends NoEndResource[MouseEvent] {
            private[this] var l: MouseInputAdapter = null
            override protected def closeResource() = source.removeMouseMotionListener(l)
            override protected def openResource(f: MouseEvent => Unit) {
                l = new MouseInputAdapter {
                    override def mouseMoved(e: MouseEvent) = f(e)
                }
                source.addMouseMotionListener(l)
            }
        }
        def Moved: Seq[MouseEvent] = new Moved

        // MouseWheelEvent

        private class WheelMoved extends NoEndResource[MouseWheelEvent] {
            private[this] var l: MouseInputAdapter = null
            override protected def closeResource() = source.removeMouseWheelListener(l)
            override protected def openResource(f: MouseWheelEvent => Unit) {
                l = new MouseInputAdapter {
                    override def mouseWheelMoved(e: MouseWheelEvent) = f(e)
                }
                source.addMouseWheelListener(l)
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

        private class Event extends NoEndResource[PopupMenuEvent] {
            private[this] var l: PopupMenuAdapter = null
            override protected def closeResource() = source.removePopupMenuListener(l)
            override protected def openResource(f: PopupMenuEvent => Unit) {
                l = new PopupMenuAdapter {
                    override def popupMenuCanceled(e: PopupMenuEvent) = f(e)
                    override def popupMenuWillBecomeInvisible(e: PopupMenuEvent) = f(e)
                    override def popupMenuWillBecomeVisible(e: PopupMenuEvent) = f(e)
                }
                source.addPopupMenuListener(l)
            }
        }
        def Event: Seq[PopupMenuEvent] = new Event

        private class Canceled extends NoEndResource[PopupMenuEvent] {
            private[this] var l: PopupMenuAdapter = null
            override protected def closeResource() = source.removePopupMenuListener(l)
            override protected def openResource(f: PopupMenuEvent => Unit) {
                l = new PopupMenuAdapter {
                    override def popupMenuCanceled(e: PopupMenuEvent) = f(e)
                }
                source.addPopupMenuListener(l)
            }
        }
        def Canceled: Seq[PopupMenuEvent] = new Canceled

        private class WillBecomeInvisible extends NoEndResource[PopupMenuEvent] {
            private[this] var l: PopupMenuAdapter = null
            override protected def closeResource() = source.removePopupMenuListener(l)
            override protected def openResource(f: PopupMenuEvent => Unit) {
                l = new PopupMenuAdapter {
                    override def popupMenuWillBecomeVisible(e: PopupMenuEvent) = f(e)
                }
                source.addPopupMenuListener(l)
            }
        }
        def WillBecomeInvisible: Seq[PopupMenuEvent] = new WillBecomeInvisible

        private class WillBecomeVisible extends NoEndResource[PopupMenuEvent] {
            private[this] var l: PopupMenuAdapter = null
            override protected def closeResource() = source.removePopupMenuListener(l)
            override protected def openResource(f: PopupMenuEvent => Unit) {
                l = new PopupMenuAdapter {
                    override def popupMenuWillBecomeVisible(e: PopupMenuEvent) = f(e)
                }
                source.addPopupMenuListener(l)
            }
        }
        def WillBecomeVisible: Seq[PopupMenuEvent] = new WillBecomeVisible

    }

}
