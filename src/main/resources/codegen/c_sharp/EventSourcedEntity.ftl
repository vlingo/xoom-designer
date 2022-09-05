using System;
using Vlingo.Xoom.Common;
using Vlingo.Xoom.Lattice.Model;
using Vlingo.Xoom.Lattice.Model.Sourcing;
using Vlingo.Xoom.Turbo.Scooter.Model.Sourced;

namespace ${packageName};

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#sourced">EventSourced</a>
 */
public sealed class ${entityName} : SourcedEntity<DomainEvent>, I${aggregateProtocolName}
{
  private static ${stateName} _state;

  public ${entityName}(${idType} id)
  {
    _state = ${stateName}.IdentifiedBy(id);
  }

  <#if sourcedEvents?has_content>
  static ${entityName}()
  {
    <#list sourcedEvents as sourcedEvent>
    RegisterConsumer<${entityName}, ${sourcedEvent}>(delegate(Vlingo.Xoom.Symbio.Source<DomainEvent> source)
    {
      Apply${sourcedEvent}(source as ${sourcedEvent});
    });
    </#list>
  }
  </#if>
  <#if !useCQRS>

  /*
   * Returns my current state.
   *
   * @return {@code Completes<${stateName}>}
   */
  public ICompletes<${stateName}> CurrentState()
  {
    return Completes().WithSuccess(_state);
  }
  </#if>

  <#list methods as method>
  ${method}
  </#list>
  <#list eventHandlers as eventHandler>
  <#if eventHandler.eventName?has_content>
  private static void Apply${eventHandler.eventName}(${eventHandler.eventName} @event)
  {
    <#list eventHandler.missingFields as missingField>
    //TODO: Event is missing ${missingField.fieldName}; using ${missingField.defaultValue} instead
    </#list>
    _state = _state.${eventHandler.methodName}(${eventHandler.methodInvocationParameters});
  }

  </#if>
  </#list>
  /*
   * Restores my initial state by means of {@code state}.
   *
   * @param snapshot the {@code ${stateName}} holding my state
   * @param currentVersion the int value of my current version; may be helpful in determining if snapshot is needed
   */
  protected void RestoreSnapshot(${stateName} snapshot, int currentVersion)
  {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/xoom-lattice/entity-cqrs#eventsourced
  }

  /*
   * Answer the valid {@code ${stateName}} instance if a snapshot should
   * be taken and persisted along with applied {@code Source<T>} instance(s).
   *
   * @return ${stateName}
   */
  protected ${stateName} Snapshot()
  {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/xoom-lattice/entity-cqrs#eventsourced
    return null;
  }

  public override string Id() => StreamName();

  protected override string StreamName() => null;
}
