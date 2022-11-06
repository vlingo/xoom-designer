public ICompletes<Response> ${routeSignature}
  <#if valueObjectInitializers?has_content>
  {
    <#list valueObjectInitializers as initializer>
    ${initializer}
    </#list>
    return <#else> =></#if> Resolve(${idName})
        .AndThenTo<ICompletes<${stateName}>>(${modelAttribute} => ${routeHandlerInvocation})
        .AndThenTo<ICompletes<Response>>(state => Vlingo.Xoom.Common.Completes.WithSuccess(EntityResponseOf(Ok, JsonSerialization.Serialized(${adapterHandlerInvocation}))))
        .Otherwise<Response>(noGreeting => Response.Of(NotFound))
        .RecoverFrom(e => Response.Of(InternalServerError));
  <#if valueObjectInitializers?has_content>
  }
  </#if>
