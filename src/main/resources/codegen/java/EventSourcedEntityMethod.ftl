@Override
  public Completes<${stateName}> ${methodName}(${methodParameters}) {
    /**
     * TODO: Implement command logic. See {@link ${stateName}#${methodName}()}
     */
    final ${stateName} stateArg = state.${methodName}(${methodInvocationParameters});
    <#if domainEventName?has_content>
    return apply(new ${domainEventName}(stateArg), () -> state);
    <#else>
    return Completes.withSuccess(stateArg);
    </#if>
  }
