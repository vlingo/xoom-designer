<#if imports?has_content>
<#list imports as import>
using ${import.qualifiedClassName};
</#list>
</#if>
using Vlingo.Xoom.Actors;
using Vlingo.Xoom.Lattice.Model.Projection;
using Vlingo.Xoom.Turbo;

namespace ${packageName};

public class ProjectionDispatcherProvider
{

  public IProjectionDispatcher ProjectionDispatcher { get; }
  public IDispatcher StoreDispatcher { get; }

  public static ProjectionDispatcherProvider Using(Stage stage)
  {
    if (ComponentRegistry.Has<ProjectionDispatcherProvider>()) {
      return ComponentRegistry.WithType<ProjectionDispatcherProvider>();
    }

    var descriptions = new List<ProjectToDescription>{
<#list projectToDescriptions as projectToDescription>
                    ${projectToDescription.initializationCommand}
</#list>
                    };

    var dispatcherProtocols = stage.ActorFor(new [] { typeof(IDispatcher), typeof(IProjectionDispatcher) },
                    Definition.Has<TextProjectionDispatcherActor>(Definition.Parameters(descriptions)));

    var dispatchers = Protocols.Two<IDispatcher, IProjectionDispatcher>(dispatcherProtocols);

    return new ProjectionDispatcherProvider(dispatchers._1, dispatchers._2);
  }

  private ProjectionDispatcherProvider(IDispatcher storeDispatcher, IProjectionDispatcher projectionDispatcher)
  {
    this.StoreDispatcher = storeDispatcher;
    this.ProjectionDispatcher = projectionDispatcher;
    ComponentRegistry.Register<ProjectionDispatcherProvider>(this);
  }
}
