<#if compositeId?has_content>static Completes<${stateName}> ${methodName}(${methodParameters}) {
    final String prefix = String.format("g-{0}:", ${compositeId});
    final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith(prefix);
    final ${aggregateProtocolName} _${aggregateProtocolVariable} = stage.actorFor(${aggregateProtocolName}.class, Definition.has(${entityName}.class, Definition.parameters(_address.idString())), _address);
    return _${aggregateProtocolVariable}.${methodName}(${methodInvocationParameters});
  }<#else>static Completes<${stateName}> ${methodName}(${methodParameters}) {
    final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final ${aggregateProtocolName} _${aggregateProtocolVariable} = stage.actorFor(${aggregateProtocolName}.class, Definition.has(${entityName}.class, Definition.parameters(_address.idString())), _address);
    return _${aggregateProtocolVariable}.${methodName}(${methodInvocationParameters});
  }
</#if>