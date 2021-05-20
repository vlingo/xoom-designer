package ${packageName}

import io.vlingo.xoom.common.Completes
import io.vlingo.xoom.turbo.annotation.autodispatch.*
import io.vlingo.xoom.http.Response

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

import io.vlingo.xoom.http.Method.*

@AutoDispatch(path="${uriRoot}", handlers=${autoDispatchHandlersMappingName}::class.java)
<#if useCQRS>
@Queries(protocol = ${queriesName}::class.java, actor = ${queriesActorName}::class.java)
</#if>
@Model(protocol = ${aggregateProtocolName}::class.java, actor = ${entityName}::class.java, data = ${dataName}::class.java)
public interface ${autoDispatchMappingName} {

  <#list routeDeclarations as declaration>
  ${declaration}
  </#list>
}
