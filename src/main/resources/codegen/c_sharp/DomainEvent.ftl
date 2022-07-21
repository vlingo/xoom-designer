using Vlingo.Xoom.Common.Version;
using Vlingo.Xoom.Lattice.Model;

namespace ${packageName};

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#commands-domain-events-and-identified-domain-events">
 *   Commands, Domain Events, and Identified Domain Events
 * </a>
 */
public class ${domainEventName} : IdentifiedDomainEvent
{

  <#list members as member>
  ${member}
  </#list>

  public ${domainEventName}(${constructorParameters}) : base(SemanticVersion.From("${defaultSchemaVersion}").ToValue())
  {
    <#list membersAssignment as assignment>
    ${assignment}
    </#list>
  }

  public override string Identity => Id;
}
