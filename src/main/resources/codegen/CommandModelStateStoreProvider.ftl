package ${packageName};

import java.util.Arrays;

<#list imports as import>
import ${import.fullyQualifiedClassName};
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
import io.vlingo.symbio.store.state.StateStore;
import ${stateStoreClassName};

public class CommandModelStateStoreProvider {
  private static CommandModelStateStoreProvider instance;

  public final DispatcherControl dispatcherControl;
  public final StateStore store;

  public static CommandModelStateStoreProvider instance() {
    return instance;
  }

  @SuppressWarnings("rawtypes")
  public static CommandModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry, final Dispatcher dispatcher) {
    if (instance != null) {
      return instance;
    }

    final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
<#list stateAdapters as stateAdapter>
    stateAdapterProvider.registerAdapter(${stateAdapter.stateClass}.class, new ${stateAdapter.stateAdapterClass}());
</#list>

    new EntryAdapterProvider(stage.world()); // future

    final Protocols storeProtocols =
            stage.actorFor(
                    new Class<?>[] { StateStore.class, DispatcherControl.class },
                    Definition.has(${stateStoreClassName}.class, Definition.parameters(Arrays.asList(dispatcher))));

    final Protocols.Two<StateStore, DispatcherControl> storeWithControl = Protocols.two(storeProtocols);

    instance = new CommandModelStoreProvider(registry, storeWithControl._1, storeWithControl._2);

    return instance;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private CommandModelStateStoreProvider(final StatefulTypeRegistry registry, final StateStore store, final DispatcherControl dispatcherControl) {
    this.store = store;
    this.dispatcherControl = dispatcherControl;

<#list stateAdapters as stateAdapter>
    registry.register(new Info(store, ${stateAdapter.stateClass}.class, ${stateAdapter.stateClass}.class.getSimpleName()));
</#list>
  }
}
