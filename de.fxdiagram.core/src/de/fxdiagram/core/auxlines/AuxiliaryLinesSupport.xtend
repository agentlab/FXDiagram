package de.fxdiagram.core.auxlines

import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XShape
import javafx.scene.Group

import static extension de.fxdiagram.annotations.ForeachExtensions.*

class AuxiliaryLinesSupport {
	
	AuxiliaryLinesCache cache
	Group group = new Group
	
	
	new(XDiagram diagram) {
		this.cache = new AuxiliaryLinesCache(diagram)
		diagram.buttonLayer.children += group
	}
	
	def show(Iterable<? extends XShape> selection) {
		group.children.clear()
		val selectedNodes = selection.filter(XNode)
		if(selectedNodes.size == 1) {
			val lines = cache.getAuxiliaryLines(selectedNodes.head)
			lines.forEachExt[ group.children += createNode ]
		}	
	}
	
	def hide() {
		group.children.clear()		
	}
}