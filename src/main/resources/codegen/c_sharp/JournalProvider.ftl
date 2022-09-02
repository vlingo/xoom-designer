<#if imports?has_content>
  <#list imports as import>
using ${import.qualifiedClassName};
  </#list>
</#if>
using Vlingo.Xoom.Actors;
using Vlingo.Xoom.Lattice.Model.Sourcing;
using Vlingo.Xoom.Symbio;
using Vlingo.Xoom.Symbio.Store.Dispatch;
using Vlingo.Xoom.Symbio.Store.Journal;
using Vlingo.Xoom.Turbo;
using Vlingo.Xoom.Turbo.Actors;
using Vlingo.Xoom.Turbo.Annotation.Codegen.Storage;
using Vlingo.Xoom.Turbo.Storage;
using IDispatcher = Vlingo.Xoom.Symbio.Store.Dispatch.IDispatcher;

namespace ${packageName};

public class ${storeProviderName}
{

  public IJournal<string> Journal { get; }

  public static ${storeProviderName} Using(Stage stage, SourcedTypeRegistry registry)
  {
    return Using(stage, registry, new NoOpDispatcher());
  }

  public static ${storeProviderName} Using(Stage stage, SourcedTypeRegistry registry, IDispatcher dispatcher)
  {
    if (ComponentRegistry.Has<${storeProviderName}>())
    {
      return ComponentRegistry.WithType<${storeProviderName}>();
    }

    var entryAdapterProvider = EntryAdapterProvider.Instance(stage.World);

<#list adapters as entryAdapter>
    entryAdapterProvider.RegisterAdapter(new ${entryAdapter.adapterClass}());
</#list>

    var journal = StoreActorBuilder.From(stage, new Vlingo.Xoom.Turbo.Storage.Model("${model}"), dispatcher, StorageType.Journal, Settings.Properties, true);

<#list aggregates as aggregate>
    registry.Register(new Info(journal, typeof(${aggregate}), nameof(${aggregate})));
</#list>

    return new ${storeProviderName}(journal);
  }

  private ${storeProviderName}(IJournal<string> journal)
  {
    Journal = journal;
    ComponentRegistry.Register<${storeProviderName}>(this);
  }

}
