  public fun ${methodName}(${methodParameters}): Completes<${stateName}> {
    return apply(${domainEventName}(state)) {state}
  }
