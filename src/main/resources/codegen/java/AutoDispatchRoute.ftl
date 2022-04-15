<#if routePath?has_content>
@Route(method = ${routeMethod}, path = "${routePath}", handler = ${autoDispatchHandlersMappingName}.${routeMappingValue})
<#else>
@Route(method = ${routeMethod}, handler = ${autoDispatchHandlersMappingName}.${routeMappingValue})
</#if>
<#if compositeId?has_content>
  <#assign queryAllCompositeId=compositeId?substring(0, compositeId?length - 2) />
<#else>
  <#assign queryAllCompositeId=compositeId />
</#if>
<#if retrievalRoute>
  <#if methodParameters?has_content>
  Completes<Response> ${methodName}(${compositeId}${methodParameters});
  <#else>
  Completes<Response> ${methodName}(${queryAllCompositeId}${methodParameters});
  </#if>
  <#else>
  @ResponseAdapter(handler = ${autoDispatchHandlersMappingName}.ADAPT_STATE)
  <#if requireEntityLoading>
  Completes<Response> ${methodName}(${compositeId}@Id final ${idType} id, @Body final ${dataName} data);
  <#else>
  Completes<Response> ${methodName}(${compositeId}@Body final ${dataName} data);
  </#if>
</#if>
