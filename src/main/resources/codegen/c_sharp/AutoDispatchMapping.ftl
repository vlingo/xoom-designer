<#list imports as import>
using ${import.qualifiedClassName};
</#list>

namespace ${packageName};

[AutoDispatch(path="${uriRoot}", handlers=typeof(${autoDispatchHandlersMappingName}))]
<#if useCQRS>
[Queries(protocol = typeof(${queriesName}), actor = typeof(${queriesActorName}))]
</#if>
[Model(protocol = typeof(${aggregateProtocolName}), actor = typeof(${entityName}), data = typeof(${dataName}))
public interface ${autoDispatchMappingName}
{

  <#list routeDeclarations as declaration>
  ${declaration}
  </#list>
}