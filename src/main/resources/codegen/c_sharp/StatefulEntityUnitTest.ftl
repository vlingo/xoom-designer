using Vlingo.Xoom.Actors;
using Vlingo.Xoom.Symbio.Store.State;
using Vlingo.Xoom.Symbio;
using Vlingo.Xoom.Symbio.Store.State.InMemory;
using Vlingo.Xoom.Lattice.Model.Stateful;
<#list imports as import>
using ${import.qualifiedClassName};
</#list>
using Xunit;

namespace ${packageName};

public class ${entityUnitTestName}
{
  private readonly ${dispatcherName} _dispatcher;
  private readonly ${aggregateProtocolName} _${aggregateProtocolVariable};

  public ${entityUnitTestName}()
  {
    _dispatcher = new ${dispatcherName}();
    var world = World.StartWithDefaults("test-stateful");
    new StateAdapterProvider(world).RegisterAdapter(new ${adapterName}());
    var store = world.ActorFor<IStateStore>(typeof(InMemoryStateStoreActor<TextState>), _dispatcher);
    new StatefulTypeRegistry(world).Register(new Info(store, typeof(${stateName}), typeof(${stateName}).FullName!));
    _${aggregateProtocolVariable} = world.ActorFor<${aggregateProtocolName}>(typeof(${entityName}), "#1");
  }

  <#list testCases as testCase>
  <#list testCase.dataDeclarations as dataDeclaration>
  ${dataDeclaration}
  </#list>

  [Fact]
  public void ${testCase.methodName}()
  {
    <#list testCase.preliminaryStatements as statement>
    ${statement}
    </#list>
    ${testCase.resultAssignmentStatement}

    <#list testCase.assertions as assertion>
    ${assertion}
    </#list>
  }

  </#list>
  <#if auxiliaryEntityCreation.required>
  <#list auxiliaryEntityCreation.dataDeclarations as dataDeclaration>
  ${dataDeclaration}
  </#list>

  private void ${auxiliaryEntityCreation.methodName}()
  {
    ${auxiliaryEntityCreation.statement}
  }
  </#if>
}
