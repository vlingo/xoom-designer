package ${packageName};

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
<#if imports?has_content>
<#list imports as import>
import ${import.qualifiedClassName};
</#list>
</#if>

public class ${dataValueObjectName} {

  <#list members as member>
  ${member}
  </#list>

  <#list staticFactoryMethods as factoryMethod>
  public static ${factoryMethod.dataObjectName} from(${factoryMethod.parameters}) {
    <#if factoryMethod.singleArgumentName?has_content>
    if (${factoryMethod.singleArgumentName} == null) {
      return ${dataValueObjectName}.empty();
    } else {
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
  public static Set<${dataValueObjectName}> fromAll(final Set<${valueObjectName}> correspondingObjects) {
    return correspondingObjects == null ? Collections.emptySet() : correspondingObjects.stream().map(${dataValueObjectName}::from).collect(Collectors.toSet());
  }

  public static List<${dataValueObjectName}> fromAll(final List<${valueObjectName}> correspondingObjects) {
    return correspondingObjects == null ? Collections.emptyList() : correspondingObjects.stream().map(${dataValueObjectName}::from).collect(Collectors.toList());
  }

  private ${dataValueObjectName}(${constructorParameters}) {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
  }

  public ${valueObjectName} to${valueObjectName}() {
    <#list valueObjectTranslations as translation>
    ${translation}
    </#list>
    return ${valueObjectName}.from(${valueObjectFields});
  }

  public static ${dataValueObjectName} empty() {
    return new ${dataValueObjectName}(${emptyObjectArguments});
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(31, 17)
              <#list memberNames as member>
              .append(${member})
              </#list>
              .toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    ${dataValueObjectName} another = (${dataValueObjectName}) other;
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
