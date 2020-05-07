package ${packageName};

import java.util.Collections;
import java.util.List;

import io.vlingo.common.Completes;
import io.vlingo.common.Tuple3;
import io.vlingo.lattice.model.stateful.StatefulEntity;
import io.vlingo.symbio.Source;

public final class ${aggregateProtocolName}Entity extends StatefulEntity<${stateClass}> implements ${aggregateProtocolName} {
  private ${stateClass} state;

  public ${aggregateProtocolName}Entity() {
    super(); // uses GridAddress id as unique identity

    this.state = ${stateClass}.identifiedBy(id);
  }

  public Completes<${stateClass}> definePlaceholder(final String value) {
    return apply(state.withPlaceholderValue(value), new ${placeholderDefinedEvent}(state.id, value), () -> state);
  }

  //=====================================
  // StatefulEntity
  //=====================================

  @Override
  protected void state(final ${stateClass} state) {
    this.state = state;
  }

  @Override
  protected Class<${stateClass}> stateType() {
    return ${stateClass}.class;
  }

  @Override
  protected <C> Tuple3<${stateClass},List<Source<C>>,String> whenNewState() {
    if (state.isIdentifiedOnly()) {
      return null;
    }
    return Tuple3.from(state, Collections.emptyList(), "${aggregateProtocolName}:new");
  }
}
