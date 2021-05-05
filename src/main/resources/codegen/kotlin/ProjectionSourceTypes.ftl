package ${packageName}

public enum ${projectionSourceTypesName} {
<#list sourceNames as source>
  ${source},
</#list>
}
