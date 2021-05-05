package ${packageName};

<#if imports?has_content>
<#list imports as import>
import ${import.qualifiedClassName};
</#list>
</#if>

import io.vlingo.xoom.lattice.model.stateful.StatefulEntity;

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#stateful">StatefulEntity</a>
 */
public final class ${entityName} extends StatefulEntity<${stateName}> implements ${aggregateProtocolName} {
  private ${stateName} state;

  public ${entityName}(final ${idType} id) {
    super(id);
    this.state = ${stateName}.identifiedBy(id);
  }
  <#if !useCQRS>

  /*
   * Returns my current state.
   *
   * @return {@code Completes<${stateName}>}
   */
  public Completes<${stateName}> currentState() {
    return Completes.withSuccess(state);
  }
  </#if>

  <#list methods as method>
  ${method}
  </#list>
  /*
   * Received when my current state has been applied and restored.
   *
   * @param state the ${stateName}
   */
  @Override
  protected void state(final ${stateName} state) {
    this.state = state;
  }

  /*
   * Received when I must provide my state type.
   *
   * @return {@code Class<${stateName}>}
   */
  @Override
  protected Class<${stateName}> stateType() {
    return ${stateName}.class;
  }
}
