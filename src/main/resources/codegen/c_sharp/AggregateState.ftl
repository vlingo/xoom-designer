using System;
<#if imports?has_content>
<#list imports as import>
using ${import.qualifiedClassName};
</#list>
</#if>
using Vlingo.Xoom.Symbio;
<#if !eventSourced>
using Vlingo.Xoom.Symbio.Store.Object;
</#if>
using Vlingo.Xoom.Wire.Nodes;

namespace ${packageName};

<#if eventSourced>
public sealed class ${stateName}
<#else>
/**
 * See <a href="https://docs.vlingo.io/xoom-symbio/object-storage">Object Storage</a>
 */
public sealed class ${stateName} : BaseEntry<string>, IEquatable<${stateName}>
</#if>
{
  <#list members as member>
  ${member}
  </#list>

  public static ${stateName} IdentifiedBy(${idType} id) => new ${stateName}(${methodInvocationParameters});

  public ${stateName}(${constructorParameters})<#if !eventSourced> : base(id, typeof(string), 1, EmptyObjectData ?? "")</#if>
  {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
  }

  <#list methods as method>
  ${method}
  </#list>
  <#if !eventSourced>
  public override IEntry WithId(string id) => IdentifiedBy(id);

  public override bool Equals(object obj)
  {
    if (obj == null || obj.GetType() != GetType()) return false;

    var otherState = (${stateName}) obj;
    return ${memberNames?map(member -> member + " == otherState." + member)?join(" && ")};
  }

  public bool Equals(${stateName} other) => ${memberNames?map(member -> member + " == other." + member)?join(" && ")};
  public override int GetHashCode() => HashCode.Combine(${memberNames?join(", ")});
  </#if>
}
