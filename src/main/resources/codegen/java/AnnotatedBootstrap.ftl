package ${packageName};

<#if imports?has_content>
<#list imports as import>
import ${import.qualifiedClassName};
</#list>
</#if>

import io.vlingo.xoom.http.resource.SinglePageApplicationConfiguration;
import io.vlingo.xoom.lattice.grid.Grid;
import io.vlingo.xoom.turbo.XoomInitializationAware;
import io.vlingo.xoom.turbo.annotation.initializer.Xoom;

@Xoom(name = "${appName}")
<#if restResourcePackage?has_content>
@ResourceHandlers(packages = "${restResourcePackage}")
</#if>
public class Bootstrap implements XoomInitializationAware {

  @Override
  public void onInit(final Grid grid) {
  }

  @Override
  public SinglePageApplicationConfiguration singlePageApplicationResource() {
    return SinglePageApplicationConfiguration.defineWith("/frontend", "/app");
  }

}
