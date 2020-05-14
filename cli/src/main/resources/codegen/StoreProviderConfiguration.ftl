package ${packageName};

import io.micronaut.context.annotation.Context;
import io.vlingo.actors.World;
import io.vlingo.xoom.server.VlingoScene;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import ${registryQualifiedClassName};

@Context
public class StoreProviderConfiguration {

  @Inject
  private VlingoScene vlingoScene;

  @PostConstruct
  public void setupStorage()  {
    final World world = vlingoScene.getWorld();
    final ${registryClassName} registry = new ${registryClassName}(world);
<#list storeProviders as storeProvider>
    ${storeProvider.name}.using(world.stage(), registry);
</#list>
  }
}