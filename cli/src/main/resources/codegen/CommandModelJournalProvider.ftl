package ${packageName};

<#list imports as import>
import ${import.fullyQualifiedClassName};
</#list>

import io.vlingo.actors.World;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.DefaultTextEntryAdapter;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.State.TextState;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.journal.Journal;

public class CommandModelJournalProvider {
  private static CommandModelJournalProvider instance;

  public final Journal<String> journal;

  public static CommandModelJournalProvider instance() {
    return instance;
  }

  @SuppressWarnings({ "unchecked", "unused" })
  public static void initialize(final World world, final SchemataConfig config, final Dispatcher dispatcher) throws Exception {
    final EntryAdapterProvider entryAdapterProvider = EntryAdapterProvider.instance(world);
<#list entryAdapters as entryAdapter>
    entryAdapterProvider.registerAdapter(${entryAdapter.sourceClass}.class, new ${entryAdapter.entryAdapterClass}());
</#list>

    final Journal<String> journal = world.actorFor(Journal.class, ${journalStoreClassName}.class, dispatcher);
    
    instance = new CommandModelJournalProvider(journal);
  }

  private CommandModelJournalProvider(final Journal<String> journal) {
    this.journal = journal;
  }
}
