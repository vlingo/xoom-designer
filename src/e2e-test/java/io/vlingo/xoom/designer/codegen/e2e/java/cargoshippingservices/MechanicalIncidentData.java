package io.vlingo.xoom.designer.codegen.e2e.java.cargoshippingservices;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("all")
public class MechanicalIncidentData {

  public String id;
  public final String freighterId;
  public final String freighterPartNumber;
  public final Long occurredOn;
  public final Set<FreighterPartData> relatedParts = new HashSet<>();

  public static MechanicalIncidentData sampleOfNewIncident() {
    return new MechanicalIncidentData("YLK-17825", "752489&Y", 1631764888381l, new HashSet<>());
  }

  public static MechanicalIncidentData sampleOfIncidentWithRelatedPart(final String incidentId) {
    final MechanicalIncidentData incident = sampleOfNewIncident();
    incident.id = incidentId;
    incident.relatedParts.add(FreighterPartData.sample());
    return incident;
  }

  public MechanicalIncidentData (final String freighterId,
                                 final String freighterPartNumber,
                                 final Long occurredOn,
                                 final Set<FreighterPartData> relatedParts) {
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
