package ${packageName};

<#if imports?has_content>
<#list imports as import>
import ${import.qualifiedClassName};
</#list>
</#if>

public class ${valueObjectName} {

  <#list members as member>
  ${member}
  </#list>

  public companion object {
    public fun from(${constructorParameters}) : ${valueObjectName} {
      return new ${valueObjectName}(${constructorInvocationParameters})
    }
  }

  constructor (${constructorParameters}) {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
  }

}
