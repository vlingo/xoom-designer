using System;
using System.Linq;
using System.Collections.Generic;
<#if imports?has_content>
<#list imports as import>
using ${import.qualifiedClassName};
</#list>
</#if>

namespace ${packageName};

public class ${dataValueObjectName} : IEquatable<${dataValueObjectName}>
{

  <#list members as member>
  ${member}
  </#list>

  <#list staticFactoryMethods as factoryMethod>
  public static ${factoryMethod.dataObjectName} From(${factoryMethod.parameters})
  {
    <#if factoryMethod.singleArgumentName?has_content>
    if (${factoryMethod.singleArgumentName} == null)
      return ${dataValueObjectName}.Empty;
    else
    {
      <#list factoryMethod.valueObjectInitializers as initializer>
      ${initializer}
      </#list>
      return ${factoryMethod.constructorInvocation};
    }
    <#else>
    <#list factoryMethod.valueObjectInitializers as initializer>
    ${initializer}
    </#list>
    return ${factoryMethod.constructorInvocation};
    </#if>
  }

  </#list>
  public static ISet<${dataValueObjectName}> FromAll(ISet<${valueObjectName}> correspondingObjects)
  {
    return correspondingObjects == null ? new HashSet<${dataValueObjectName}>() : correspondingObjects.Select(From).ToHashSet();
  }

  public static List<${dataValueObjectName}> FromAll(List<${valueObjectName}> correspondingObjects)
  {
    return correspondingObjects == null ? new List<${dataValueObjectName}>() : correspondingObjects.Select(From).ToList();
  }

  private ${dataValueObjectName}(${constructorParameters})
  {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
  }

  public ${valueObjectName} To${valueObjectName}()
  {
    <#list valueObjectTranslations as translation>
    ${translation}
    </#list>
    return ${valueObjectName}.From(${valueObjectFields});
  }

  public static ${dataValueObjectName} Empty => new ${dataValueObjectName}(${emptyObjectArguments});

  public override bool Equals(object obj)
  {
    if (obj == null || obj.GetType() != GetType()) return false;

    var otherData = (${dataValueObjectName}) obj;
    return ${memberNames?map(member -> member + " == otherData." + member)?join(" && ")};
  }

  public bool Equals(${dataValueObjectName} other) => ${memberNames?map(member -> member + " == other." + member)?join(" && ")};
  public override int GetHashCode() => HashCode.Combine(${memberNames?join(", ")});
}
