companion object {
  fun ${methodName}(${methodParameters}) : Completes<${stateName}> {
    val _address = stage.addressFactory().uniquePrefixedWith("g-") : Address
    val _${aggregateProtocolVariable} = stage.actorFor(${aggregateProtocolName}::class.java, Definition.has(${entityName}::class.java, Definition.parameters(_address.idString())), _address) : ${aggregateProtocolName}
    return _${aggregateProtocolVariable}.${methodName}(${methodInvocationParameters})
  }
}
