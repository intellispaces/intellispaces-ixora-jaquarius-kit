package tech.intellispaces.ixora.http.engine;

import tech.intellispaces.commons.action.conditional.ConditionalActions;
import tech.intellispaces.commons.base.entity.Reference;
import tech.intellispaces.commons.base.entity.References;
import tech.intellispaces.commons.base.exception.NotImplementedExceptions;
import tech.intellispaces.commons.base.type.Classes;
import tech.intellispaces.ixora.http.HttpRequestHandle;
import tech.intellispaces.ixora.http.HttpResponseHandle;
import tech.intellispaces.ixora.http.pathtree.FinalExecutor;
import tech.intellispaces.ixora.http.pathtree.PathSegment;
import tech.intellispaces.ixora.http.pathtree.PathTreeFunctions;
import tech.intellispaces.ixora.http.port.PortDescriptor;

import java.util.List;

public class DefaultHttpPortEngine implements HttpPortEngine {

  @Override
  public Reference<?> bridge(Object port, Class<?> portDomain) {
    return References.get(new PortDescriptor(port, portDomain));
  }

  @Override
  public HttpResponseHandle exchange(Reference<?> reference, HttpRequestHandle request) {
    var descriptor = (PortDescriptor) reference.get();

    List<PathSegment> rootSegments = ConditionalActions.getOrSetIfAbsentAction(
        PortDescriptor.class, Classes.ofList(PathSegment.class)
    ).execute(
        descriptor,
        PortDescriptor::rootSegments,
        PortDescriptor::setRootSegments,
        () -> PathTreeFunctions.readPathTree(descriptor.port(), descriptor.portDomain())
    );

    FinalExecutor executor = PathTreeFunctions.findExecution(request, rootSegments);
    if (executor == null) {
      throw NotImplementedExceptions.withCode("YNScfA");
    }
    return executor.exchange(descriptor.port(), request);
  }
}
