<#list imports as import>
using ${import.qualifiedClassName};
</#list>
using System.Collections.ObjectModel;
using Vlingo.Xoom.Common;
using Vlingo.Xoom.Lattice.Query;
using Vlingo.Xoom.Symbio.Store.State;

namespace ${packageName};

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#querying-a-statestore">Querying a StateStore</a>
 */
public class ${queriesActorName}: StateStoreQueryActor<${dataName}>, ${queriesName}
{

  public ${queriesActorName}(IStateStore store) : base(store)
  {
  }

  public ICompletes<${dataName}> ${queryByIdMethodName}(string id)
  {
    return QueryStateFor<${dataName}>(id, ${dataName}.Empty, default, default);
  }

  public ICompletes<Collection<${dataName}>> ${queryAllMethodName}()
  {
    return StreamAllOf<${dataName}>(new List<${dataName}>());
  }
}
