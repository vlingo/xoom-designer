package ${packageName};

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Three;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Two;
import io.vlingo.xoom.turbo.annotation.autodispatch.HandlerEntry;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>
<#if useCQRS>
import java.util.Collection;
</#if>

public class ${autoDispatchHandlersMappingName} {

  <#list handlerIndexes as index>
  ${index}
  </#list>

  <#list handlerEntries as entry>
  ${entry}
  </#list>
  public static final HandlerEntry<Two<${dataName}, ${stateName}>> ADAPT_STATE_HANDLER =
          HandlerEntry.of(ADAPT_STATE, ${dataName}::from);

  <#if useCQRS>
  public static final HandlerEntry<Two<Completes<Collection<${dataName}>>, ${queriesName}>> QUERY_ALL_HANDLER =
          HandlerEntry.of(${queryAllIndexName}, ${queriesName}::${queryAllMethodName});

  public static final HandlerEntry<Three<Completes<${dataName}>, ${queriesName}, String>> QUERY_BY_ID_HANDLER =
          HandlerEntry.of(${queryByIdIndexName}, ($queries, id) -> $queries.${queryByIdMethodName}(id));
  </#if>

}