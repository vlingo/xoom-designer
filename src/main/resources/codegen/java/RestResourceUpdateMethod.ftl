public Completes<Response> ${routeSignature} {
    <#list valueObjectInitializers as initializer>
    ${initializer}
    </#list>
    return resolve(${compositeId}${idName})
            .andThenTo(${modelAttribute} -> ${routeHandlerInvocation})
            .andThenTo(state -> Completes.withSuccess(entityResponseOf(Ok, serialized(${adapterHandlerInvocation}))))
            .otherwise(noGreeting -> Response.of(NotFound))
            .recoverFrom(e -> Response.of(InternalServerError, e.getMessage()));
  }
