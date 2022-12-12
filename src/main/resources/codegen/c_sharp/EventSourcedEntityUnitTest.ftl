using System;
<#list imports as import>
using ${import.qualifiedClassName};
</#list>
using Vlingo.Xoom.Actors;
using Vlingo.Xoom.Lattice.Model.Sourcing;
using Vlingo.Xoom.Symbio;
using Vlingo.Xoom.Symbio.Store.Journal;
using Vlingo.Xoom.Symbio.Store.Journal.InMemory;
using Xunit;

namespace ${packageName};

public class ${entityUnitTestName}
{

  private readonly World _world;
  private readonly IJournal<string> _journal;
  private ${dispatcherName} _dispatcher;
  private ${aggregateProtocolName} _${aggregateProtocolVariable};

  public ${entityUnitTestName}()
  {
    _world = World.StartWithDefaults("test-es");

    _dispatcher = new ${dispatcherName}();

    EntryAdapterProvider entryAdapterProvider = EntryAdapterProvider.Instance(_world);

    <#list sourcedEvents as event>
    entryAdapterProvider.RegisterAdapter(new ${event.adapterName}());
    </#list>

    _journal = _world.ActorFor<IJournal<string>>(typeof(InMemoryJournalActor<string>), _dispatcher);

    new SourcedTypeRegistry(_world).Register(Info.RegisterSourced(_journal, typeof(${entityName})));

    _${aggregateProtocolVariable} = _world.ActorFor<${aggregateProtocolName}>(typeof(${entityName}), "#1");
  }

  <#list testCases as testCase>
  <#list testCase.dataDeclarations as dataDeclaration>
  ${dataDeclaration}
  </#list>

  [Fact(<#if testCase.disabled>Skip</#if>)]
  public void ${testCase.methodName}()
  {
    <#if testCase.disabled>
    /**
     * TODO: Unable to generate tests for method ${testCase.methodName}. See {@link ${entityName}#${testCase.methodName}()}
     */
    <#else>
    <#list testCase.preliminaryStatements as statement>
    ${statement}
    </#list>
    ${testCase.resultAssignmentStatement}

    <#list testCase.assertions as assertion>
    ${assertion}
    </#list>
    </#if>
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
