@Override
  public Completes<${stateName}> ${methodName}(${methodParameters}) {
    /**
     * TODO: Implement command logic. See {@link ${stateName}#${methodName}()}
     */
    <#if domainEventName?has_content>
    <#if operationBased>
    return apply(state.${methodName}(${methodInvocationParameters}), ${projectionSourceTypesName}.${domainEventName}.name(), () -> state);
    <#else>
    return apply(state.${methodName}(${methodInvocationParameters}), new ${domainEventName}(state.id<#if methodInvocationParameters?has_content>, ${methodInvocationParameters}</#if>), () -> state);
    </#if>
    <#else>
    return apply(state.${methodName}(${methodInvocationParameters}), () -> state);
    </#if>
  }
