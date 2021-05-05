public fun ${methodName}(${methodParameters}): Completes<${stateName}> {
    val stateArg: ${stateName} = state.${methodName}(${methodInvocationParameters})
    return apply(stateArg, ${domainEventName}(stateArg)){state}
  }
