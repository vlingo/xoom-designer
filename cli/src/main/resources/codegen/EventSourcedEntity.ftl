package ${packageName};

import java.util.Collections;
import java.util.List;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

public final class ${aggregateProtocolName}Entity extends EventSourced implements ${aggregateProtocolName} {
  private ${stateClass} state;

  public ${aggregateProtocolName}Entity() {
    super(); // uses GridAddress id as unique identity
    this.state = ${stateClass}.identifiedBy(streamName);
  }

  public Completes<${stateClass}> definePlaceholder(final String value) {
    return apply(new ${placeholderDefinedEvent}(state.id, value), () -> state);
  }

  //=====================================
  // EventSourced
  //=====================================

  static {
    EventSourced.registerConsumer(${aggregateProtocolName}Entity.class, ${placeholderDefinedEvent}.class, ${aggregateProtocolName}Entity::apply${placeholderDefinedEvent});
  }

  private void apply${placeholderDefinedEvent}(final ${placeholderDefinedEvent} e) {
    state = state.withPlaceholderValue(e.placeholderValue);
  }
}
