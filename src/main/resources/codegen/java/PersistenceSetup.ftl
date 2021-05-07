package ${packageName};

import io.vlingo.xoom.turbo.annotation.persistence.Persistence;
import io.vlingo.xoom.turbo.annotation.persistence.Persistence.StorageType;
<#if useProjections>
import io.vlingo.xoom.turbo.annotation.persistence.Projections;
import io.vlingo.xoom.turbo.annotation.persistence.Projection;
import io.vlingo.xoom.turbo.annotation.persistence.ProjectionType;
</#if>
<#if requireAdapters>
import io.vlingo.xoom.turbo.annotation.persistence.Adapters;
</#if>
<#if queries?size gt 0>
import io.vlingo.xoom.turbo.annotation.persistence.EnableQueries;
import io.vlingo.xoom.turbo.annotation.persistence.QueriesEntry;
</#if>
<#if dataObjects?has_content>
import io.vlingo.xoom.turbo.annotation.persistence.DataObjects;
</#if>
<#list imports as import>
import ${import.qualifiedClassName};
</#list>

@SuppressWarnings("unused")
@Persistence(basePackage = "${basePackage}", storageType = StorageType.${storageType}, cqrs = ${useCQRS?c})
<#if useProjections>
@Projections(value = {
<#list projections as projection>
  <#if projection.last>
  @Projection(actor = ${projection.actor}.class, becauseOf = {${projection.causes}})
  <#else>
  @Projection(actor = ${projection.actor}.class, becauseOf = {${projection.causes}}),
  </#if>
</#list>
}, type = ProjectionType.${projectionType})
</#if>
<#if requireAdapters>
@Adapters({
<#list adapters as stateAdapter>
  <#if stateAdapter.last>
  ${stateAdapter.sourceClass}.class
  <#else>
  ${stateAdapter.sourceClass}.class,
  </#if>
</#list>
})
</#if>
<#if queries?size gt 0>
@EnableQueries({
<#list queries as query>
  @QueriesEntry(protocol = ${query.protocolName}.class, actor = ${query.actorName}.class),
</#list>
})
</#if>
<#if dataObjects?has_content>
@DataObjects({${dataObjects}})
</#if>
public class PersistenceSetup {


}