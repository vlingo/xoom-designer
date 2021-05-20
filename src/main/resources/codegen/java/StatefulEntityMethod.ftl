@Override
  public Completes<${stateName}> ${methodName}(${methodParameters}) {
    /**
     * TODO: Implement command logic. See {@link ${stateName}#${methodName}()}
     */
    final ${stateName} stateArg = state.${methodName}(${methodInvocationParameters});
    <#if domainEventName?has_content>
    <#if operationBased>
    return apply(stateArg, ${projectionSourceTypesName}.${domainEventName}.name(), () -> state);
    <#else>
    return apply(stateArg, new ${domainEventName}(stateArg), () -> state);
    </#if>
    <#else>
    return apply(stateArg, () -> state);
    </#if>
  }
