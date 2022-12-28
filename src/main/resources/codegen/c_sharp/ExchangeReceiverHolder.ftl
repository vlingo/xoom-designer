<#list imports as import>
using ${import.qualifiedClassName};
</#list>

namespace ${packageName};

public class ${exchangeReceiverHolderName}
{

<#list exchangeReceivers as receiver>
  /**
   * See <a href="https://docs.vlingo.io/xoom-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
   */
  static class ${receiver.innerClassName} : IExchangeReceiver<${receiver.localTypeName}> {

    private Grid _stage;

    public ${receiver.innerClassName}(Grid stage)
    {
      _stage = stage;
    }

    public void Receive(${receiver.localTypeName} event)
    {
      <#if receiver.dispatchToFactoryMethod>
      ${receiver.modelProtocol}.${receiver.modelMethod}(${receiver.modelMethodParameters});
      <#else>
        _stage.ActorOf<${receiver.modelProtocol}>(_stage.AddressFactory().From(event.Id), Definition.Has(typeof(${receiver.modelActor}), Definition.Parameters(event.Id)))
              .AndFinallyConsume(${receiver.modelVariable} => ${receiver.modelVariable}.${receiver.modelMethod}(${receiver.modelMethodParameters}));
      </#if>
    }
  }

</#list>
}
