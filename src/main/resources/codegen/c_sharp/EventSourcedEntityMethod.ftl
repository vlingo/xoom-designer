  public ICompletes<${stateName}> ${methodName}(${methodParameters})
  {
    <#if domainEventName?has_content>
    /**
     * TODO: Implement command logic. See {@link ${stateName}#${methodName}()}
     */
    return Apply(new ${domainEventName}(${domainEventConstructorParameters}), () => _state);
    <#else>
    /**
     * TODO: Unable to generate method body because there is no associated Domain Event.
     */
    return Completes().WithFailure();
    </#if>
  }
