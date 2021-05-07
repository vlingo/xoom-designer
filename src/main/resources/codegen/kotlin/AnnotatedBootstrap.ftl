package ${packageName}

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

import io.vlingo.xoom.actors.Stage
import io.vlingo.xoom.turbo.XoomInitializationAware
import io.vlingo.xoom.turbo.annotation.initializer.AddressFactory
import io.vlingo.xoom.turbo.annotation.initializer.Xoom

import io.vlingo.xoom.turbo.annotation.initializer.AddressFactory.Type.UUID

@Xoom(name = "${appName}", addressFactory = @AddressFactory(type = UUID))
<#if restResourcePackage?has_content>
@ResourceHandlers(packages = "${restResourcePackage}")
</#if>
public class Bootstrap : XoomInitializationAware {

  public override void onInit(stage: Stage) {
  }

<#if hasProducerExchange>
  public override io.vlingo.xoom.symbio.store.dispatch.Dispatcher exchangeDispatcher(stage: Stage) {
     return ExchangeBootstrap.init(stage).dispatcher()
  }
</#if>
}
