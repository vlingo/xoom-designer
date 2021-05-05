package ${packageName}

import io.vlingo.xoom.turbo.annotation.persistence.Persistence
import io.vlingo.xoom.turbo.annotation.persistence.Persistence.StorageType
<#if useProjections>
import io.vlingo.xoom.turbo.annotation.persistence.Projections
import io.vlingo.xoom.turbo.annotation.persistence.Projection
</#if>
<#if requireAdapters>
import io.vlingo.xoom.turbo.annotation.persistence.Adapters
</#if>
<#if queries?size gt 0>
import io.vlingo.xoom.turbo.annotation.persistence.EnableQueries
import io.vlingo.xoom.turbo.annotation.persistence.QueriesEntry
</#if>
<#if dataObjects?has_content>
import io.vlingo.xoom.turbo.annotation.persistence.DataObjects
</#if>
<#list imports as import>
import ${import.qualifiedClassName}
</#list>

@Persistence(basePackage = "${basePackage}", storageType = StorageType.${storageType}, cqrs = ${useCQRS?c})
<#if useProjections>
@Projections({
<#list projections as projection>
  <#if projection.last>
  @Projection(actor = ${projection.actor}::class.java, becauseOf = {${projection.causes}})
  <#else>
  @Projection(actor = ${projection.actor}::class.java, becauseOf = {${projection.causes}}),
  </#if>
</#list>
})
</#if>
<#if requireAdapters>
@Adapters({
<#list adapters as stateAdapter>
  <#if stateAdapter.last>
  ${stateAdapter.sourceClass}::class.java
  <#else>
  ${stateAdapter.sourceClass}::class.java,
  </#if>
</#list>
})
</#if>
<#if queries?size gt 0>
@EnableQueries({
<#list queries as query>
  @QueriesEntry(protocol = ${query.protocolName}::class.java, actor = ${query.actorName}::class.java),
</#list>
})
</#if>
<#if dataObjects?has_content>
@DataObjects({${dataObjects}})
</#if>
public class PersistenceSetup {


}
