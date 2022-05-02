static ICompletes<${stateName}> ${methodName}(Stage stage, ${methodParameters})
  {
    var _address = stage.AddressFactory.UniquePrefixedWith("g-");
    var _${aggregateProtocolVariable} = stage.ActorFor<I${aggregateProtocolName}>(Definition.Has(typeof(${entityName}), Definition.Parameters(_address.IdString)), _address);
    return _${aggregateProtocolVariable}.${methodName}(${methodInvocationParameters});
  }
