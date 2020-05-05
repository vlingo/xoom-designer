package ${packageName};

import java.util.Collections;
import java.util.List;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

public final class ${aggregateProtocolName}Entity extends EventSourced implements ${aggregateProtocolName} {
  private ${stateClass} state;

  public ${aggregateProtocolName}Entity() {
    super(); // uses GridAddress id as unique identity

    this.state = ${stateClass}.identifiedBy(id);
  }

  public Completes<${stateClass}> definePlaceholder(final String value) {
    return apply(state.withPlaceholderValue(value), new ${aggregateProtocolName}PlaceholderDefined(state.id, value), () -> state);
  }

  //=====================================
  // EventSourced
  //=====================================

  static {
    EventSourced.registerConsumer(${aggregateProtocolName}Entity.class, ${aggregateProtocolName}PlaceholderDefined.class, ${aggregateProtocolName}Entity::apply${aggregateProtocolName}PlaceholderDefined);
  }

  private void apply${aggregateProtocolName}PlaceholderDefined(final ${aggregateProtocolName}PlaceholderDefined e) {
    state = state.withPlaceholderValue(e.placeholderValue);
  }
}
