package ${packageName};

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.symbio.store.state.StateStore;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#querying-a-statestore">Querying a StateStore</a>
 */
@SuppressWarnings("all")
public class ${queriesActorName} extends StateStoreQueryActor implements ${queriesName} {

  public ${queriesActorName}(StateStore store) {
    super(store);
  }

  @Override
  public Completes<${dataName}> ${queryByIdMethodName}(String id) {
    return queryStateFor(id, ${dataName}.class, ${dataName}.empty());
  }

  @Override
  public Completes<Collection<${dataName}>> ${queryAllMethodName}() {
    return streamAllOf(${dataName}.class, new ArrayList<>());
  }

}
