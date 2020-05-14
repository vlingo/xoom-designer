package ${packageName};

import java.util.Arrays;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import io.vlingo.actors.Definition;
import io.vlingo.actors.Protocols;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.StateAdapterProvider;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.state.StateStore;

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

    return ${storeProviderName}.using(stage, registry, noop);
  }
  @SuppressWarnings("rawtypes")
  public static ${storeProviderName} using(final Stage stage, final StatefulTypeRegistry registry, final Dispatcher dispatcher) {
    if (instance != null) {
      return instance;
    }

    final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
<#list stateAdapters as stateAdapter>
    stateAdapterProvider.registerAdapter(${stateAdapter.stateClass}.class, new ${stateAdapter.stateAdapterClass}());
</#list>

    new EntryAdapterProvider(stage.world()); // future use

    final Protocols storeProtocols =
            stage.actorFor(
                    new Class<?>[] { StateStore.class, DispatcherControl.class },
                    Definition.has(${storeClassName}.class, Definition.parameters(Arrays.asList(dispatcher))));

    final Protocols.Two<StateStore, DispatcherControl> storeWithControl = Protocols.two(storeProtocols);

<#list stateAdapters as stateAdapter>
    registry.register(new Info(storeWithControl._1, ${stateAdapter.stateClass}.class, ${stateAdapter.stateClass}.class.getSimpleName()));
</#list>

    instance = new ${storeProviderName}(storeWithControl._1, storeWithControl._2);

    return instance;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private ${storeProviderName}(final StateStore store, final DispatcherControl dispatcherControl) {
    this.store = store;
    this.dispatcherControl = dispatcherControl;
  }
}
