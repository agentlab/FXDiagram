package de.fxdiagram.core.anchors;

import com.google.common.collect.Lists;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.model.ModelElementImpl;
import java.util.Collections;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode({ "width", "height", "stroke", "fill" })
@SuppressWarnings("all")
public class DiamondArrowHead extends ArrowHead {
  public DiamondArrowHead(final XConnection connection, final double width, final double height, final Property<Paint> strokeProperty, final Property<Paint> fillProperty, final boolean isSource) {
    this.setConnection(connection);
    this.setIsSource(isSource);
    this.setWidth(width);
    this.setHeight(height);
    this.strokeProperty.bind(strokeProperty);
    this.fillProperty.bind(fillProperty);
    this.activatePreview();
  }
  
  public DiamondArrowHead(final XConnection connection, final boolean isSource) {
    this(connection, 10, 10, connection.strokeProperty(), connection.strokeProperty(), isSource);
  }
  
  public void doActivatePreview() {
    Polygon _polygon = new Polygon();
    final Procedure1<Polygon> _function = new Procedure1<Polygon>() {
      public void apply(final Polygon it) {
        ObservableList<Double> _points = it.getPoints();
        double _width = DiamondArrowHead.this.getWidth();
        double _multiply = (0.5 * _width);
        double _height = DiamondArrowHead.this.getHeight();
        double _multiply_1 = ((-0.5) * _height);
        double _width_1 = DiamondArrowHead.this.getWidth();
        double _width_2 = DiamondArrowHead.this.getWidth();
        double _multiply_2 = (0.5 * _width_2);
        double _height_1 = DiamondArrowHead.this.getHeight();
        double _multiply_3 = (0.5 * _height_1);
        _points.setAll(
          Collections.<Double>unmodifiableList(Lists.<Double>newArrayList(Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(_multiply), Double.valueOf(_multiply_1), Double.valueOf(_width_1), Double.valueOf(0.0), Double.valueOf(_multiply_2), Double.valueOf(_multiply_3))));
        ObjectProperty<Paint> _fillProperty = it.fillProperty();
        _fillProperty.bind(DiamondArrowHead.this.fillProperty);
        ObjectProperty<Paint> _strokeProperty = it.strokeProperty();
        _strokeProperty.bind(DiamondArrowHead.this.strokeProperty);
        DoubleProperty _strokeWidthProperty = it.strokeWidthProperty();
        XConnection _connection = DiamondArrowHead.this.getConnection();
        DoubleProperty _strokeWidthProperty_1 = _connection.strokeWidthProperty();
        _strokeWidthProperty.bind(_strokeWidthProperty_1);
        it.setStrokeType(StrokeType.CENTERED);
      }
    };
    Polygon _doubleArrow = ObjectExtensions.<Polygon>operator_doubleArrow(_polygon, _function);
    this.setNode(_doubleArrow);
  }
  
  public double getLineCut() {
    double _width = this.getWidth();
    XConnection _connection = this.getConnection();
    double _strokeWidth = _connection.getStrokeWidth();
    return (_width + _strokeWidth);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public DiamondArrowHead() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(widthProperty, Double.class);
    modelElement.addProperty(heightProperty, Double.class);
    modelElement.addProperty(strokeProperty, Paint.class);
    modelElement.addProperty(fillProperty, Paint.class);
  }
  
  private SimpleDoubleProperty widthProperty = new SimpleDoubleProperty(this, "width");
  
  public double getWidth() {
    return this.widthProperty.get();
  }
  
  public void setWidth(final double width) {
    this.widthProperty.set(width);
  }
  
  public DoubleProperty widthProperty() {
    return this.widthProperty;
  }
  
  private SimpleDoubleProperty heightProperty = new SimpleDoubleProperty(this, "height");
  
  public double getHeight() {
    return this.heightProperty.get();
  }
  
  public void setHeight(final double height) {
    this.heightProperty.set(height);
  }
  
  public DoubleProperty heightProperty() {
    return this.heightProperty;
  }
  
  private SimpleObjectProperty<Paint> strokeProperty = new SimpleObjectProperty<Paint>(this, "stroke");
  
  public Paint getStroke() {
    return this.strokeProperty.get();
  }
  
  public void setStroke(final Paint stroke) {
    this.strokeProperty.set(stroke);
  }
  
  public ObjectProperty<Paint> strokeProperty() {
    return this.strokeProperty;
  }
  
  private SimpleObjectProperty<Paint> fillProperty = new SimpleObjectProperty<Paint>(this, "fill");
  
  public Paint getFill() {
    return this.fillProperty.get();
  }
  
  public void setFill(final Paint fill) {
    this.fillProperty.set(fill);
  }
  
  public ObjectProperty<Paint> fillProperty() {
    return this.fillProperty;
  }
}
