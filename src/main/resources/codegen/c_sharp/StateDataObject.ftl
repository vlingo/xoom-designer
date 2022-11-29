using System;
using System.Linq;
using System.Collections.Generic;
<#if imports?has_content>
<#list imports as import>
using ${import.qualifiedClassName};
</#list>
</#if>

namespace ${packageName};

public class ${dataName} : IEquatable<${dataName}>
{
  <#list members as member>
  ${member}
  </#list>

  public ${dataName}()
  {}

  <#list staticFactoryMethods as factoryMethod>
  public static ${factoryMethod.dataObjectName} From(${factoryMethod.parameters})
  {
    <#list factoryMethod.valueObjectInitializers as initializer>
    ${initializer}
    </#list>
    return ${factoryMethod.constructorInvocation};
  }

  </#list>
  public static List<${dataName}> FromAll(List<${stateName}> states) => states.Select(${dataName}.From).ToList();

  public static ${dataName} Empty => From(${stateName}.IdentifiedBy(""));

  private ${dataName}(${constructorParameters})
  {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
  }

  public ${stateName} To${stateName}()
  {
    <#list valueObjectTranslations as translation>
    ${translation}
    </#list>
    return new ${stateName}(${stateFields});
  }

  public override bool Equals(object obj)
  {
    if (obj == null || obj.GetType() != GetType()) return false;

    var otherData = (${dataName}) obj;
    return ${memberNames?map(member -> member + " == otherData." + member)?join(" && ")};
  }

  public bool Equals(${dataName} other) => ${memberNames?map(member -> member + " == other." + member)?join(" && ")};
  public override int GetHashCode() => HashCode.Combine(${memberNames?join(", ")});

}
