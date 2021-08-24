package ${packageName};

<#if modelProtocol?has_content>
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Address;
</#if>
<#if useAutoDispatch>
import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.turbo.annotation.autodispatch.Handler;
</#if>
<#if queries?has_content && !queries.empty>
import io.vlingo.xoom.turbo.ComponentRegistry;
</#if>
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.ContentType;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.ResponseHeader;
import io.vlingo.xoom.http.resource.Resource;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import io.vlingo.xoom.lattice.grid.Grid;
<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import static io.vlingo.xoom.common.serialization.JsonSerialization.serialized;
import static io.vlingo.xoom.http.Response.Status.*;
import static io.vlingo.xoom.http.ResponseHeader.Location;
import static io.vlingo.xoom.http.resource.ResourceBuilder.resource;

/**
 * See <a href="https://docs.vlingo.io/xoom-turbo/xoom-annotations#resourcehandlers">@ResourceHandlers</a>
 */
<#if useAutoDispatch>
public class ${resourceName} extends DynamicResourceHandler implements ${autoDispatchMappingName} {
<#else>
public class ${resourceName} extends DynamicResourceHandler {
</#if>
  <#if useAutoDispatch>
  private final Grid $stage;
  private final Logger $logger;
  <#else>
  private final Grid grid;
  </#if>
  <#if queries?has_content && !queries.empty>
  private final ${queries.protocolName} $queries;
  </#if>

  public ${resourceName}(final Grid grid) {
      super(grid.world().stage());
      <#if useAutoDispatch>
      this.$stage = grid;
      this.$logger = super.logger();
      <#else>
      this.grid = grid;
      </#if>
      <#if queries?has_content && !queries.empty>
      this.$queries = ComponentRegistry.withType(${storeProviderName}.class).${queries.attributeName};
      </#if>
  }

  <#list routeMethods as routeMethod>
  ${routeMethod}
  </#list>
  @Override
  public Resource<?> routes() {
  <#if routeDeclarations?has_content && routeDeclarations?size == 0>
     return resource("${resourceName}" /*Add Request Handlers here as a second parameter*/);
  <#else>
     return resource("${resourceName}",
     <#list routeDeclarations as declaration>
        <#if declaration.path?has_content>
        ${declaration.builderMethod}("${declaration.path}")
        <#else>
        ${declaration.builderMethod}("${uriRoot}")
        </#if>
         <#list declaration.parameterTypes as parameterType>
            .param(${parameterType}.class)
         </#list>
         <#if declaration.bodyType?has_content>
            .body(${declaration.bodyType}.class)
         </#if>
            .handle(this::${declaration.handlerName})<#if declaration?has_next>,</#if>
     </#list>
     );
  </#if>
  }

  @Override
  protected ContentType contentType() {
    return ContentType.of("application/json", "charset=UTF-8");
  }

  private String location(final String id) {
    return "${uriRoot?replace("/$", "")}/" + id;
  }

  <#if modelProtocol?has_content>
  private Completes<${modelProtocol}> resolve(final String id) {
    <#if useAutoDispatch>
    final Address address = $stage.addressFactory().from(id);
    return $stage.actorOf(${modelProtocol}.class, address, Definition.has(${modelActor}.class, Definition.parameters(id)));
    <#else>
    final Address address = grid.addressFactory().from(id);
    return grid.actorOf(${modelProtocol}.class, address, Definition.has(${modelActor}.class, Definition.parameters(id)));
    </#if>
  }
  </#if>

}
