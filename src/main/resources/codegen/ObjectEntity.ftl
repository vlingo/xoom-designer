package ${packageName};

import java.util.Collections;
import java.util.List;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.object.ObjectEntity;

public final class ${entityName} extends ObjectEntity<${stateName}> implements ${aggregateProtocolName} {
  private ${stateName} state;

  public ${entityName}() {
    super(); // uses GridAddress id as unique identity

    this.state = ${stateName}.identifiedBy(id);
  }

  public Completes<${stateName}> definePlaceholder(final String value) {
    state.withPlaceholderValue(value);
    return apply(state, new ${domainEventName}(state.id, value), () -> state);
  }

  //=====================================
  // ObjectEntity
  //=====================================

  @Override
  protected ${stateName} stateObject() {
    return state;
  }

  @Override
  protected void stateObject(final ${stateName} stateObject) {
    this.state = stateObject;
  }

  @Override
  protected Class<${stateName}> stateObjectType() {
    return ${stateName}.class;
  }
}
