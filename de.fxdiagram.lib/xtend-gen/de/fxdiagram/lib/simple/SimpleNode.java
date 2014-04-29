package de.fxdiagram.lib.simple;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode
@SuppressWarnings("all")
public class SimpleNode extends XNode {
  private Text label;
  
  public SimpleNode(final DomainObjectDescriptor domainObject) {
    super(domainObject);
  }
  
  public SimpleNode(final String name) {
    super(name);
  }
  
  protected Node createNode() {
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
      public void apply(final RectangleBorderPane it) {
        ObservableList<Node> _children = it.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            it.setTextOrigin(VPos.TOP);
            String _name = SimpleNode.this.getName();
            it.setText(_name);
          }
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
        Text _label = SimpleNode.this.label = _doubleArrow;
        _children.add(_label);
        Insets _insets = new Insets(10, 20, 10, 20);
        StackPane.setMargin(SimpleNode.this.label, _insets);
        InnerShadow _innerShadow = new InnerShadow();
        final Procedure1<InnerShadow> _function_1 = new Procedure1<InnerShadow>() {
          public void apply(final InnerShadow it) {
            it.setRadius(7);
          }
        };
        InnerShadow _doubleArrow_1 = ObjectExtensions.<InnerShadow>operator_doubleArrow(_innerShadow, _function_1);
        it.setEffect(_doubleArrow_1);
      }
    };
    return ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
  }
  
  public void doActivate() {
    super.doActivate();
    String _name = this.getName();
    this.label.setText(_name);
  }
  
  protected Anchors createAnchors() {
    return new RoundedRectangleAnchors(this, 12, 12);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public SimpleNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
