package ${packageName}

<#if eventSourced>
public class ${stateName} {
<#else>
import io.vlingo.xoom.symbio.store.`object`.StateObject

/**
 * See <a href="https://docs.vlingo.io/xoom-symbio/object-storage">Object Storage</a>
 */
public class ${stateName} : StateObject {
</#if>

  <#list members as member>
  ${member}
  </#list>

  public companion object{
    public fun identifiedBy(id: ${idType}) : ${stateName} {
      return ${stateName}(${methodInvocationParameters})
    }
  }

  public constructor(${constructorParameters}) {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
  }

  <#list methods as method>
  ${method}
  </#list>
}
