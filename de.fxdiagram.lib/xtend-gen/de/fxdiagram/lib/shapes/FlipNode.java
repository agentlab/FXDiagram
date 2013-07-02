package de.fxdiagram.lib.shapes;

import com.google.common.base.Objects;
import de.fxdiagram.core.transform.BoundsExtensions;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class FlipNode extends StackPane {
  private Node front;
  
  private Node back;
  
  private boolean isCurrentFront = true;
  
  private EventHandler<MouseEvent> clickHandler;
  
  public FlipNode() {
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
        public void handle(final MouseEvent event) {
          boolean _and = false;
          boolean _notEquals = (!Objects.equal(FlipNode.this.front, null));
          if (!_notEquals) {
            _and = false;
          } else {
            boolean _notEquals_1 = (!Objects.equal(FlipNode.this.back, null));
            _and = (_notEquals && _notEquals_1);
          }
          if (_and) {
            double _sceneX = event.getSceneX();
            double _sceneY = event.getSceneY();
            Point2D _point2D = new Point2D(_sceneX, _sceneY);
            final Point2D clickInScene = _point2D;
            Node _currentVisible = FlipNode.this.getCurrentVisible();
            final Point2D clickInLocal = _currentVisible.sceneToLocal(clickInScene);
            Bounds _boundsInLocal = FlipNode.this.getBoundsInLocal();
            final Point2D center = BoundsExtensions.center(_boundsInLocal);
            double _x = clickInLocal.getX();
            double _x_1 = center.getX();
            double _minus = (_x - _x_1);
            double _y = clickInLocal.getY();
            double _y_1 = center.getY();
            double _minus_1 = (_y - _y_1);
            Point3D _point3D = new Point3D(_minus, _minus_1, 0);
            final Point3D direction = _point3D;
            Point3D _xifexpression = null;
            double _x_2 = direction.getX();
            double _x_3 = direction.getX();
            double _multiply = (_x_2 * _x_3);
            double _y_2 = direction.getY();
            double _y_3 = direction.getY();
            double _multiply_1 = (_y_2 * _y_3);
            double _plus = (_multiply + _multiply_1);
            boolean _lessThan = (_plus < 1E-6);
            if (_lessThan) {
              Point3D _point3D_1 = new Point3D(1, 0, 0);
              _xifexpression = _point3D_1;
            } else {
              Point3D _xifexpression_1 = null;
              double _x_4 = direction.getX();
              double _abs = Math.abs(_x_4);
              double _y_4 = direction.getY();
              double _abs_1 = Math.abs(_y_4);
              boolean _greaterThan = (_abs > _abs_1);
              if (_greaterThan) {
                double _y_5 = direction.getY();
                Point3D _point3D_2 = new Point3D(0, _y_5, 0);
                _xifexpression_1 = _point3D_2;
              } else {
                double _x_5 = direction.getX();
                Point3D _point3D_3 = new Point3D(_x_5, 0, 0);
                _xifexpression_1 = _point3D_3;
              }
              _xifexpression = _xifexpression_1;
            }
            final Point3D turnAxis = _xifexpression;
            SequentialTransition _sequentialTransition = new SequentialTransition();
            final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
                public void apply(final SequentialTransition it) {
                  ObservableList<Animation> _children = it.getChildren();
                  RotateTransition _rotateTransition = new RotateTransition();
                  final Procedure1<RotateTransition> _function = new Procedure1<RotateTransition>() {
                      public void apply(final RotateTransition it) {
                        Node _currentVisible = FlipNode.this.getCurrentVisible();
                        it.setNode(_currentVisible);
                        Duration _millis = Duration.millis(250);
                        it.setDuration(_millis);
                        it.setAxis(turnAxis);
                        it.setFromAngle(0);
                        it.setToAngle(90);
                        final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                            public void handle(final ActionEvent it) {
                              ObservableList<Node> _children = FlipNode.this.getChildren();
                              Node _currentVisible = FlipNode.this.getCurrentVisible();
                              _children.remove(_currentVisible);
                              boolean _not = (!FlipNode.this.isCurrentFront);
                              FlipNode.this.isCurrentFront = _not;
                              ObservableList<Node> _children_1 = FlipNode.this.getChildren();
                              Node _currentVisible_1 = FlipNode.this.getCurrentVisible();
                              _children_1.add(_currentVisible_1);
                            }
                          };
                        it.setOnFinished(_function);
                      }
                    };
                  RotateTransition _doubleArrow = ObjectExtensions.<RotateTransition>operator_doubleArrow(_rotateTransition, _function);
                  _children.add(_doubleArrow);
                  ObservableList<Animation> _children_1 = it.getChildren();
                  RotateTransition _rotateTransition_1 = new RotateTransition();
                  final Procedure1<RotateTransition> _function_1 = new Procedure1<RotateTransition>() {
                      public void apply(final RotateTransition it) {
                        Node _currentInvisible = FlipNode.this.getCurrentInvisible();
                        it.setNode(_currentInvisible);
                        Duration _millis = Duration.millis(250);
                        it.setDuration(_millis);
                        it.setAxis(turnAxis);
                        it.setFromAngle(90);
                        it.setToAngle(0);
                      }
                    };
                  RotateTransition _doubleArrow_1 = ObjectExtensions.<RotateTransition>operator_doubleArrow(_rotateTransition_1, _function_1);
                  _children_1.add(_doubleArrow_1);
                  it.play();
                }
              };
            ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
          }
        }
      };
    this.clickHandler = _function;
    this.setOnMousePressed(this.clickHandler);
  }
  
  public boolean setFront(final Node front) {
    boolean _xblockexpression = false;
    {
      this.front = front;
      front.setOnMousePressed(this.clickHandler);
      ObservableList<Node> _children = this.getChildren();
      boolean _add = _children.add(front);
      _xblockexpression = (_add);
    }
    return _xblockexpression;
  }
  
  public void setBack(final Node back) {
    this.back = back;
    back.setOnMousePressed(this.clickHandler);
  }
  
  public Node getCurrentVisible() {
    Node _xifexpression = null;
    if (this.isCurrentFront) {
      _xifexpression = this.front;
    } else {
      _xifexpression = this.back;
    }
    return _xifexpression;
  }
  
  public Node getCurrentInvisible() {
    Node _xifexpression = null;
    if (this.isCurrentFront) {
      _xifexpression = this.back;
    } else {
      _xifexpression = this.front;
    }
    return _xifexpression;
  }
}