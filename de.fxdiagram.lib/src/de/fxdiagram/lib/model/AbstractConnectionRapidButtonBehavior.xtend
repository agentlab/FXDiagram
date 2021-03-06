package de.fxdiagram.lib.model

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.core.extensions.InitializingListListener
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.tools.AbstractChooser
import java.util.List
import java.util.Set

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import de.fxdiagram.core.XRapidButtonAction

abstract class AbstractConnectionRapidButtonBehavior<HOST extends XNode, MODEL, KEY extends DomainObjectDescriptor> extends AbstractHostBehavior<HOST> {
	
	Set<KEY> availableChoiceKeys = newLinkedHashSet	
	Set<KEY> unavailableChoiceKeys = newHashSet
	
	List<XRapidButton> buttons = newArrayList
	
	new(HOST host) {
		super(host)
	}
	
	override getBehaviorKey() {
		class
	}
	
	override protected doActivate() {
		availableChoiceKeys += initialModelChoices.map[choiceKey]
		if(!availableChoiceKeys.empty) {
			val XRapidButtonAction addConnectionAction = [
				XRapidButton button |
				val chooser = createChooser(button, availableChoiceKeys, unavailableChoiceKeys)
				host.root.currentTool = chooser
			]
			buttons += createButtons(addConnectionAction)
			host.diagram.buttons += buttons
			host.diagram.connections.addInitializingListener(new InitializingListListener() => [
				add = [ XConnection it |
					if(availableChoiceKeys.remove(domainObject)) {
						if(availableChoiceKeys.empty)						
							host.diagram.buttons -= buttons
						unavailableChoiceKeys.add(domainObject as KEY)
					}
				]
				remove = [ XConnection it |
					if(unavailableChoiceKeys.remove(domainObject)) {
						if(availableChoiceKeys.empty) 
							host.diagram.buttons += buttons
						availableChoiceKeys.add(domainObject as KEY)
					} 
				]
			])
		}
	}	
	
	protected def Iterable<MODEL> getInitialModelChoices() 
	
	protected def KEY getChoiceKey(MODEL model)
	
	protected def XNode createNode(KEY key)

	protected def Iterable<XRapidButton> createButtons(XRapidButtonAction addConnectionAction) 
		
	protected def AbstractChooser createChooser(XRapidButton button, Set<KEY> availableChoiceKeys, Set<KEY> unavailableChoiceKeys) 
	
}

