package ${packageName};

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import io.vlingo.xoom.lattice.model.sourcing.EventSourced;

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#sourced">EventSourced</a>
 */
public final class ${entityName} extends EventSourced implements ${aggregateProtocolName} {
  private ${stateName} state;

  public ${entityName}(final ${idType} id) {
    super(id);
    this.state = ${stateName}.identifiedBy(id);
  }

  <#if sourcedEvents?has_content>
  static {
    <#list sourcedEvents as sourcedEvent>
    EventSourced.registerConsumer(${entityName}.class, ${sourcedEvent}.class, ${entityName}::apply${sourcedEvent});
    </#list>
  }
  </#if>
  <#if !useCQRS>

  /*
   * Returns my current state.
   *
   * @return {@code Completes<${stateName}>}
   */
  public Completes<${stateName}> currentState() {
    return Completes.withSuccess(state);
  }
  </#if>

  <#list methods as method>
  ${method}
  </#list>
  <#list eventHandlers as eventHandler>
  <#if eventHandler.eventName?has_content>
  private void apply${eventHandler.eventName}(final ${eventHandler.eventName} event) {
    <#list eventHandler.missingFields as missingField>
    //TODO: Event is missing ${missingField.fieldName}; using ${missingField.defaultValue} instead
    </#list>
    state = state.${eventHandler.methodName}(${eventHandler.methodInvocationParameters});
  }

  </#if>
  </#list>
  /*
   * Restores my initial state by means of {@code state}.
   *
   * @param snapshot the {@code ${stateName}} holding my state
   * @param currentVersion the int value of my current version; may be helpful in determining if snapshot is needed
   */
  @Override
  @SuppressWarnings("hiding")
  protected <${stateName}> void restoreSnapshot(final ${stateName} snapshot, final int currentVersion) {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/xoom-lattice/entity-cqrs#eventsourced
  }

  /*
   * Answer the valid {@code ${stateName}} instance if a snapshot should
   * be taken and persisted along with applied {@code Source<T>} instance(s).
   *
   * @return ${stateName}
   */
  @Override
  @SuppressWarnings("unchecked")
  protected ${stateName} snapshot() {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/xoom-lattice/entity-cqrs#eventsourced
    return null;
  }
}
