public ICompletes<Response> ${routeSignature}
  <#if valueObjectInitializers?has_content>
  {
      <#list valueObjectInitializers as initializer>
          ${initializer}
      </#list>
    return <#else> =></#if> ${routeHandlerInvocation}
      .AndThenTo<ICompletes<Response>>(state => Vlingo.Xoom.Common.Completes.WithSuccess(EntityResponseOf(Created, Headers.Of(ResponseHeader.Of(ResponseHeader.Location, Location(state.Id))), JsonSerialization.Serialized(${adapterHandlerInvocation})))
      .Otherwise<Response>(arg => Response.Of(NotFound))
      .RecoverFrom(e => Response.Of(InternalServerError));
  <#if valueObjectInitializers?has_content>
  }
  </#if>