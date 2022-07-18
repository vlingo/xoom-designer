package ${packageName};

<#if imports?has_content>
<#list imports as import>
import ${import.qualifiedClassName};
</#list>
</#if>

<#if eventSourced>
public final class ${stateName} {
<#else>
import io.vlingo.xoom.symbio.store.object.StateObject;

/**
 * See <a href="https://docs.vlingo.io/xoom-symbio/object-storage">Object Storage</a>
 */
public final class ${stateName} extends StateObject {
</#if>

  <#list members as member>
  ${member}
  </#list>

  public static ${stateName} identifiedBy(final ${idType} id) {
    return new ${stateName}(${methodInvocationParameters});
  }

  public ${stateName}(${constructorParameters}) {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
  }

  <#list methods as method>
  ${method}
  </#list>
}
