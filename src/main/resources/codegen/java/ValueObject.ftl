package ${packageName};

public final class ${valueObjectName} {

  <#list members as member>
  ${member}
  </#list>

  public static ${valueObjectName} from(${constructorParameters}) {
    return new ${valueObjectName}(${constructorInvocationParameters});
  }

  private ${valueObjectName} (${constructorParameters}) {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
  }

}