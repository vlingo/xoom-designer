<#if imports?has_content>
  <#list imports as import>
using ${import.qualifiedClassName};
  </#list>
</#if>
using Vlingo.Xoom.Actors;
using Vlingo.Xoom.Lattice.Model.Stateful;
using Vlingo.Xoom.Symbio;
using Vlingo.Xoom.Symbio.Store.Dispatch;
using Vlingo.Xoom.Symbio.Store.State;
using Vlingo.Xoom.Turbo;
using Vlingo.Xoom.Turbo.Actors;
using Vlingo.Xoom.Turbo.Annotation.Codegen.Storage;
using Vlingo.Xoom.Turbo.Storage;
using IDispatcher = Vlingo.Xoom.Symbio.Store.Dispatch.IDispatcher;

namespace ${packageName};

public class ${storeProviderName}
{

  public IStateStore Store { get; }
  <#list queries as query>
  public ${query.protocolName} ${query.attributeName} {get;}
  </#list>

  public static ${storeProviderName} Using(Stage stage, StatefulTypeRegistry registry)
  {
    return Using(stage, registry, new NoOpDispatcher());
  }

  public static ${storeProviderName} Using(Stage stage, StatefulTypeRegistry registry, IDispatcher dispatcher)
  {
    if (ComponentRegistry.Has<${storeProviderName}>())
    {
      return ComponentRegistry.WithType<${storeProviderName}>();
    }

<#if requireAdapters>
    var stateAdapterProvider = new StateAdapterProvider(stage.World);
<#list adapters as stateAdapter>
    stateAdapterProvider.RegisterAdapter(new ${stateAdapter.adapterClass}());
</#list>

</#if>
    new EntryAdapterProvider(stage.World); // future use

<#list persistentTypes as persistentType>
    StateTypeStateStoreMap.StateTypeToStoreName(nameof(${persistentType}), typeof(${persistentType}));
</#list>

    var store = StoreActorBuilder.From<IStateStore>(stage, new Vlingo.Xoom.Turbo.Storage.Model("${model}"), dispatcher, StorageType.StateStore, Settings.Properties, true);
<#if requireAdapters>
<#list adapters as stateAdapter>
    registry.Register(new Info(store, typeof(${stateAdapter.sourceClass}), nameof(${stateAdapter.sourceClass})));
</#list>
</#if>

    return new ${storeProviderName}(stage, store);
  }

  private ${storeProviderName}(Stage stage, IStateStore store)
  {
    Store = store;
    <#list queries as query>
    ${query.attributeName} = stage.ActorFor<${query.protocolName}>(typeof(${query.actorName}), store);
    </#list>
    ComponentRegistry.Register<${storeProviderName}>(this);
  }
}
