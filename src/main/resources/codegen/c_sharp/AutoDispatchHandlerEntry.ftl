<#if factoryMethod && !requireEntityLoading>
public static HandlerEntry<${handlerType}<ICompletes<${stateName}>, Stage, ${dataName}>> ${indexName}_HANDLER =
          HandlerEntry.of(${indexName}, ($stage, data) => {
              <#list valueObjectInitializers as initializer>
              ${initializer}
              </#list>
              return ${aggregateProtocolName}.${methodName}(${methodInvocationParameters});
          });
<#else>
public static HandlerEntry<${handlerType}<ICompletes<${stateName}>, ${aggregateProtocolName}, ${dataName}>> ${indexName}_HANDLER =
          HandlerEntry.of(${indexName}, (${aggregateProtocolVariable}, data) => {
              <#list valueObjectInitializers as initializer>
              ${initializer}
              </#list>
              return ${aggregateProtocolVariable}.${methodName}(${methodInvocationParameters});
          });
</#if>
