<#if routePath?has_content>
[Route(method = ${routeMethod}, path = "${routePath}", handler = ${autoDispatchHandlersMappingName}.${routeMappingValue})]
<#else>
[Route(method = ${routeMethod}, handler = ${autoDispatchHandlersMappingName}.${routeMappingValue})]
</#if>
<#if retrievalRoute>
  <#if methodParameters?has_content>
  ICompletes<Response> ${methodName}(${methodParameters});
  <#else>
  ICompletes<Response> ${methodName}(${methodParameters});
  </#if>
  <#else>
  [ResponseAdapter(handler = ${autoDispatchHandlersMappingName}.ADAPT_STATE)]
  <#if requireEntityLoading>
  ICompletes<Response> ${methodName}([Id] ${idType} id, [Body] ${dataName} data);
  <#else>
  ICompletes<Response> ${methodName}([Body] ${dataName} data);
  </#if>
</#if>
