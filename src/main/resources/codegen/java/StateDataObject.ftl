package ${packageName};

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.stream.Collectors;
<#if imports?has_content>
<#list imports as import>
import ${import.qualifiedClassName};
</#list>
</#if>

@SuppressWarnings("all")
public class ${dataName} {
  <#list members as member>
  ${member}
  </#list>

  <#list staticFactoryMethods as factoryMethod>
  public static ${factoryMethod.dataObjectName} from(${factoryMethod.parameters}) {
    <#list factoryMethod.valueObjectInitializers as initializer>
    ${initializer}
    </#list>
    return ${factoryMethod.constructorInvocation};
  }

  </#list>
  public static List<${dataName}> from(final List<${stateName}> states) {
    return states.stream().map(${dataName}::from).collect(Collectors.toList());
  }

  public static ${dataName} empty() {
    return from(${stateName}.identifiedBy(""));
  }

  private ${dataName} (${constructorParameters}) {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
  }

  public ${stateName} to${stateName}() {
    <#list valueObjectTranslations as translation>
    ${translation}
    </#list>
    return new ${stateName}(${stateFields});
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    ${dataName} another = (${dataName}) other;
    return new EqualsBuilder()
              <#list memberNames as member>
              .append(this.${member}, another.${member})
              </#list>
              .isEquals();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
              <#list memberNames as member>
              .append("${member}", ${member})
              </#list>
              .toString();
  }

}
