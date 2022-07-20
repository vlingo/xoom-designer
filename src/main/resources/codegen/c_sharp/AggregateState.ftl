using Vlingo.Xoom.Symbio;
using Vlingo.Xoom.Symbio.Store.Object;
using Vlingo.Xoom.Wire.Nodes;

namespace ${packageName};

/**
 * See <a href="https://docs.vlingo.io/xoom-symbio/object-storage">Object Storage</a>
 */
public sealed class ${stateName} : BaseEntry<string>, IEquatable<${stateName}>
{
  <#list members as member>
  ${member}
  </#list>

  public static ${stateName} IdentifiedBy(${idType} id)
  {
    return new ${stateName}(${methodInvocationParameters});
  }

  private ${stateName}(${constructorParameters}) : base(id, typeof(string), 1, EmptyObjectData ?? "")
  {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
  }

  <#list methods as method>
  ${method}
  </#list>
  public override IEntry WithId(string id) => IdentifiedBy(id);

  public override bool Equals(object obj)
  {
    if (obj == null || obj.GetType() != GetType()) return false;

    var otherState = (${stateName}) obj;
    return ${memberNames?map(member -> member + " == otherState." + member)?join(" && ")};
  }

  public bool Equals(${stateName} other) => ${memberNames?map(member -> member + " == other." + member)?join(" && ")};
  public override int GetHashCode() => HashCode.Combine(${memberNames?join(", ")});
}
