public ICompletes<Response> ${routeSignature}
  <#if valueObjectInitializers?has_content>
  {
      <#list valueObjectInitializers as initializer>
          ${initializer}
      </#list>
    return <#else> =></#if> ${routeHandlerInvocation}
      .AndThenTo(state => Completes().WithSuccess(EntityResponseOf(Created, ResponseHeader.Headers(ResponseHeader.Of(Location, Location(state.Id))), Serialized(${adapterHandlerInvocation})))
      .Otherwise(arg => Response.Of(NotFound))
      .RecoverFrom(e => Response.Of(InternalServerError));
  <#if valueObjectInitializers?has_content>
  }
  </#if>