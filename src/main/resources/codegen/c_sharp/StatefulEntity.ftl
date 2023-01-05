using System;
using Vlingo.Xoom.Common;
using Vlingo.Xoom.Lattice.Model.Stateful;

namespace ${packageName};

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#stateful">StatefulEntity</a>
 */
public sealed class ${entityName} : StatefulEntity<${stateName}>, I${aggregateProtocolName}
{
  private ${stateName} _state;

  public ${entityName}(${idType} id) : base(id)
  {
    _state = ${stateName}.IdentifiedBy(id);
  }

  public ICompletes<${stateName}> CurrentState()
  {
    return Completes().With(_state);
  }

  <#list methods as method>
  ${method}
  </#list>
  protected override void State(${stateName} state)
  {
    _state = state;
  }

  protected Type StateType() => typeof(${stateName});
}
