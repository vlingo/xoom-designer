using Vlingo.Xoom.Actors;
using Vlingo.Xoom.Common;

namespace ${packageName};

public interface I${aggregateProtocolName}
{
  <#if !useCQRS>
  /// <summary>
  /// Returns my current state.
  /// </summary>
  /// <returns>ICompletes<${stateName}></returns>
  ICompletes<${stateName}> CurrentState();
  </#if>

  <#list methods as method>
  ${method}
  </#list>
}