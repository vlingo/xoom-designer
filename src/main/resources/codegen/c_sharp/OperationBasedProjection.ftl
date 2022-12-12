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
 *   Implementing With the StateStoreProjectionActor
 * </a>
 */
public class ${projectionName} : StateStoreProjectionActor<${dataName}>
{
  private string _becauseOf;

  public ${projectionName}() : this(ComponentRegistry.WithType<${storeProviderName}>().Store)
  {
  }

  public ${projectionName}(IStateStore stateStore) : base(stateStore)
  {
  }

  protected ${dataName} CurrentDataFor(IProjectable projectable)
  {
    _becauseOf = projectable.BecauseOf()[0];
    var state = projectable.Object<${stateName}>();
    return ${dataName}.From(state);
  }

  protected ${dataName} Merge(${dataName} previousData, int previousVersion, ${dataName} currentData, int currentVersion)
  {
    if (previousVersion == currentVersion) return currentData;

    var merged = previousData;

    switch (Enum.Parse<${projectionSourceTypesName}>(_becauseOf))
    {
      <#list sources as source>
      case ${projectionSourceTypesName}.${source.name}:
      {
        merged = ${source.dataObjectName}.From(${source.mergeParameters});
        break;
      }

      </#list>
      default:
        Logger.Warn("Operation of type " + _becauseOf + " was not matched.");
        break;
    }

    return merged;
  }
}
