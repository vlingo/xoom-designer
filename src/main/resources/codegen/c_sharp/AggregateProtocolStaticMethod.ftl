static ICompletes<${stateName}> ${methodName}(Stage stage, ${methodParameters})
  {
    var address = stage.AddressFactory.UniquePrefixedWith("g-");
    var ${aggregateProtocolVariable} = stage.ActorFor<I${aggregateProtocolName}>(Definition.Has(typeof(${entityName}), Definition.Parameters(address.IdString)), address);
    return ${aggregateProtocolVariable}.${methodName}(${methodInvocationParameters});
  }
