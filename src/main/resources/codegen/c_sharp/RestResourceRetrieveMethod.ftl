public ICompletes<Response> ${routeSignature} => ${routeHandlerInvocation}
    .AndThenTo<ICompletes<Response>>(data => Vlingo.Xoom.Common.Completes.WithSuccess(EntityResponseOf(Ok, JsonSerialization.Serialized(data))))
    .Otherwise<Response>(arg => Response.Of(NotFound))
    .RecoverFrom(e => Response.Of(InternalServerError));
