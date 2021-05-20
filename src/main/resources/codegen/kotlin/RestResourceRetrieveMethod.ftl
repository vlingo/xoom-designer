public fun ${routeSignature}: Completes<Response> {
    return ${routeHandlerInvocation}
            .andThenTo{data -> Completes.withSuccess(Response.of(Ok, serialized(data)))}
            .otherwise{arg -> Response.of(NotFound)}
            .recoverFrom{e -> Response.of(InternalServerError, e.getMessage())}
  }
