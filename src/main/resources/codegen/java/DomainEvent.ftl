package ${packageName};

import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;

<#if imports?has_content>
<#list imports as import>
import ${import.qualifiedClassName};
</#list>
</#if>

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public final class ${domainEventName} extends IdentifiedDomainEvent {

  <#list members as member>
  ${member}
  </#list>

  public ${domainEventName}(final ${stateName} state) {
    super(SemanticVersion.from("${defaultSchemaVersion}").toValue());
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
  }

  @Override
  public String identity() {
    return id;
  }
}
