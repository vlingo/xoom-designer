package ${packageName};

import io.micronaut.context.annotation.Context;
import io.vlingo.actors.World;
import io.vlingo.xoom.server.VlingoScene;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import ${registryQualifiedClassName};
<#if useProjections>
import ${projectionDispatcherProviderQualifiedClassName};
</#if>

@Context
public class StoreProviderConfiguration {

  @Inject
  private VlingoScene vlingoScene;

  @PostConstruct
  public void setupStorage()  {
    final World world = vlingoScene.getWorld();
    final ${registryClassName} registry = new ${registryClassName}(world);
<#list storeProviders as storeProvider>
    <#if storeProvider.useProjections>
    ${storeProvider.name}.using(world.stage(), registry, ${projectionDispatcherProviderName}.using(world.stage()).storeDispatcher);
    <#else>
    ${storeProvider.name}.using(world.stage(), registry);
    </#if>
</#list>
  }
}