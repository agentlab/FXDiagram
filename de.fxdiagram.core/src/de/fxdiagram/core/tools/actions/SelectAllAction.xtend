package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import eu.hansolo.enzo.radialmenu.Symbol
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

import static extension de.fxdiagram.annotations.ForeachExtensions.*

class SelectAllAction implements DiagramAction {
	
	override matches(KeyEvent it) {
		isShortcutDown && code == KeyCode.A
	}
	
	override getSymbol() {
		Symbol.Type.SELECTION1
	}
	
	override perform(XRoot root) {
		root.diagram.allShapes.forEachExt[if(selectable) selected = true]
	}
}