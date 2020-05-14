package ${packageName};

import java.util.Arrays;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import io.vlingo.actors.Stage;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.StateAdapterProvider;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.state.StateStore;

public class QueryModelStateStoreProvider {
  private static QueryModelStateStoreProvider instance;

  public final StateStore store;

  public static QueryModelStateStoreProvider instance() {
    return instance;
  }

  @SuppressWarnings("rawtypes")
  public static QueryModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry) {
    if (instance != null) {
      return instance;
    }

    final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
<#list stateAdapters as stateAdapter>
    stateAdapterProvider.registerAdapter(${stateAdapter.stateClass}.class, new ${stateAdapter.stateAdapterClass}());
</#list>

    new EntryAdapterProvider(stage.world()); // future

    final Dispatcher noop = new Dispatcher() {
      public void controlWith(final DispatcherControl control) { }
      public void dispatch(Dispatchable d) { }
    };

    final StateStore store = stage.actorFor(StateStore.class, ${storeClassName}.class, Arrays.asList(noop));

<#list stateAdapters as stateAdapter>
    registry.register(new Info(store, ${stateAdapter.stateClass}.class, ${stateAdapter.stateClass}.class.getSimpleName()));
</#list>

    instance = new QueryModelStateStoreProvider(store);

    return instance;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private QueryModelStateStoreProvider(final StateStore store) {
    this.store = store;
  }
}
