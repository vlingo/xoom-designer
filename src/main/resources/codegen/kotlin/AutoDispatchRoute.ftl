<#if routePath?has_content>
@Route(method = ${routeMethod}, path = "${routePath}", handler = ${autoDispatchHandlersMappingName}.${routeMappingValue})
<#else>
@Route(method = ${routeMethod}, handler = ${autoDispatchHandlersMappingName}.${routeMappingValue})
</#if>
<#if retrievalRoute>
  fun ${methodName}(): Completes<Response>
<#else>
  @ResponseAdapter(handler = ${autoDispatchHandlersMappingName}.ADAPT_STATE)
  <#if requireEntityLoading>
  fun ${methodName}(@Id id: ${idType}, @Body data: ${dataName}): Completes<Response>
  <#else>
  fun ${methodName}(@Body data: ${dataName} ): Completes<Response>
  </#if>
</#if>
