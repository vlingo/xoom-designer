<#if factoryMethod && !requireEntityLoading>
public static final HandlerEntry<${handlerType}<Completes<${stateName}>, Stage, ${compositeIdType}${dataName}>> ${indexName}_HANDLER =
          HandlerEntry.of(${indexName}, ($stage, ${compositeId}data) -> {
              <#list valueObjectInitializers as initializer>
              ${initializer}
              </#list>
              return ${aggregateProtocolName}.${methodName}(${methodInvocationParameters});
          });
<#else>
public static final HandlerEntry<${handlerType}<Completes<${stateName}>, ${aggregateProtocolName}, ${compositeIdType}${dataName}>> ${indexName}_HANDLER =
          HandlerEntry.of(${indexName}, (${aggregateProtocolVariable}, ${compositeId}data) -> {
              <#list valueObjectInitializers as initializer>
              ${initializer}
              </#list>
              return ${aggregateProtocolVariable}.${methodName}(${methodInvocationParameters});
          });
</#if>
