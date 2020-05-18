package ${packageName};

import java.util.Collections;
import java.util.List;

import io.vlingo.common.Completes;
import io.vlingo.common.Tuple3;
import io.vlingo.lattice.model.stateful.StatefulEntity;
import io.vlingo.symbio.Source;

public final class ${entityName} extends StatefulEntity<${stateName}> implements ${aggregateProtocolName} {
  private ${stateName} state;

  public ${entityName}() {
    super(); // uses GridAddress id as unique identity

    this.state = ${stateName}.identifiedBy(id);
  }

  public Completes<${stateName}> definePlaceholder(final String value) {
    return apply(state.withPlaceholderValue(value), new ${domainEventName}(state.id, value), () -> state);
  }

  //=====================================
  // StatefulEntity
  //=====================================

  @Override
  protected void state(final ${stateName} state) {
    this.state = state;
  }

  @Override
  protected Class<${stateName}> stateType() {
    return ${stateName}.class;
  }

  @Override
  protected <C> Tuple3<${stateName},List<Source<C>>,String> whenNewState() {
    if (state.isIdentifiedOnly()) {
      return null;
    }
    return Tuple3.from(state, Collections.emptyList(), "${aggregateProtocolName}:new");
  }
}
