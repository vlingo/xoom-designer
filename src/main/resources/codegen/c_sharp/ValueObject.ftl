namespace ${packageName};

public class ${valueObjectName} : IEquatable<${valueObjectName}>
{

  <#list members as member>
  ${member}
  </#list>

  public static ${valueObjectName} From(${constructorParameters})
  {
    return new ${valueObjectName}(${constructorInvocationParameters});
  }

  private ${valueObjectName}(${constructorParameters})
  {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
  }

  public override bool Equals(object obj)
  {
    if (obj == null || obj.GetType() != GetType()) return false;

    var other = (${valueObjectName}) obj;
    return ${memberNames?map(member -> member + " == other." + member)?join(" && ")};
  }

  public bool Equals(${valueObjectName} other) => ${memberNames?map(member -> member + " == other." + member)?join(" && ")};
  public override int GetHashCode() => HashCode.Combine(${memberNames?join(", ")});
}