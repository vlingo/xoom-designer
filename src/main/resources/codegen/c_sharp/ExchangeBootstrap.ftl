<#if producerExchanges?has_content>
using Vlingo.Xoom.Symbio.Store.Dispatch;
</#if>

<#list imports as import>
using ${import.qualifiedClassName};
</#list>

namespace ${packageName};

public class ${exchangeBootstrapName} : ExchangeInitializer
{

  <#if producerExchanges?has_content>
  private IDispatcher _dispatcher;
  </#if>

  public void Init(Grid stage)
  {
    var exchangeSettings = ExchangeSettings.LoadOne(Settings.Properties);

    <#list exchanges as exchange>
    var ${exchange.settingsName} = exchangeSettings.MapToConnection();

    var ${exchange.variableName} = ExchangeFactory.FanOutInstanceQuietly(${exchange.settingsName}, exchangeSettings.ExchangeName, true);

    try
    {
      <#list exchange.coveys as covey>
      ${exchange.variableName}.register(Covey.Of(
          new MessageSender(${exchange.variableName}.connection()),
          ${covey.receiverInstantiation},
          ${covey.adapterInstantiation},
          typeof(${covey.localClass}),
          typeof(${covey.externalClass}),
          typeof(Message)));

      </#list>
    } catch (InactiveBrokerExchangeException exception)
    {
      Stage.World.DefaultLogger.Error("Unable to register covey(s) for exchange");
      Stage.World.DefaultLogger.Warn(exception.Message);
    }

    </#list>
    <#if producerExchanges?has_content>_dispatcher = new ExchangeDispatcher(${producerExchanges});</#if>
  }

  <#if producerExchanges?has_content>
  public IDispatcher Dispatcher => _dispatcher;
  </#if>

}