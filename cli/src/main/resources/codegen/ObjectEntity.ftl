package ${packageName};

import java.util.Collections;
import java.util.List;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.object.ObjectEntity;

public final class ${aggregateProtocolName}Entity extends ObjectEntity<${stateClass}> implements ${aggregateProtocolName} {
  private ${stateClass} state;

  public ${aggregateProtocolName}Entity() {
    super(); // uses GridAddress id as unique identity

    this.state = ${stateClass}.identifiedBy(id);
  }

  public Completes<${stateClass}> definePlaceholder(final String value) {
    state.withPlaceholderValue(value);
    return apply(state, new ${placeholderDefinedEvent}(state.id, value), () -> state);
  }

  //=====================================
  // ObjectEntity
  //=====================================

  @Override
  protected ${stateClass} stateObject() {
    return state;
  }

  @Override
  protected void stateObject(final ${stateClass} stateObject) {
    this.state = stateObject;
  }

  @Override
  protected Class<${stateClass}> stateObjectType() {
    return ${stateClass}.class;
  }
}
