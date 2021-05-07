package ${packageName}

import java.util.Arrays
import java.util.List

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

import io.vlingo.xoom.actors.Definition
import io.vlingo.xoom.actors.Protocols
import io.vlingo.xoom.actors.Stage
<#if persistentTypes?has_content>
import io.vlingo.xoom.symbio.store.state.StateTypeStateStoreMap
</#if>
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry
<#if requireAdapters>
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry.Info
import io.vlingo.xoom.symbio.StateAdapterProvider
</#if>
import io.vlingo.xoom.symbio.EntryAdapterProvider
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl
import io.vlingo.xoom.symbio.store.dispatch.Dispatchable
import io.vlingo.xoom.symbio.store.state.StateStore
import io.vlingo.xoom.turbo.actors.Settings
import io.vlingo.xoom.turbo.storage.Model
import io.vlingo.xoom.turbo.storage.StoreActorBuilder
import io.vlingo.xoom.turbo.annotation.persistence.Persistence.StorageType


public class ${storeProviderName} {

  companion object {
    instance: ${storeProviderName}

    public fun instance(): ${storeProviderName} {
      return instance
    }

    public fun using(stage: Stage, registry: StatefulTypeRegistry): ${storeProviderName} {
      return using(stage, registry, NoOpDispatcher())
    }

    @SuppressWarnings("rawtypes")
    public fun using(stage: Stage, registry: StatefulTypeRegistry, vararg dispatchers: Dispatcher): ${storeProviderName} {
      if (instance != null) {
        return instance
      }

      <#if requireAdapters>
        val stateAdapterProvider = StateAdapterProvider(stage.world())
          <#list adapters as stateAdapter>
            stateAdapterProvider.registerAdapter(${stateAdapter.sourceClass}::class.java, ${stateAdapter.adapterClass}())
          </#list>
      </#if>
      EntryAdapterProvider(stage.world()) // future use

      <#list persistentTypes as persistentType>
        StateTypeStateStoreMap.stateTypeToStoreName(${persistentType}::class.java, ${persistentType}::class.java.getSimpleName())
      </#list>

      val store: StateStore =
      StoreActorBuilder.from(stage, Model.${model}, Arrays.asList(dispatchers), StorageType.STATE_STORE, Settings.properties(), true)

      <#if requireAdapters>
          <#list adapters as stateAdapter>
            registry.register(Info(store, ${stateAdapter.sourceClass}::class.java, ${stateAdapter.sourceClass}::class.java.getSimpleName()))
          </#list>
      </#if>

      instance = ${storeProviderName}(stage, store)

      return instance
    }
  }

  public val store: StateStore
  <#list queries as query>
  public val ${query.attributeName}: ${query.protocolName}
  </#list>

  @SuppressWarnings({ "unchecked", "rawtypes" })
  constructor(stage: Stage, store: StateStore) {
    this.store = store
    <#list queries as query>
    this.${query.attributeName} = stage.actorFor(${query.protocolName}::class.java, ${query.actorName}::class.java, store)
    </#list>
  }
}
