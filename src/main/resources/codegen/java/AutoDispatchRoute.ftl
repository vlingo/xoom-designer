<#if routePath?has_content>
@Route(method = ${routeMethod}, path = "${routePath}", handler = ${autoDispatchHandlersMappingName}.${routeMappingValue})
<#else>
@Route(method = ${routeMethod}, handler = ${autoDispatchHandlersMappingName}.${routeMappingValue})
</#if>
<#if compositeId?has_content>
  <#if retrievalRoute>
  <#if methodParameters?has_content>
  Completes<Response> ${methodName}(${compositeId}${methodParameters});
  <#else>
  Completes<Response> ${methodName}(${compositeId?substring(0, compositeId?length - 2)}${methodParameters});
  </#if>
  <#else>
  @ResponseAdapter(handler = ${autoDispatchHandlersMappingName}.ADAPT_STATE)
  <#if requireEntityLoading>
  Completes<Response> ${methodName}(${compositeId}@Id final ${idType} id, @Body final ${dataName} data);
  <#else>
  Completes<Response> ${methodName}(${compositeId}@Body final ${dataName} data);
  </#if>
  </#if>
<#else>
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
</#if>
