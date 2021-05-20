${schemaCategory} ${schemataSpecificationName} {
  version semanticVersion
  <#list fieldsDeclarations as declaration>
  ${declaration}
  </#list>
}