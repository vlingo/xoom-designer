package io.vlingo.xoom.designer.codegen.e2e.java.cargoshippingservices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComponentData {

  public final String name;
  public final String cricalityLevel;

  @JsonCreator
  public ComponentData (@JsonProperty("name") final String name,
                        @JsonProperty("cricalityLevel") final String cricalityLevel) {
    this.name = name;
    this.cricalityLevel = cricalityLevel;
  }

  public static Set<ComponentData> samples() {
    return Stream.of(new ComponentData("distiller", "high"), new ComponentData("tracker", "low")).collect(Collectors.toSet());
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(31, 17)
              .append(name)
              .append(cricalityLevel)
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
    ComponentData another = (ComponentData) other;
    return new EqualsBuilder()
              .append(this.name, another.name)
              .append(this.cricalityLevel, another.cricalityLevel)
              .isEquals();
  }

}
