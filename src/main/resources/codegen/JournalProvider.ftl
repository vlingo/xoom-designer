package ${packageName};

import java.util.List;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry.Info;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.journal.Journal;

<#if configurable>
import io.vlingo.symbio.store.DataFormat;
import io.vlingo.symbio.store.StorageException;
import io.vlingo.symbio.store.common.jdbc.Configuration;
</#if>

public class ${storeProviderName}  {
  private static ${storeProviderName} instance;

  public final Journal<String> journal;

  public static ${storeProviderName}  instance() {
    return instance;
  }

  public static ${storeProviderName} using(final Stage stage, final SourcedTypeRegistry registry) {
    final Dispatcher noop = new Dispatcher() {
      public void controlWith(final DispatcherControl control) { }
      public void dispatch(Dispatchable d) { }
    };

    return using(stage, registry, noop);
 }


  @SuppressWarnings({ "unchecked", "unused" })
  public static ${storeProviderName} using(final Stage stage, final SourcedTypeRegistry registry, final Dispatcher dispatcher) {
    if (instance != null) {
      return instance;
    }

    final EntryAdapterProvider entryAdapterProvider = EntryAdapterProvider.instance(stage.world());

<#list adapters as entryAdapter>
    entryAdapterProvider.registerAdapter(${entryAdapter.sourceClass}.class, new ${entryAdapter.adapterClass}());
</#list>

<#if configurable>
    final List<Object> parameters = Definition.parameters(dispatcher, configDatabase());
<#else>
    final List<Object> parameters = Definition.parameters(dispatcher);
</#if>

    final Journal<String> journal = stage.world().actorFor(Journal.class, ${storeClassName}.class, dispatcher);

<#list adapters as entryAdapter>
    registry.register(new Info(journal, ${entryAdapter.sourceClass}.class, ${entryAdapter.sourceClass}.class.getSimpleName()));
</#list>

    instance = new ${storeProviderName}(journal);

    return instance;
  }

  private ${storeProviderName}(final Journal<String> journal) {
    this.journal = journal;
  }

<#if configurable>
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
}
