package ${packageName}

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

import io.vlingo.xoom.lattice.model.sourcing.EventSourced

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#sourced">EventSourced</a>
 */
public class ${entityName} extends EventSourced implements ${aggregateProtocolName} {
  state: ${stateName}

  public constructor(id: ${idType}) : super(id){
    this.state = ${stateName}.identifiedBy(id)
  }

  <#if sourcedEvents?has_content>
  companion object {
    <#list sourcedEvents as sourcedEvent>
    EventSourced.registerConsumer(${entityName}::class.java, ${sourcedEvent}::class.java, ${entityName}::apply${sourcedEvent})
    </#list>
  }
  </#if>

  <#list methods as method>
  ${method}
  </#list>
  <#list sourcedEvents as sourcedEvent>
  fun apply${sourcedEvent}(event: ${sourcedEvent}) {
    //TODO: Handle ${sourcedEvent} here
  }

  </#list>
  /*
   * Restores my initial state by means of {@code state}.
   *
   * @param snapshot the {@code ${stateName}} holding my state
   * @param currentVersion the int value of my current version; may be helpful in determining if snapshot is needed
   */
  @Override
  protected <AccountState> fun restoreSnapshot(snapshot: ${stateName}, currentVersion: Int) {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/xoom-lattice/entity-cqrs#eventsourced
  }

  /*
   * Answer the valid {@code ${stateName}} instance if a snapshot should
   * be taken and persisted along with applied {@code Source<T>} instance(s).
   *
   * @return AccountState
   */
  @Override
  protected fun snapshot(): ${stateName} {
    // OVERRIDE FOR SNAPSHOT SUPPORT
    // See: https://docs.vlingo.io/xoom-lattice/entity-cqrs#eventsourced
    return null
  }
}
