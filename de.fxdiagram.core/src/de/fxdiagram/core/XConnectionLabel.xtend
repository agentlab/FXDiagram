package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.behavior.MoveBehavior
import java.util.List
import javafx.geometry.VPos
import javafx.scene.effect.DropShadow
import javafx.scene.effect.Effect
import javafx.scene.text.Text
import javafx.scene.transform.Affine

import static extension de.fxdiagram.core.geometry.TransformExtensions.*
import static extension java.lang.Math.*

class XConnectionLabel extends XShape {

	@FxProperty XConnection connection
	@FxProperty Text text

	MoveBehavior<XConnectionLabel> moveBehavior
	Effect selectionEffect

	new(XConnection connection) {
		this.connection = connection
		connection.label = this
		text = new Text => [
			textOrigin = VPos.TOP
		]
		node = text
		selectionEffect = new DropShadow
	}

	override doActivate() {
		moveBehavior = new MoveBehavior(this)
		moveBehavior.activate()
		selectedProperty.addListener [ observable, oldValue, newValue |
			if (newValue) {
				effect = selectionEffect
				scaleX = 1.05
				scaleY = 1.05
				connection.selected = true
			} else {
				effect = null
				scaleX = 1.0
				scaleY = 1.0
			}
		]
	}

	def protected void place(List<XControlPoint> list) {
		transforms.clear
		val center = connection.at(0.5)
		val derivative = connection.derivativeAt(0.5)
		var angle = atan2(derivative.y, derivative.x)
		val labelDx = -boundsInLocal.width / 2
		var labelDy = 1
		if (abs(angle) > PI / 2) {
			if (angle < 0)
				angle = angle + PI
			else if (angle > 0)
				angle = angle - PI
		}
		val transform = new Affine
		transform.translate(labelDx, labelDy)
		transform.rotate(angle.toDegrees)
		layoutX = transform.tx + center.x
		layoutY = transform.ty + center.y
		transform.tx = 0
		transform.ty = 0
		transforms += transform
	}

	override getMoveBehavior() {
		moveBehavior
	}
}
