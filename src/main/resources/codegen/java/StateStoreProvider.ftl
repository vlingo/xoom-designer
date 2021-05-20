package ${packageName};

import java.util.Arrays;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import io.vlingo.xoom.actors.Stage;
<#if persistentTypes?has_content>
import io.vlingo.xoom.symbio.store.state.StateTypeStateStoreMap;
</#if>
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
<#if requireAdapters>
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.xoom.symbio.StateAdapterProvider;
</#if>
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.turbo.actors.Settings;
import io.vlingo.xoom.turbo.storage.Model;
import io.vlingo.xoom.turbo.storage.StoreActorBuilder;
import io.vlingo.xoom.turbo.annotation.persistence.Persistence.StorageType;

@SuppressWarnings("all")
public class ${storeProviderName} {
  private static ${storeProviderName} instance;

  public final StateStore store;
  <#list queries as query>
  public final ${query.protocolName} ${query.attributeName};
  </#list>

  public static ${storeProviderName} instance() {
    return instance;
  }

  public static ${storeProviderName} using(final Stage stage, final StatefulTypeRegistry registry) {
    return using(stage, registry, new NoOpDispatcher());
  }

  public static ${storeProviderName} using(final Stage stage, final StatefulTypeRegistry registry, final Dispatcher ...dispatchers) {
    if (instance != null) {
      return instance;
    }

<#if requireAdapters>
    final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
<#list adapters as stateAdapter>
    stateAdapterProvider.registerAdapter(${stateAdapter.sourceClass}.class, new ${stateAdapter.adapterClass}());
</#list>

</#if>
    new EntryAdapterProvider(stage.world()); // future use

<#list persistentTypes as persistentType>
    StateTypeStateStoreMap.stateTypeToStoreName(${persistentType}.class, ${persistentType}.class.getSimpleName());
</#list>

    final StateStore store =
            StoreActorBuilder.from(stage, Model.${model}, Arrays.asList(dispatchers), StorageType.STATE_STORE, Settings.properties(), true);

<#if requireAdapters>
<#list adapters as stateAdapter>
    registry.register(new Info(store, ${stateAdapter.sourceClass}.class, ${stateAdapter.sourceClass}.class.getSimpleName()));
</#list>
</#if>

    instance = new ${storeProviderName}(stage, store);

    return instance;
  }

  private ${storeProviderName}(final Stage stage, final StateStore store) {
    this.store = store;
    <#list queries as query>
    this.${query.attributeName} = stage.actorFor(${query.protocolName}.class, ${query.actorName}.class, store);
    </#list>
  }
}
