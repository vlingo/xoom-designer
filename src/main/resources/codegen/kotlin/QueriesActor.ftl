package ${packageName}

import java.util.ArrayList
import java.util.Collection

import io.vlingo.xoom.common.Completes
import io.vlingo.xoom.lattice.query.StateStoreQueryActor
import io.vlingo.xoom.symbio.store.state.StateStore

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#querying-a-statestore">Querying a StateStore</a>
 */
public class ${queriesActorName} : StateStoreQueryActor, ${queriesName} {

  public constructor(store: StateStore) : super(store){

  }

  public override fun ${queryByIdMethodName}(id: String): Completes<${dataName}> {
    return queryStateFor(id, ${dataName}::class.java, ${dataName}.empty())
  }

  public override fun ${queryAllMethodName}(): Completes<Collection<${dataName}>> {
    return streamAllOf(${dataName}::class.java, ArrayList<>())
  }

}
