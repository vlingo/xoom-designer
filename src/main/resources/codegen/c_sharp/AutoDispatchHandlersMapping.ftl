<#list imports as import>
using ${import.qualifiedClassName};
</#list>

namespace ${packageName};

public class ${autoDispatchHandlersMappingName}
{

<#list handlerIndexes as index>
    ${index}
</#list>

<#list handlerEntries as entry>
    ${entry}
</#list>
  public static HandlerEntry<Two<${dataName}, ${stateName}>> ADAPT_STATE_HANDLER =
    HandlerEntry.Of(ADAPT_STATE, ${dataName}.From);

<#if useCQRS>
  public static HandlerEntry<Two<ICompletes<ICollection<${dataName}>>, ${queriesName}>> QUERY_ALL_HANDLER =
    HandlerEntry.Of(${queryAllIndexName}, ${queriesName}.${queryAllMethodName});

  public static HandlerEntry<Three<ICompletes<${dataName}>, ${queriesName}, String>> QUERY_BY_ID_HANDLER =
    HandlerEntry.Of(${queryByIdIndexName}, ($queries, id) => $queries.${queryByIdMethodName}(id));
</#if>

}