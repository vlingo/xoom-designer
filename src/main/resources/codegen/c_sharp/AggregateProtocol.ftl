using Vlingo.Xoom.Actors;
using Vlingo.Xoom.Common;

namespace ${packageName};

public interface I${aggregateProtocolName}
{

  <#list methods as method>
  ${method}
  </#list>
}