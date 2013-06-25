package de.itemis.javafx.diagram.tools

import com.google.common.base.Charsets
import com.google.common.io.Files
import de.itemis.javafx.diagram.XRootDiagram
import de.itemis.javafx.diagram.export.SvgExporter
import java.io.File
import javafx.event.EventHandler
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import de.itemis.javafx.diagram.layout.Layouter
import static extension javafx.util.Duration.*

class KeyTool implements XDiagramTool {

	XRootDiagram diagram
	
	EventHandler<KeyEvent> keyHandler

	new(XRootDiagram diagram) {
		this.diagram = diagram
		keyHandler = [
			switch code {
				case KeyCode.E: 
					if(shortcutDown) {
						val svgCode = new SvgExporter().toSvg(diagram)
						Files.write(svgCode, new File("Diagram.svg"), Charsets.UTF_8)
						consume
					}
				case KeyCode.L: 
					if(shortcutDown) {
						new Layouter().layout(diagram, 2.seconds)
					}
			}
		]
	}	
	
	override activate() {
		diagram.scene.addEventHandler(KeyEvent.KEY_PRESSED, keyHandler)
		true
	}
	
	override deactivate() {
		diagram.scene.removeEventHandler(KeyEvent.KEY_PRESSED, keyHandler)
		true
	}
	
}