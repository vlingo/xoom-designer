package ${packageName}

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

interface ${aggregateProtocolName} {

  <#list methods as method>
  ${method}
  </#list>
}
