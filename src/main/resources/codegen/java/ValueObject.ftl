package ${packageName};

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
<#if imports?has_content>
<#list imports as import>
import ${import.qualifiedClassName};
</#list>
</#if>

public final class ${valueObjectName} {

  <#list members as member>
  ${member}
  </#list>

  public static ${valueObjectName} from(${constructorParameters}) {
    return new ${valueObjectName}(${constructorInvocationParameters});
  }

  private ${valueObjectName}(${constructorParameters}) {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
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
    ${valueObjectName} another = (${valueObjectName}) other;
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
