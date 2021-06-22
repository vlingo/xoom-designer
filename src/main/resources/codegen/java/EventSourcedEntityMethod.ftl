@Override
  public Completes<${stateName}> ${methodName}(${methodParameters}) {
    <#if domainEventName?has_content>
    /**
     * TODO: Implement command logic. See {@link ${stateName}#${methodName}()}
     */
    return apply(new ${domainEventName}(state.id<#if methodInvocationParameters?has_content>, ${methodInvocationParameters}</#if>), () -> state);
    <#else>
    /**
     * TODO: Unable to generate method body because there is no associated Domain Event.
     */
    return Completes.withFailure();
    </#if>
  }
