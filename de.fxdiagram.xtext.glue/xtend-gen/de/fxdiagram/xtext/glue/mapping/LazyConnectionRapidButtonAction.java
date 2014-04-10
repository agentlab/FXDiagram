package de.fxdiagram.xtext.glue.mapping;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRapidButtonAction;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.tools.AbstractChooser;
import de.fxdiagram.core.tools.ChooserConnectionProvider;
import de.fxdiagram.lib.tools.CarusselChooser;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import de.fxdiagram.xtext.glue.mapping.LazyConnectionMappingBehavior;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import de.fxdiagram.xtext.glue.mapping.NodeMappingCall;
import de.fxdiagram.xtext.glue.mapping.OpenElementInEditorBehavior;
import de.fxdiagram.xtext.glue.mapping.XDiagramProvider;
import java.util.List;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LazyConnectionRapidButtonAction<MODEL extends Object, ARG extends Object> extends XRapidButtonAction {
  private XDiagramProvider diagramProvider;
  
  private AbstractConnectionMappingCall<MODEL,ARG> mappingCall;
  
  private boolean hostIsSource;
  
  public LazyConnectionRapidButtonAction(final AbstractConnectionMappingCall<MODEL,ARG> mappingCall, final XDiagramProvider diagramProvider, final boolean hostIsSource) {
    this.mappingCall = mappingCall;
    this.diagramProvider = diagramProvider;
    this.hostIsSource = hostIsSource;
  }
  
  public boolean isEnabled(final XRapidButton button) {
    Boolean _xblockexpression = null;
    {
      XNode _host = button.getHost();
      DomainObjectDescriptor _domainObject = _host.getDomainObject();
      final XtextDomainObjectDescriptor<ARG> hostDescriptor = ((XtextDomainObjectDescriptor<ARG>) _domainObject);
      XNode _host_1 = button.getHost();
      XDiagram _diagram = CoreExtensions.getDiagram(_host_1);
      ObservableList<XConnection> _connections = _diagram.getConnections();
      final Function1<XConnection,DomainObjectDescriptor> _function = new Function1<XConnection,DomainObjectDescriptor>() {
        public DomainObjectDescriptor apply(final XConnection it) {
          return it.getDomainObject();
        }
      };
      List<DomainObjectDescriptor> _map = ListExtensions.<XConnection, DomainObjectDescriptor>map(_connections, _function);
      final Set<DomainObjectDescriptor> existingConnectionDescriptors = IterableExtensions.<DomainObjectDescriptor>toSet(_map);
      final Function1<ARG,Boolean> _function_1 = new Function1<ARG,Boolean>() {
        public Boolean apply(final ARG domainArgument) {
          final List<MODEL> connectionDomainObjects = LazyConnectionRapidButtonAction.this.diagramProvider.<MODEL, ARG>select(LazyConnectionRapidButtonAction.this.mappingCall, domainArgument);
          for (final MODEL connectionDomainObject : connectionDomainObjects) {
            {
              ConnectionMapping<MODEL> _connectionMapping = LazyConnectionRapidButtonAction.this.mappingCall.getConnectionMapping();
              final XtextDomainObjectDescriptor<MODEL> connectionDescriptor = LazyConnectionRapidButtonAction.this.diagramProvider.<MODEL>getDescriptor(connectionDomainObject, _connectionMapping);
              boolean _add = existingConnectionDescriptors.add(connectionDescriptor);
              if (_add) {
                NodeMappingCall<?,MODEL> _elvis = null;
                ConnectionMapping<MODEL> _connectionMapping_1 = LazyConnectionRapidButtonAction.this.mappingCall.getConnectionMapping();
                NodeMappingCall<?,MODEL> _source = _connectionMapping_1.getSource();
                if (_source != null) {
                  _elvis = _source;
                } else {
                  ConnectionMapping<MODEL> _connectionMapping_2 = LazyConnectionRapidButtonAction.this.mappingCall.getConnectionMapping();
                  NodeMappingCall<?,MODEL> _target = _connectionMapping_2.getTarget();
                  _elvis = _target;
                }
                final NodeMappingCall<?,MODEL> nodeMappingCall = _elvis;
                final List<?> nodeDomainObjects = LazyConnectionRapidButtonAction.this.diagramProvider.select(nodeMappingCall, connectionDomainObject);
                boolean _isEmpty = nodeDomainObjects.isEmpty();
                return Boolean.valueOf((!_isEmpty));
              }
            }
          }
          return Boolean.valueOf(false);
        }
      };
      _xblockexpression = hostDescriptor.<Boolean>withDomainObject(_function_1);
    }
    return (_xblockexpression).booleanValue();
  }
  
  public void perform(final XRapidButton button) {
    final AbstractChooser chooser = this.createChooser(button);
    XNode _host = button.getHost();
    this.populateChooser(chooser, _host);
    XNode _host_1 = button.getHost();
    XRoot _root = CoreExtensions.getRoot(_host_1);
    _root.setCurrentTool(chooser);
  }
  
  protected AbstractChooser createChooser(final XRapidButton button) {
    AbstractChooser _xblockexpression = null;
    {
      final Pos position = button.getChooserPosition();
      AbstractChooser _xifexpression = null;
      VPos _vpos = position.getVpos();
      boolean _equals = Objects.equal(_vpos, VPos.CENTER);
      if (_equals) {
        XNode _host = button.getHost();
        _xifexpression = new CarusselChooser(_host, position);
      } else {
        XNode _host_1 = button.getHost();
        _xifexpression = new CoverFlowChooser(_host_1, position);
      }
      final AbstractChooser chooser = _xifexpression;
      _xblockexpression = chooser;
    }
    return _xblockexpression;
  }
  
  protected Object populateChooser(final AbstractChooser chooser, final XNode host) {
    Object _xblockexpression = null;
    {
      DomainObjectDescriptor _domainObject = host.getDomainObject();
      final XtextDomainObjectDescriptor<ARG> hostDescriptor = ((XtextDomainObjectDescriptor<ARG>) _domainObject);
      XDiagram _diagram = CoreExtensions.getDiagram(host);
      ObservableList<XConnection> _connections = _diagram.getConnections();
      final Function1<XConnection,DomainObjectDescriptor> _function = new Function1<XConnection,DomainObjectDescriptor>() {
        public DomainObjectDescriptor apply(final XConnection it) {
          return it.getDomainObject();
        }
      };
      List<DomainObjectDescriptor> _map = ListExtensions.<XConnection, DomainObjectDescriptor>map(_connections, _function);
      final Set<DomainObjectDescriptor> existingConnectionDescriptors = IterableExtensions.<DomainObjectDescriptor>toSet(_map);
      final Function1<ARG,Object> _function_1 = new Function1<ARG,Object>() {
        public Object apply(final ARG domainArgument) {
          Object _xblockexpression = null;
          {
            final List<MODEL> connectionDomainObjects = LazyConnectionRapidButtonAction.this.diagramProvider.<MODEL, ARG>select(LazyConnectionRapidButtonAction.this.mappingCall, domainArgument);
            final Procedure1<MODEL> _function = new Procedure1<MODEL>() {
              public void apply(final MODEL connectionDomainObject) {
                ConnectionMapping<MODEL> _connectionMapping = LazyConnectionRapidButtonAction.this.mappingCall.getConnectionMapping();
                final XtextDomainObjectDescriptor<MODEL> connectionDescriptor = LazyConnectionRapidButtonAction.this.diagramProvider.<MODEL>getDescriptor(connectionDomainObject, _connectionMapping);
                boolean _add = existingConnectionDescriptors.add(connectionDescriptor);
                if (_add) {
                  NodeMappingCall<?,MODEL> _elvis = null;
                  ConnectionMapping<MODEL> _connectionMapping_1 = LazyConnectionRapidButtonAction.this.mappingCall.getConnectionMapping();
                  NodeMappingCall<?,MODEL> _source = _connectionMapping_1.getSource();
                  if (_source != null) {
                    _elvis = _source;
                  } else {
                    ConnectionMapping<MODEL> _connectionMapping_2 = LazyConnectionRapidButtonAction.this.mappingCall.getConnectionMapping();
                    NodeMappingCall<?,MODEL> _target = _connectionMapping_2.getTarget();
                    _elvis = _target;
                  }
                  final NodeMappingCall<?,MODEL> nodeMappingCall = _elvis;
                  final List<?> nodeDomainObjects = LazyConnectionRapidButtonAction.this.diagramProvider.select(nodeMappingCall, connectionDomainObject);
                  final Procedure1<Object> _function = new Procedure1<Object>() {
                    public void apply(final Object it) {
                      NodeMapping<?> _nodeMapping = nodeMappingCall.getNodeMapping();
                      XNode _createNode = LazyConnectionRapidButtonAction.this.<Object>createNode(it, _nodeMapping);
                      chooser.addChoice(_createNode, connectionDescriptor);
                    }
                  };
                  IterableExtensions.forEach(nodeDomainObjects, _function);
                }
              }
            };
            IterableExtensions.<MODEL>forEach(connectionDomainObjects, _function);
            final ChooserConnectionProvider _function_1 = new ChooserConnectionProvider() {
              public XConnection getConnection(final XNode thisNode, final XNode thatNode, final DomainObjectDescriptor connectionDesc) {
                XConnection _xblockexpression = null;
                {
                  final XtextDomainObjectDescriptor<MODEL> descriptor = ((XtextDomainObjectDescriptor<MODEL>) connectionDesc);
                  ConnectionMapping<MODEL> _connectionMapping = LazyConnectionRapidButtonAction.this.mappingCall.getConnectionMapping();
                  Function1<? super XtextDomainObjectDescriptor<MODEL>,? extends XConnection> _createConnection = _connectionMapping.getCreateConnection();
                  XConnection _apply = _createConnection.apply(descriptor);
                  final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
                    public void apply(final XConnection it) {
                      OpenElementInEditorBehavior _openElementInEditorBehavior = new OpenElementInEditorBehavior(it);
                      it.addBehavior(_openElementInEditorBehavior);
                      if (LazyConnectionRapidButtonAction.this.hostIsSource) {
                        it.setSource(thisNode);
                        it.setTarget(thatNode);
                      } else {
                        it.setSource(thatNode);
                        it.setTarget(thisNode);
                      }
                    }
                  };
                  _xblockexpression = ObjectExtensions.<XConnection>operator_doubleArrow(_apply, _function);
                }
                return _xblockexpression;
              }
            };
            chooser.setConnectionProvider(_function_1);
            _xblockexpression = null;
          }
          return _xblockexpression;
        }
      };
      _xblockexpression = hostDescriptor.<Object>withDomainObject(_function_1);
    }
    return _xblockexpression;
  }
  
  protected <NODE extends Object> XNode createNode(final Object nodeDomainObject, final NodeMapping<?> nodeMapping) {
    XNode _xifexpression = null;
    boolean _isApplicable = nodeMapping.isApplicable(nodeDomainObject);
    if (_isApplicable) {
      XNode _xblockexpression = null;
      {
        final NodeMapping<NODE> nodeMappingCasted = ((NodeMapping<NODE>) nodeMapping);
        final XtextDomainObjectDescriptor<NODE> descriptor = this.diagramProvider.<NODE>getDescriptor(((NODE) nodeDomainObject), nodeMappingCasted);
        Function1<? super XtextDomainObjectDescriptor<NODE>,? extends XNode> _createNode = nodeMappingCasted.getCreateNode();
        final XNode node = _createNode.apply(descriptor);
        OpenElementInEditorBehavior _openElementInEditorBehavior = new OpenElementInEditorBehavior(node);
        node.addBehavior(_openElementInEditorBehavior);
        List<AbstractConnectionMappingCall<?,NODE>> _outgoing = nodeMappingCasted.getOutgoing();
        final Function1<AbstractConnectionMappingCall<?,NODE>,Boolean> _function = new Function1<AbstractConnectionMappingCall<?,NODE>,Boolean>() {
          public Boolean apply(final AbstractConnectionMappingCall<?,NODE> it) {
            return Boolean.valueOf(it.isLazy());
          }
        };
        Iterable<AbstractConnectionMappingCall<?,NODE>> _filter = IterableExtensions.<AbstractConnectionMappingCall<?,NODE>>filter(_outgoing, _function);
        final Procedure1<AbstractConnectionMappingCall<?,NODE>> _function_1 = new Procedure1<AbstractConnectionMappingCall<?,NODE>>() {
          public void apply(final AbstractConnectionMappingCall<?,NODE> it) {
            LazyConnectionMappingBehavior<?,NODE> _lazyConnectionMappingBehavior = new LazyConnectionMappingBehavior(node, it, LazyConnectionRapidButtonAction.this.diagramProvider, true);
            node.addBehavior(_lazyConnectionMappingBehavior);
          }
        };
        IterableExtensions.<AbstractConnectionMappingCall<?,NODE>>forEach(_filter, _function_1);
        List<AbstractConnectionMappingCall<?,NODE>> _incoming = nodeMappingCasted.getIncoming();
        final Function1<AbstractConnectionMappingCall<?,NODE>,Boolean> _function_2 = new Function1<AbstractConnectionMappingCall<?,NODE>,Boolean>() {
          public Boolean apply(final AbstractConnectionMappingCall<?,NODE> it) {
            return Boolean.valueOf(it.isLazy());
          }
        };
        Iterable<AbstractConnectionMappingCall<?,NODE>> _filter_1 = IterableExtensions.<AbstractConnectionMappingCall<?,NODE>>filter(_incoming, _function_2);
        final Procedure1<AbstractConnectionMappingCall<?,NODE>> _function_3 = new Procedure1<AbstractConnectionMappingCall<?,NODE>>() {
          public void apply(final AbstractConnectionMappingCall<?,NODE> it) {
            LazyConnectionMappingBehavior<?,NODE> _lazyConnectionMappingBehavior = new LazyConnectionMappingBehavior(node, it, LazyConnectionRapidButtonAction.this.diagramProvider, false);
            node.addBehavior(_lazyConnectionMappingBehavior);
          }
        };
        IterableExtensions.<AbstractConnectionMappingCall<?,NODE>>forEach(_filter_1, _function_3);
        _xblockexpression = node;
      }
      _xifexpression = _xblockexpression;
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
}
