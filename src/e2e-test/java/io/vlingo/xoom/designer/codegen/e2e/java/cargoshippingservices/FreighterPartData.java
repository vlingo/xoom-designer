package io.vlingo.xoom.designer.codegen.e2e.java.cargoshippingservices;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Set;

public class FreighterPartData {

  public final String partNumber;
  public final Set<ComponentData> dependentComponents = new HashSet<>();

  public FreighterPartData (final String partNumber, final Set<ComponentData> dependentComponents) {
    this.partNumber = partNumber;
    this.dependentComponents.addAll(dependentComponents);
  }

  public static FreighterPartData sample() {
    return new FreighterPartData("IIU76", ComponentData.samples());
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(31, 17)
              .append(partNumber)
              .append(dependentComponents)
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
    FreighterPartData another = (FreighterPartData) other;
    return new EqualsBuilder()
              .append(this.partNumber, another.partNumber)
              .append(this.dependentComponents, another.dependentComponents)
              .isEquals();
  }

}
