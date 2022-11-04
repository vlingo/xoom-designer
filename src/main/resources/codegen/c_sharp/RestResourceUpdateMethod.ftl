public ICompletes<Response> ${routeSignature}
  <#if valueObjectInitializers?has_content>
  {
    <#list valueObjectInitializers as initializer>
    ${initializer}
    </#list>
    return <#else> =></#if> Resolve(${idName})
        .AndThenTo(${modelAttribute} => ${routeHandlerInvocation})
        .AndThenTo(state => Completes().WithSuccess(EntityResponseOf(Ok, Serialized(${adapterHandlerInvocation}))))
        .Otherwise(noGreeting => Response.Of(NotFound))
        .RecoverFrom(e => Response.Of(InternalServerError));
  <#if valueObjectInitializers?has_content>
  }
  </#if>
