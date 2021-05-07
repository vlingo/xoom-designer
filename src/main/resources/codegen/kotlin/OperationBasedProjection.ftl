package ${packageName}

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

import io.vlingo.xoom.lattice.model.projection.Projectable
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/projections#implementing-with-the-statestoreprojectionactor">
 *   Implementing With the StateStoreProjectionActor
 * </a>
 */
public class ${projectionName} : StateStoreProjectionActor<${dataName}> {
  var becauseOf: String

  public constructor() : super(${storeProviderName}.instance().store){

  }

  protected override fun currentDataFor(projectable: Projectable): ${dataName} {
    becauseOf = projectable.becauseOf()[0]
    val state: ${stateName} = projectable.object()
    val current: ${dataName} = ${dataName}.from(state)
    return current
  }

  protected override fun merge(previousData: ${dataName}, previousVersion: Int, currentData: ${dataName}, currentVersion: Int): ${dataName} {
    var merged: ${dataName}

    if (previousData == null) {
      previousData = currentData
    }

    when (${projectionSourceTypesName}.valueOf(becauseOf)) {
      <#list sourceNames as source>
      ${source} -> return ${dataName}.empty()   // TODO: implement actual merge
      </#list>
      else -> merged = currentData
    }

    return merged
  }
}
