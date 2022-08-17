<#list imports as import>
using ${import.qualifiedClassName};
</#list>

namespace ${packageName};

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#querying-a-statestore">Querying a StateStore</a>
 */
public class ${queriesActorName}: StateStoreQueryActor, ${queriesName}
{

  public ${queriesActorName}(IStateStore store) : base(store)
  {
  }

  public Completes<${dataName}> ${queryByIdMethodName}(string id)
  {
    return QueryStateFor<${dataName}>(id, ${dataName}.Empty);
  }

  public Completes<Collection<${dataName}>> ${queryAllMethodName}()
  {
    return StreamAllOf<${dataName}>(new List<>());
  }
}
