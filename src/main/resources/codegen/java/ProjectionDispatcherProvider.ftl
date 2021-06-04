package ${packageName};

import java.util.Arrays;
import java.util.List;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Protocols;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.lattice.model.projection.ProjectionDispatcher;
import io.vlingo.xoom.lattice.model.projection.ProjectionDispatcher.ProjectToDescription;
import io.vlingo.xoom.lattice.model.projection.TextProjectionDispatcherActor;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.turbo.ComponentRegistry;

<#if imports?has_content>
<#list imports as import>
import ${import.qualifiedClassName};
</#list>
</#if>

@SuppressWarnings("all")
public class ProjectionDispatcherProvider {

  public final ProjectionDispatcher projectionDispatcher;
  public final Dispatcher storeDispatcher;

  public static ProjectionDispatcherProvider using(final Stage stage) {
    if (ComponentRegistry.has(ProjectionDispatcherProvider.class)) {
      return ComponentRegistry.withType(ProjectionDispatcherProvider.class);
    }

    final List<ProjectToDescription> descriptions =
            Arrays.asList(
<#list projectToDescriptions as projectToDescription>
                    ${projectToDescription.initializationCommand}
</#list>
                    );

    final Protocols dispatcherProtocols =
            stage.actorFor(
                    new Class<?>[] { Dispatcher.class, ProjectionDispatcher.class },
                    Definition.has(TextProjectionDispatcherActor.class, Definition.parameters(descriptions)));

    final Protocols.Two<Dispatcher, ProjectionDispatcher> dispatchers = Protocols.two(dispatcherProtocols);

    return new ProjectionDispatcherProvider(dispatchers._1, dispatchers._2);
  }

  private ProjectionDispatcherProvider(final Dispatcher storeDispatcher, final ProjectionDispatcher projectionDispatcher) {
    this.storeDispatcher = storeDispatcher;
    this.projectionDispatcher = projectionDispatcher;
    ComponentRegistry.register(getClass(), this);
  }
}
