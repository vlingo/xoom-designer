package ${packageName}

<#if modelProtocol?has_content>
import io.vlingo.xoom.actors.Definition
</#if>
import io.vlingo.xoom.actors.Stage
import io.vlingo.xoom.http.resource.Resource
import io.vlingo.xoom.http.resource.DynamicResourceHandler
import io.vlingo.xoom.http.resource.ResourceBuilder.resource

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

import io.vlingo.xoom.http.Response
import io.vlingo.xoom.common.Completes
import io.vlingo.xoom.common.serialization.JsonSerialization.serialized
import io.vlingo.xoom.http.Response.Status.*
import io.vlingo.xoom.http.ResponseHeader.*
<#if useAutoDispatch>
import io.vlingo.xoom.actors.Logger
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler
</#if>

/**
 * See <a href="https://docs.vlingo.io/xoom-turbo/xoom-annotations#resourcehandlers">@ResourceHandlers</a>
 */
<#if useAutoDispatch>
public class ${resourceName} : DynamicResourceHandler, ${autoDispatchMappingName} {
<#else>
public class ${resourceName} : DynamicResourceHandler {
</#if>
  <#if useAutoDispatch>
  val $stage: Stage
  val $logger: Logger
  </#if>
  <#if queries?has_content && !queries.empty>
  val $queries: ${queries.protocolName}
  </#if>

  public constructor(stage: Stage) : super(stage){
      <#if useAutoDispatch>
      this.$stage = super.stage()
      this.$logger = super.logger()
      </#if>
      <#if queries?has_content && !queries.empty>
      this.$queries = ${storeProviderName}.instance().${queries.attributeName}
      </#if>
  }

  <#list routeMethods as routeMethod>
  ${routeMethod}
  </#list>
  public override fun routes(): Resource<?> {
  <#if routeDeclarations?has_content && routeDeclarations?size == 0>
     return resource("${resourceName}" /*Add Request Handlers here as a second parameter*/)
  <#else>
     return resource("${resourceName}",
     <#list routeDeclarations as declaration>
        <#if declaration.path?has_content>
        ${declaration.builderMethod}("${declaration.path}")
        <#else>
        ${declaration.builderMethod}("${uriRoot}")
        </#if>
         <#list declaration.parameterTypes as parameterType>
            .param(${parameterType}::class.java)
         </#list>
         <#if declaration.bodyType?has_content>
            .body(${declaration.bodyType}::class.java)
         </#if>
         <#if declaration.last>
            .handle(this::${declaration.handlerName})
         <#else>
            .handle(this::${declaration.handlerName}),
         </#if>
     </#list>
     )
  </#if>
  }

  fun location(): String {
    return location("")
  }

  fun location(id: String): String {
    return "${uriRoot?replace("/$", "")}/" + id;
  }

  <#if modelProtocol?has_content>
  fun resolve(id: String): Completes<${modelProtocol}> {
    return stage().actorOf(${modelProtocol}::class.java, stage().addressFactory().from(id), Definition.has(${modelActor}::class.java, Definition.parameters(id)))
  }
  </#if>

}
