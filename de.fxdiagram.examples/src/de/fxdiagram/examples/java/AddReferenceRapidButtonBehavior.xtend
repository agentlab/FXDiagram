package de.fxdiagram.examples.java

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.XRapidButtonAction
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior
import de.fxdiagram.lib.tools.CarusselChooser
import java.util.Set

import static extension de.fxdiagram.annotations.ForeachExtensions.*
import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class AddReferenceRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<JavaTypeNode, JavaProperty, JavaPropertyDescriptor> {
	
	new(JavaTypeNode host) {
		super(host)
	}
	
	override protected getInitialModelChoices() {
		host.javaTypeModel.references
	}
	
	override protected getChoiceKey(JavaProperty property) {
		domainObjectProvider.createJavaPropertyDescriptor(property)
	}
	
	override protected createNode(JavaPropertyDescriptor key) {
		new JavaTypeNode(domainObjectProvider.createJavaTypeDescriptor(key.domainObject.type))
	}
	
	protected def getDomainObjectProvider() {
		host.root.getDomainObjectProvider(JavaModelProvider)
	}
	
	override protected createChooser(XRapidButton button, Set<JavaPropertyDescriptor> availableChoiceKeys, Set<JavaPropertyDescriptor> unavailableChoiceKeys) {
		val chooser = new CarusselChooser(host, button.chooserPosition)
		availableChoiceKeys.forEachExt[
			chooser.addChoice(it.createNode, it)
		]
		chooser.connectionProvider = [
			host, choice, choiceInfo |
			val reference = choiceInfo as JavaPropertyDescriptor
			new XConnection(host, choice, reference) => [
				targetArrowHead = new LineArrowHead(it, false)
				new XConnectionLabel(it) => [
					text.text = reference.domainObject.name
				]
			]
		]
		chooser
	}
	
	override protected createButtons(XRapidButtonAction addConnectionAction) {
		#[	new XRapidButton(host, 0, 0.5, getArrowButton(LEFT, 'Discover properties'), addConnectionAction),
			new XRapidButton(host, 1, 0.5, getArrowButton(RIGHT, 'Discover properties'), addConnectionAction) ]
	}
}