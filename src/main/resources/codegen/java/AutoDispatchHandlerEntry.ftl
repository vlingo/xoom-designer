<#macro compositeIdFieldType input>
${input?split(", ")?map(id -> id?trim)?filter(id -> id?has_content)?map(id -> "String")?join(", ")+", "}</#macro>
<#macro handlerFrom input>
  <#assign elements=input?split(",")?filter(id -> id?has_content) />
<#if elements?size == 1>Three<#elseif elements?size == 2>Four<#else>Five</#if></#macro>
<#if compositeId?has_content>
<#if factoryMethod>
public static final HandlerEntry<<@handlerFrom compositeId/><Completes<${stateName}>, Stage, <@compositeIdFieldType compositeId/>${dataName}>> ${indexName}_HANDLER =
          HandlerEntry.of(${indexName}, ($stage, ${compositeId}data) -> {
              <#list valueObjectInitializers as initializer>
              ${initializer}
              </#list>
            return ${aggregateProtocolName}.${methodName}(${methodInvocationParameters});
          });
<#else>
public static final HandlerEntry<<@handlerFrom compositeId/><Completes<${stateName}>, ${aggregateProtocolName}, <@compositeIdFieldType compositeId/>${dataName}>> ${indexName}_HANDLER =
          HandlerEntry.of(${indexName}, (${aggregateProtocolVariable}, ${compositeId}data) -> {
              <#list valueObjectInitializers as initializer>
              ${initializer}
              </#list>
            return ${aggregateProtocolVariable}.${methodName}(${methodInvocationParameters});
          });
</#if>
<#else>
<#if factoryMethod>
public static final HandlerEntry<Three<Completes<${stateName}>, Stage, ${dataName}>> ${indexName}_HANDLER =
          HandlerEntry.of(${indexName}, ($stage, data) -> {
              <#list valueObjectInitializers as initializer>
              ${initializer}
              </#list>
              return ${aggregateProtocolName}.${methodName}(${methodInvocationParameters});
          });
<#else>
public static final HandlerEntry<Three<Completes<${stateName}>, ${aggregateProtocolName}, ${dataName}>> ${indexName}_HANDLER =
          HandlerEntry.of(${indexName}, (${aggregateProtocolVariable}, data) -> {
              <#list valueObjectInitializers as initializer>
              ${initializer}
              </#list>
              return ${aggregateProtocolVariable}.${methodName}(${methodInvocationParameters});
          });
</#if>
</#if>