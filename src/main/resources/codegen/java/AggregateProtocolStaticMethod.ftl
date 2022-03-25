static Completes<${stateName}> ${methodName}(${methodParameters}) {
    final io.vlingo.xoom.actors.Address _address = stage.addressFactory().uniquePrefixedWith("g-");
    final ${aggregateProtocolName} _${aggregateProtocolVariable} = stage.actorFor(${aggregateProtocolName}.class, Definition.has(${entityName}.class, Definition.parameters(_address.idString())), _address);
    return _${aggregateProtocolVariable}.${methodName}(${methodInvocationParameters});
  }
