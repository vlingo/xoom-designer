public ${stateName} ${methodName}(${methodParameters})
  {
    //TODO: Implement command logic.
    <#list collectionMutations as collectionMutation>
    ${collectionMutation}
    </#list>
    return new ${stateName}(${constructorParameters});
  }
