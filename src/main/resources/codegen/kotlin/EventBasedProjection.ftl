package ${packageName}

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

import io.vlingo.xoom.lattice.model.projection.Projectable
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor
import io.vlingo.xoom.symbio.Source

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/projections#implementing-with-the-statestoreprojectionactor">
 *   StateStoreProjectionActor
 * </a>
 */
public class ${projectionName} : StateStoreProjectionActor<${dataName}> {
  companion object {
    val Empty = ${dataName}.empty()
  }

  public constructor() : super(${storeProviderName}.instance().store) {

  }

  protected override fun currentDataFor(projectable: Projectable): ${dataName} {
    return Empty
  }

  protected override fun merge(previousData: ${dataName}, previousVersion: Int, currentData: ${dataName}, currentVersion: Int): ${dataName} {

    if (previousData == null) {
      previousData = currentData
    }

    for (event in sources()) {
      when (${projectionSourceTypesName}.valueOf(event.typeName())) {
      <#list sourceNames as source>
        ${source} -> return ${dataName}.empty()   // TODO: implement actual merge
      </#list>
        else -> logger().warn("Event of type " + event.typeName() + " was not matched.")
      }
    }

    return previousData
  }
}
