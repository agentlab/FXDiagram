package de.fxdiagram.core

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.Lazy
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.anchors.Anchors
import de.fxdiagram.core.anchors.RectangleAnchors
import de.fxdiagram.core.behavior.MoveBehavior
import javafx.collections.ObservableList
import javafx.scene.effect.DropShadow
import javafx.scene.effect.Effect
import javafx.scene.effect.InnerShadow

import static javafx.collections.FXCollections.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.ForeachExtensions.*

@Logging
class XNode extends XShape {

	static int instanceCount

	@FxProperty @Lazy double width
	@FxProperty @Lazy double height
	@FxProperty @ReadOnly String key
	@FxProperty ObservableList<XConnection> incomingConnections = observableArrayList
	@FxProperty ObservableList<XConnection> outgoingConnections = observableArrayList
	 
	Effect mouseOverEffect
	Effect selectionEffect
	Effect originalEffect

	MoveBehavior<XNode> moveBehavior
	Anchors anchors

	new() {
		mouseOverEffect = createMouseOverEffect
		key = class.simpleName + instanceCount
		instanceCount = instanceCount + 1
		selectionEffect = createSelectionEffect
		anchors = createAnchors
	}

	new(String key) {
		this()
		this.key = key
	}

	protected def createMouseOverEffect() {
		new InnerShadow
	}

	protected def createSelectionEffect() {
		new DropShadow() => [
			offsetX = 4.0
			offsetY = 4.0
		]
	}

	protected def Anchors createAnchors() {
		new RectangleAnchors(this)
	}

	override doActivate() {
		if(key == null) {
			LOG.warning('Node\'s key is not set')
		}
		moveBehavior = new MoveBehavior(this)
		moveBehavior.activate()
		onMouseEntered = [
			originalEffect = node.effect
			node.effect = mouseOverEffect ?: originalEffect
		]
		onMouseExited = [
			node.effect = originalEffect
		]
		switch n:node { XActivatable: n.activate }
	}

	override selectionFeedback(boolean isSelected) {
		if (isSelected) {
			effect = selectionEffect
			scaleX = 1.05
			scaleY = 1.05
			(outgoingConnections + incomingConnections).forEachExt[toFront]
		} else {
			effect = null
			scaleX = 1.0
			scaleY = 1.0
		}
	}
	
	override getSnapBounds() {
		node.boundsInParent.scale(1 / scaleX, 1 / scaleY)
	}

	protected def setKey(String key) {
		keyProperty.set(key)
	}

	override getMoveBehavior() {
		moveBehavior
	}

	def getAnchors() {
		anchors
	}

	override minWidth(double height) {
		if (widthProperty != null)
			widthProperty.get
		else
			super.minWidth(height)
	}

	override minHeight(double width) {
		if (heightProperty != null)
			heightProperty.get
		else
			super.minHeight(width)
	}

	override prefWidth(double height) {
		if (widthProperty != null)
			widthProperty.get
		else
			super.prefWidth(height)
	}

	override prefHeight(double width) {
		if (heightProperty != null)
			heightProperty.get
		else
			super.prefHeight(width)
	}

	override maxWidth(double height) {
		if (widthProperty != null)
			widthProperty.get
		else
			super.maxWidth(height)
	}

	override maxHeight(double width) {
		if (heightProperty != null)
			heightProperty.get
		else
			super.maxHeight(width)
	}
}
