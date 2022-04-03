package ${packageName};

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
<#if compositeId?has_content>
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler.Four;
  <#if compositeId?length == 2>
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

<#macro compositeIdToString input>
${input?split(", ")?map(id -> id?trim)?filter(id -> !id?has_content)?map(id -> "String")?join(", ") + ", "}</#macro>
<#macro compositeIdToLastString input>
${input?split(", ")?map(id -> id?trim)?filter(id -> !id?has_content)?map(id -> "String")?join(", ")}</#macro>
<#macro handlerFrom input>
  <#assign elements=input?split(", ")?map(id -> id?trim)?filter(id -> !id?has_content) />
<#if !elements?has_content>Three<#elseif elements?size == 1>Four<#else>Five</#if></#macro>
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
  <#if useCQRS>
    <#assign queryAllCompositeId=compositeId?substring(0, compositeId?length - 2) />
  public static final HandlerEntry<<@handlerFrom queryAllCompositeId/><Completes<Collection<${dataName}>>, ${queriesName}, <@compositeIdToLastString compositeId/>>> QUERY_ALL_HANDLER =
          HandlerEntry.of(${queryAllIndexName}, ${queriesName}::${queryAllMethodName});

  public static final HandlerEntry<<@handlerFrom compositeId/><Completes<${dataName}>, ${queriesName}, <@compositeIdToString compositeId/>String>> QUERY_BY_ID_HANDLER =
          HandlerEntry.of(${queryByIdIndexName}, ${queriesName}::${queryByIdMethodName});
  </#if>
  <#else>
  <#if useCQRS>
  public static final HandlerEntry<Two<Completes<Collection<${dataName}>>, ${queriesName}>> QUERY_ALL_HANDLER =
          HandlerEntry.of(${queryAllIndexName}, ${queriesName}::${queryAllMethodName});

  public static final HandlerEntry<Three<Completes<${dataName}>, ${queriesName}, String>> QUERY_BY_ID_HANDLER =
          HandlerEntry.of(${queryByIdIndexName}, ($queries, id) -> $queries.${queryByIdMethodName}(id));
  </#if>
  </#if>

}