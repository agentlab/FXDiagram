package de.fxdiagram.core

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.command.CommandStack
import de.fxdiagram.core.model.DomainObjectProvider
import de.fxdiagram.core.model.DomainObjectProviderWithState
import de.fxdiagram.core.tools.CompositeTool
import de.fxdiagram.core.tools.DiagramActionTool
import de.fxdiagram.core.tools.DiagramGestureTool
import de.fxdiagram.core.tools.SelectionTool
import de.fxdiagram.core.tools.XDiagramTool
import de.fxdiagram.core.tools.actions.DiagramActionRegistry
import java.util.List
import java.util.Map
import javafx.beans.Observable
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.layout.Pane

import static extension de.fxdiagram.annotations.ForeachExtensions.*
import static extension de.fxdiagram.core.css.JavaToCss.*

@Logging
@ModelNode('domainObjectProviders', 'rootDiagram', 'diagram')
class XRoot extends Parent implements XActivatable {
	
	@FxProperty(readOnly) boolean isActive

	@FxProperty(readOnly) XDiagram rootDiagram

	@FxProperty(readOnly) XDiagram diagram

	@FxProperty ObservableList<DomainObjectProvider> domainObjectProviders = FXCollections.observableArrayList
	
	DiagramActionRegistry diagramActionRegistry = new DiagramActionRegistry

	HeadsUpDisplay headsUpDisplay = new HeadsUpDisplay
	
	Pane diagramCanvas = new Pane
	
	List<XDiagramTool> tools = newArrayList
	
	CompositeTool defaultTool
	
	XDiagramTool currentTool
	
	Map<Class<? extends DomainObjectProvider>, DomainObjectProvider> domainObjectProviderCache
	
	CommandStack commandStack = new CommandStack(this)
	
	new() {
		children += diagramCanvas
		children += headsUpDisplay
		domainObjectProviders.addListener[Observable o | domainObjectProviderCache = null]
	}
	
	def setRootDiagram(XDiagram rootDiagram) {
		this.rootDiagramProperty.set(rootDiagram)
		this.diagram = rootDiagram		
	}
	
	def setDiagram(XDiagram newDiagram) {
		if(diagram != null) {
			diagramCanvas.children -= diagram
			headsUpDisplay.children -= diagram.fixedButtons.keySet			
		}
		diagramProperty.set(newDiagram)
		diagramCanvas.children += newDiagram
		if(isActive)
			newDiagram.activate
		diagramCanvas.style = '''
			-fx-background-color: «newDiagram.backgroundPaint.toCss»;
			-fx-text-fill: «newDiagram.foregroundPaint.toCss»;
		'''
		diagram.fixedButtons.entrySet.forEachExt[headsUpDisplay.add(key, value)]
		newDiagram.centerDiagram(false)
	}
	
	def getHeadsUpDisplay() {
		headsUpDisplay
	}
	
	def getDiagramCanvas() {
		diagramCanvas
	}
	
	def getViewportTransform() {
		diagram.viewportTransform
	}
	
	override activate() {		
		if(!isActive)
			doActivate
		isActiveProperty.set(true)
	}

	def	doActivate() {
		defaultTool = new CompositeTool
		defaultTool += new SelectionTool(this)
		defaultTool += new DiagramGestureTool(this)
		defaultTool += new DiagramActionTool(this)
		tools += defaultTool
		diagram?.activate
		diagramCanvas => [
			prefWidthProperty.bind(scene.widthProperty)
			prefHeightProperty.bind(scene.heightProperty)
		]
		setCurrentTool(defaultTool)
	}
	
	def setCurrentTool(XDiagramTool tool) {
		var previousTool = currentTool
		if(previousTool != null) {
			if(!previousTool.deactivate)
				LOG.severe("Could not deactivate active tool")
		}
		currentTool = tool
		if(tool != null) {
			if(!tool.activate) {
				currentTool = previousTool
				if(!previousTool?.activate)
					LOG.severe("Could not reactivate tool")
			}
		}
	}
	
	def restoreDefaultTool() {
		setCurrentTool(defaultTool)
	}
	
	def getDiagramActionRegistry() {
		diagramActionRegistry
	}
	
	def getCurrentSelection() {
		diagram.allShapes.filter[isSelectable && selected]
	}
	
	def <T extends DomainObjectProvider> T getDomainObjectProvider(Class<T> providerClazz) {
		if(domainObjectProviderCache == null) {
			domainObjectProviderCache = newHashMap
			domainObjectProviders.forEachExt[domainObjectProviderCache.put(class, it)]
		}
		domainObjectProviderCache.get(providerClazz) as T
	}
	
	def replaceDomainObjectProviders(List<DomainObjectProvider> newDomainObjectProviders) {
		newDomainObjectProviders.forEachExt[ newProvider |
			val oldProvider = getDomainObjectProvider(newProvider.class)
			if(oldProvider != null) {
				domainObjectProviders.set(domainObjectProviders.indexOf(oldProvider), newProvider)
				if(newProvider instanceof DomainObjectProviderWithState)
					newProvider.copyState(oldProvider as DomainObjectProviderWithState)				
			} else {
				domainObjectProviders.add(newProvider)
			} 
		]
	}
	
	def getCommandStack() {
		commandStack
	}
}

class HeadsUpDisplay extends Group {
	
	Map<Node, Pos> alignments = newHashMap

	ChangeListener<Number> sceneListener

	new() {
		sceneListener = [
			property, oldVlaue, newValue |
			children.forEach [ place ]
		]
		sceneProperty.addListener [
			property, oldVal, newVal |
			oldVal?.widthProperty?.removeListener(sceneListener)			
			oldVal?.heightProperty?.removeListener(sceneListener)			
			newVal?.widthProperty?.addListener(sceneListener)			
			newVal?.heightProperty?.addListener(sceneListener)			
		]
	}
	
	def add(Node child, Pos pos) {
		if(!children.contains(child)) {
			children += child
			alignments.put(child, pos)
			child.place
			child.boundsInParentProperty.addListener [
				property, oldValue, newValue | 
				if(child.parent != this) 
					property.removeListener(self)
				else
					child.place
			]
		}
	} 
	
	protected def place(Node child) {
		val pos = alignments.get(child) ?: Pos.CENTER
		val bounds = child.boundsInParent
		child.layoutX = switch(pos.hpos) {
			case HPos.LEFT:
				0
			case HPos.RIGHT:
				child.scene.width - bounds.width
			default:
				0.5 * (child.scene.width - bounds.width) 
		}
		child.layoutY = switch(pos.vpos) {
			case VPos.TOP:
				0
			case VPos.BOTTOM:
				child.scene.height - bounds.height
			default:
				0.5 * (child.scene.height - bounds.height) 
		}
	}
}

