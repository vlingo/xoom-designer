package ${packageName}

import java.util.UUID
import io.vlingo.xoom.common.version.SemanticVersion
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public class ${domainEventName} : IdentifiedDomainEvent {

  val eventId: UUID
  <#list members as member>
  ${member}
  </#list>

  public constructor(state: ${stateName}) : super(SemanticVersion.from("0.0.1").toValue()) {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
    this.eventId = UUID.randomUUID() //TODO: Define the event id
  }

  public override fun identity(): String {
    return eventId.toString()
  }
}
