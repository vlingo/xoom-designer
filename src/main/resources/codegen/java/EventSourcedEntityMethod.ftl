@Override
  public Completes<${stateName}> ${methodName}(${methodParameters}) {
    /**
     * TODO: Implement command logic. See {@link ${stateName}#${methodName}()}
     */
    <#if domainEventName?has_content>
    return apply(new ${domainEventName}(state.id<#if methodInvocationParameters?has_content>, ${methodInvocationParameters}</#if>), () -> state);
    <#else>
    return Completes.withSuccess(state.${methodName}(${methodInvocationParameters}));
    </#if>
  }
