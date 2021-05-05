<#if factoryMethod>
public companion object{
  val ${indexName}_HANDLER: HandlerEntry<Three<Completes<${stateName}>, Stage, ${dataName}>> =
    HandlerEntry.of(${indexName}, Three<Completes<${stateName}>, Stage, ${dataName}> {
        $stage: Stage, data: ${dataName} -> ${aggregateProtocolName}.${methodName}(${methodInvocationParameters})
    })
}
<#else>
public companion object{
  val ${indexName}_HANDLER: HandlerEntry<Three<Completes<${stateName}>, ${aggregateProtocolName}, ${dataName}>> =
    HandlerEntry.of(${indexName}, Three<Completes<${stateName}>, ${aggregateProtocolName}, ${dataName}> {
      ${aggregateProtocolVariable}: ${aggregateProtocolName}, data: ${dataName} -> ${aggregateProtocolVariable}.${methodName}(${methodInvocationParameters})
    })
}
</#if>
