using Vlingo.Xoom.Symbio.Store.Object;

namespace ${packageName};

/**
 * See <a href="https://docs.vlingo.io/xoom-symbio/object-storage">Object Storage</a>
 */
public sealed class ${stateName} : StateObject
{
  <#list members as member>
  ${member}
  </#list>

  public static ${stateName} IdentifiedBy(${idType} id)
  {
    return new ${stateName}(${methodInvocationParameters});
  }

  public ${stateName} (${constructorParameters})
  {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
  }

  <#list methods as method>
  ${method}
  </#list>
}
