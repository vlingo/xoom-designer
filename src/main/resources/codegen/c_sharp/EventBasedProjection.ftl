<#list imports as import>
using ${import.qualifiedClassName};
</#list>
using Vlingo.Xoom.Actors;
using Vlingo.Xoom.Lattice.Model.Projection;
using Vlingo.Xoom.Symbio.Store.State;
using Vlingo.Xoom.Turbo;

namespace ${packageName};

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/projections#implementing-with-the-statestoreprojectionactor">
 *   StateStoreProjectionActor
 * </a>
 */
public class ${projectionName} : StateStoreProjectionActor<${dataName}>
{

  private static readonly ${dataName} Empty = ${dataName}.Empty;

  public ${projectionName}() : this(ComponentRegistry.WithType<${storeProviderName}>().Store)
  {
  }

  public ${projectionName}(IStateStore stateStore) : base(stateStore)
  {
  }

  protected ${dataName} CurrentDataFor(IProjectable projectable)
  {
    return Empty;
  }

  protected ${dataName} Merge(${dataName} previousData, int previousVersion, ${dataName} currentData, int currentVersion)
  {

    if (previousVersion == currentVersion) return currentData;

    var merged = previousData;

    foreach (var @event in Sources) {
      switch (Enum.Parse<${projectionSourceTypesName}>(nameof(@event)))
      {
      <#list sources as source>
        case ${projectionSourceTypesName}.${source.name}:
        {
          var typedEvent = Typed<${source.name}>(@event);
          <#list source.dataObjectInitializers as initializer>
          ${initializer}
          </#list>
          <#list source.collectionMutations as collectionMutation>
          ${collectionMutation}
          </#list>
          merged = ${source.dataObjectName}.From(${source.mergeParameters});
          break;
        }

      </#list>
        default:
          Logger.Warn($"Event of type {nameof(@event)} was not matched.");
          break;
      }
    }

    return merged;
  }
}
