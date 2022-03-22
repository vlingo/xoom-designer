public Completes<Response> ${routeSignature} {
    <#list valueObjectInitializers as initializer>
    ${initializer}
    </#list>
    return ${routeHandlerInvocation}
      .andThenTo(state -> Completes.withSuccess(entityResponseOf(Created, ResponseHeader.headers(ResponseHeader.of(Location, location(${compositeId}state.id))), serialized(${adapterHandlerInvocation})))
      .otherwise(arg -> Response.of(NotFound))
      .recoverFrom(e -> Response.of(InternalServerError, e.getMessage())));
  }
