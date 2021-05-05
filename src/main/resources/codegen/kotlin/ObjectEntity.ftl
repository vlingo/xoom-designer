package ${packageName}

import java.util.Collections
import java.util.List

import io.vlingo.xoom.common.Completes
import io.vlingo.xoom.lattice.model.object.ObjectEntity

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#entity-types">Entity Types</a>
 */
public class ${entityName} : ObjectEntity<${stateName}>, ${aggregateProtocolName} {
  var state: ${stateName}

  public constructor() : super() // uses GridAddress id as unique identity {
    this.state = ${stateName}.identifiedBy(id)
  }

  public fun definePlaceholder(value: String): Completes<${stateName}> {
    state.withPlaceholderValue(value)
    return apply(state, ${domainEventName}(state.id, value)){state}
  }

  //=====================================
  // ObjectEntity
  //=====================================

  protected override fun stateObject(): ${stateName} {
    return state
  }

  protected override fun stateObject(stateObject: ${stateName}) {
    this.state = stateObject
  }

  @Override
  protected override stateObjectType(): Class<${stateName}> {
    return ${stateName}::class.java
  }
}
