package ${packageName};

import java.util.Arrays;
import java.util.List;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry.Info;
import io.vlingo.xoom.symbio.EntryAdapterProvider;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;
import io.vlingo.xoom.symbio.store.dispatch.Dispatchable;
import io.vlingo.xoom.symbio.store.journal.Journal;
import io.vlingo.xoom.turbo.ComponentRegistry;
import io.vlingo.xoom.turbo.actors.Settings;
import io.vlingo.xoom.turbo.storage.Model;
import io.vlingo.xoom.turbo.storage.StoreActorBuilder;
import io.vlingo.xoom.turbo.annotation.persistence.Persistence.StorageType;

@SuppressWarnings("all")
public class ${storeProviderName}  {

  public final Journal<String> journal;

  public static ${storeProviderName} using(final Stage stage, final SourcedTypeRegistry registry) {
    return using(stage, registry, new NoOpDispatcher());
 }

  public static ${storeProviderName} using(final Stage stage, final SourcedTypeRegistry registry, final Dispatcher ...dispatchers) {
    if (ComponentRegistry.has(${storeProviderName}.class)) {
      return ComponentRegistry.withType(${storeProviderName}.class);
    }

    final EntryAdapterProvider entryAdapterProvider = EntryAdapterProvider.instance(stage.world());

<#list adapters as entryAdapter>
    entryAdapterProvider.registerAdapter(${entryAdapter.sourceClass}.class, new ${entryAdapter.adapterClass}());
</#list>

    final Journal<String> journal =
              StoreActorBuilder.from(stage, Model.${model}, Arrays.asList(dispatchers), StorageType.JOURNAL, Settings.properties(), true);

<#list aggregates as aggregate>
    registry.register(new Info(journal, ${aggregate}.class, ${aggregate}.class.getSimpleName()));
</#list>

    return new ${storeProviderName}(journal);
  }

  private ${storeProviderName}(final Journal<String> journal) {
    this.journal = journal;
    ComponentRegistry.register(getClass(), this);
  }

}
