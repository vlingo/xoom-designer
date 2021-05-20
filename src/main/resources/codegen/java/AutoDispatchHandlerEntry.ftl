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