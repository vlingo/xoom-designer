public ICompletes<Response> ${routeSignature} => ${routeHandlerInvocation}
    .AndThenTo(data => Completes().WithSuccess(EntityResponseOf(Ok, Serialized(data))))
    .Otherwise(arg => Response.Of(NotFound))
    .RecoverFrom(e => Response.Of(InternalServerError, e.Message));
