package ${packageName};

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
<#if compositeId?has_content>
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Four;
  <#if compositeId?split(", ")?map(id -> id?trim)?filter(id -> id?has_content)?size == 2>
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Five;
  </#if>
</#if>
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

  <#if compositeId?has_content>
    <#assign queryAllCompositeId=compositeId?substring(0, compositeId?length - 2) />
  <#else>
    <#assign queryAllCompositeId=compositeId />
  </#if>
  <#if useCQRS>
  public static final HandlerEntry<${queryAllHandlerType}<Completes<Collection<${dataName}>>, ${queriesName}${queryAllCompositeIdType}>> QUERY_ALL_HANDLER =
          HandlerEntry.of(${queryAllIndexName}, ${queriesName}::${queryAllMethodName});

  public static final HandlerEntry<${queryByIdHandlerType}<Completes<${dataName}>, ${queriesName}, ${compositeIdType}String>> QUERY_BY_ID_HANDLER =
          HandlerEntry.of(${queryByIdIndexName}, ${queriesName}::${queryByIdMethodName});
  </#if>

}