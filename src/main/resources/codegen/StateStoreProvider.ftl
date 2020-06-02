package ${packageName};

import java.util.Arrays;
import java.util.List;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import io.vlingo.actors.Definition;
import io.vlingo.actors.Protocols;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
<#if requireAdapters>
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.symbio.StateAdapterProvider;
</#if>
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.state.StateStore;

<#if configurable>
import io.vlingo.actors.ActorInstantiator;
import io.vlingo.symbio.store.DataFormat;
import io.vlingo.symbio.store.StorageException;
import io.vlingo.symbio.store.state.StateStore.StorageDelegate;
import io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor.JDBCStateStoreInstantiator;
import io.vlingo.symbio.store.common.jdbc.Configuration;
</#if>

public class ${storeProviderName} {
  private static ${storeProviderName} instance;

  public final DispatcherControl dispatcherControl;
  public final StateStore store;

  public static ${storeProviderName} instance() {
    return instance;
  }

  public static ${storeProviderName} using(final Stage stage, final StatefulTypeRegistry registry) {
    final Dispatcher noop = new Dispatcher() {
      public void controlWith(final DispatcherControl control) { }
      public void dispatch(Dispatchable d) { }
    };

    return using(stage, registry, noop);
  }

  @SuppressWarnings("rawtypes")
  public static ${storeProviderName} using(final Stage stage, final StatefulTypeRegistry registry, final Dispatcher dispatcher) {
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

<#if configurable>
    final ActorInstantiator jdbcInstantiator = setupJDBCInstantiator(stage, dispatcher);
    final List<Object> parameters = Definition.parameters(Arrays.asList(jdbcInstantiator));
<#else>
    final List<Object> parameters = Definition.parameters(Arrays.asList(dispatcher));
</#if>

    final Protocols storeProtocols =
            stage.actorFor(
                    new Class<?>[] { StateStore.class, DispatcherControl.class },
                    Definition.has(${storeClassName}.class, parameters));

    final Protocols.Two<StateStore, DispatcherControl> storeWithControl = Protocols.two(storeProtocols);

<#if requireAdapters>
<#list adapters as stateAdapter>
    registry.register(new Info(storeWithControl._1, ${stateAdapter.sourceClass}.class, ${stateAdapter.sourceClass}.class.getSimpleName()));
</#list>
</#if>

    instance = new ${storeProviderName}(storeWithControl._1, storeWithControl._2);

    return instance;
  }

<#if configurable>
  private static ActorInstantiator setupJDBCInstantiator(final Stage stage, final Dispatcher dispatcher) {
    final StorageDelegate storageDelegate = setupStorageDelegate(stage);
    final ActorInstantiator<?> instantiator = new JDBCStateStoreInstantiator();
    instantiator.set("dispatcher", dispatcher);
    instantiator.set("delegate", storageDelegate);
    return instantiator;
  }

  private static StorageDelegate setupStorageDelegate(final Stage stage) {
    final Configuration configuration = configDatabase();
    return new ${storageDelegateName}(configuration, stage.world().defaultLogger());
  }

  private static Configuration configDatabase() {
    try {
        return ${configurationProviderName}.configuration(
                DataFormat.Text,
                "${connectionUrl}",
                "databaseName",
                "username",
                "password",
                "originatorId",
                true
        );
    } catch (final Exception e) {
      throw new StorageException(null, "Unable to configure database", e);
    }
  }
</#if>

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private ${storeProviderName}(final StateStore store, final DispatcherControl dispatcherControl) {
    this.store = store;
    this.dispatcherControl = dispatcherControl;
  }
}
