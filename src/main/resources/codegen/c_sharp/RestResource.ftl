using Vlingo.Xoom.Actors;
using Vlingo.Xoom.Common;
using Vlingo.Xoom.Common.Serialization;
using Vlingo.Xoom.Http;
using Vlingo.Xoom.Http.Resource;
using Vlingo.Xoom.Lattice.Grid;
<#list imports as import>
using ${import.qualifiedClassName};
</#list>
<#if useCQRS && queries?has_content>
using Vlingo.Xoom.Turbo;
</#if>
using static Vlingo.Xoom.Http.ResponseStatus;

namespace ${packageName};

/**
 * See <a href="https://docs.vlingo.io/xoom-turbo/xoom-annotations#resourcehandlers">@ResourceHandlers</a>
 */
public class ${resourceName}: DynamicResourceHandler
{
  private readonly Grid _grid;
  <#if useCQRS && queries?has_content>
  private readonly ${queries.protocolName} _queries;
  </#if>

  public ${resourceName}(Grid grid): base(grid.World.Stage)
  {
      _grid = grid;
      <#if useCQRS && queries?has_content>
      _queries = ComponentRegistry.WithType<${storeProviderName}>().${queries.attributeName};
      </#if>
      Routes =<#if routeDeclarations?has_content && routeDeclarations?size == 0> ResourceBuilder.Resource("${resourceName}" /*Add Request Handlers here as a second parameter*/);
      <#else> ResourceBuilder.Resource("${resourceName}",
          <#list routeDeclarations as declaration>
              <#if declaration.path?has_content>
            ${declaration.builderMethod}("${declaration.path}")
              <#else>
            ${declaration.builderMethod}("${uriRoot}")
              </#if>
              <#list declaration.parameterTypes as parameterType>
            .Param<${parameterType}>()
              </#list>
              <#if declaration.bodyType?has_content>
            .Body<${declaration.bodyType}>()
              </#if>
            .Handle(${declaration.handlerName})<#if declaration?has_next>,</#if>
          </#list>
        );
      </#if>
  }

  <#list routeMethods as routeMethod>
  ${routeMethod}
  </#list>
  public override Vlingo.Xoom.Http.Resource.Resource Routes { get; }

  protected ContentType ContentType => Vlingo.Xoom.Http.ContentType.Of("application/json", "charset=UTF-8");

  private string Location(string id) => "${uriRoot?replace("/$", "")?replace("{", "\" + ")?replace("}", " + \"")}/" + id;

  <#if modelProtocol?has_content>
  private ICompletes<${modelProtocol}> Resolve(string id)
  {
    var address = _grid.AddressFactory.From(id);
    return _grid.ActorOf<${modelProtocol}>(address, Definition.Has(typeof(${modelActor}), Definition.Parameters(id)));
  }
  </#if>

}
