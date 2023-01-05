public ICompletes<${stateName}> ${methodName}(${methodParameters})
  {
    return Apply(_state.${methodName}(${methodInvocationParameters}), () => _state);
  }
