<#list imports as import>
using ${import.qualifiedClassName};
</#list>

namespace ${packageName};

[Persistence(basePackage = "${basePackage}", storageType = StorageType.${storageType}, cqrs = ${useCQRS?c})]
<#if useProjections>
[Projections(value = {
<#list projections as projection>
  <#if projection.last>
  Projection(actor = typeof(${projection.actor}), becauseOf = {${projection.causes}})
  <#else>
  Projection(actor = typeof(${projection.actor}), becauseOf = {${projection.causes}}),
  </#if>
</#list>
}, type = ProjectionType.${projectionType})]
</#if>
<#if requireAdapters>
[Adapters({
<#list adapters as stateAdapter>
  <#if stateAdapter.last>
  typeof(${stateAdapter.sourceClass})
  <#else>
  typeof(${stateAdapter.sourceClass}),
  </#if>
</#list>
})]
</#if>
<#if queries?size gt 0>
[EnableQueries({
<#list queries as query>
  QueriesEntry(protocol = typeof(${query.protocolName}), actor = typeof(${query.actorName})),
</#list>
})]
</#if>
<#if dataObjects?has_content>
[DataObjects({${dataObjects}})]
</#if>
public class PersistenceSetup
{

}