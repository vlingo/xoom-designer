namespace ${packageName};

public enum ${projectionSourceTypesName}
{
<#list sources as source>
  ${source},
</#list>
}
