package ${packageName}

<#if imports?has_content>
<#list imports as import>
import ${import.qualifiedClassName}
</#list>
</#if>

public class ${dataValueObjectName} {

  <#list members as member>
  ${member}
  </#list>

  public companion object {
    <#list staticFactoryMethods as factoryMethod>
    public fun from(${factoryMethod.parameters}) : ${factoryMethod.dataObjectName} {
      <#list factoryMethod.valueObjectInitializers as initializer>
      ${initializer}
      </#list>
      return ${factoryMethod.constructorInvocation}
    }

    </#list>
  }

  constructor (${constructorParameters}) {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
  }

  public fun to${valueObjectName}() : ${valueObjectName} {
    <#list valueObjectTranslations as translation>
    ${translation}
    </#list>
    return ${valueObjectName}.from(${valueObjectFields})
  }

}
