package ${packageName}

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

import io.vlingo.xoom.lattice.model.stateful.StatefulEntity

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#stateful">StatefulEntity</a>
 */
public class ${entityName} : StatefulEntity<${stateName}>, ${aggregateProtocolName} {
  var state: ${stateName}

  public constructor(id:  ${idType}) : super(id){
    this.state = ${stateName}.identifiedBy(id)
  }

  <#list methods as method>
  ${method}
  </#list>
  /*
   * Received when my current state has been applied and restored.
   *
   * @param state the ${stateName}
   */
  protected override fun state(state: ${stateName}) {
    this.state = state
  }

  /*
   * Received when I must provide my state type.
   *
   * @return {@code Class<${stateName}>}
   */
  protected override fun stateType(): Class<${stateName}> {
    return ${stateName}::class.java
  }
}
