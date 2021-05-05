package ${packageName};

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.turbo.annotation.autodispatch.*;
import io.vlingo.xoom.http.Response;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import static io.vlingo.xoom.http.Method.*;

@AutoDispatch(path="${uriRoot}", handlers=${autoDispatchHandlersMappingName}.class)
<#if useCQRS>
@Queries(protocol = ${queriesName}.class, actor = ${queriesActorName}.class)
</#if>
@Model(protocol = ${aggregateProtocolName}.class, actor = ${entityName}.class, data = ${dataName}.class)
public interface ${autoDispatchMappingName} {

  <#list routeDeclarations as declaration>
  ${declaration}
  </#list>
}