package io.vlingo.xoom.designer.codegen.e2e.java.integratedservices;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("all")
public class MechanicalIncidentData {

  public final String id;
  public final String freighterId;
  public final String freighterPartNumber;
  public final LocalDateTime occurredOn;
  public final Set<FreighterPartData> relatedParts = new HashSet<>();

  public MechanicalIncidentData (final String id,
                                 final String freighterId,
                                 final String freighterPartNumber,
                                 final LocalDateTime occurredOn,
                                 final Set<FreighterPartData> relatedParts) {
    this.id = id;
    this.freighterId = freighterId;
    this.freighterPartNumber = freighterPartNumber;
    this.occurredOn = occurredOn;
    this.relatedParts.addAll(relatedParts);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    MechanicalIncidentData another = (MechanicalIncidentData) other;
    return new EqualsBuilder()
              .append(this.freighterId, another.freighterId)
              .append(this.freighterPartNumber, another.freighterPartNumber)
              .append(this.occurredOn, another.occurredOn)
              .append(this.relatedParts, another.relatedParts)
              .isEquals();
  }

}
