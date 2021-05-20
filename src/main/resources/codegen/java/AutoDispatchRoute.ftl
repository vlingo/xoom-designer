<#if routePath?has_content>
@Route(method = ${routeMethod}, path = "${routePath}", handler = ${autoDispatchHandlersMappingName}.${routeMappingValue})
<#else>
@Route(method = ${routeMethod}, handler = ${autoDispatchHandlersMappingName}.${routeMappingValue})
</#if>
<#if retrievalRoute>
  Completes<Response> ${methodName}(${methodParameters});
<#else>
  @ResponseAdapter(handler = ${autoDispatchHandlersMappingName}.ADAPT_STATE)
  <#if requireEntityLoading>
  Completes<Response> ${methodName}(@Id final ${idType} id, @Body final ${dataName} data);
  <#else>
  Completes<Response> ${methodName}(@Body final ${dataName} data);
  </#if>
</#if>
