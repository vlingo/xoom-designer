package ${packageName};

import io.vlingo.xoom.turbo.actors.Settings;
import io.vlingo.xoom.lattice.exchange.Exchange;
import io.vlingo.xoom.turbo.exchange.ExchangeSettings;
import io.vlingo.xoom.turbo.exchange.ExchangeInitializer;
import io.vlingo.xoom.lattice.exchange.rabbitmq.ExchangeFactory;
import io.vlingo.xoom.lattice.exchange.ConnectionSettings;
import io.vlingo.xoom.lattice.exchange.rabbitmq.Message;
import io.vlingo.xoom.lattice.exchange.rabbitmq.MessageSender;
import io.vlingo.xoom.lattice.exchange.rabbitmq.InactiveBrokerExchangeException;
import io.vlingo.xoom.lattice.exchange.Covey;
import io.vlingo.xoom.lattice.grid.Grid;
<#if producerExchanges?has_content>
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
</#if>

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

public class ${exchangeBootstrapName} implements ExchangeInitializer {

  <#if producerExchanges?has_content>
  private Dispatcher<?> dispatcher;
  </#if>

  @Override
  public void init(final Grid stage) {
    final ExchangeSettings exchangeSettings =
                ExchangeSettings.loadOne(Settings.properties());

    <#list exchanges as exchange>
    final ConnectionSettings ${exchange.settingsName} = exchangeSettings.mapToConnection();

    final Exchange ${exchange.variableName} =
                ExchangeFactory.fanOutInstanceQuietly(${exchange.settingsName}, exchangeSettings.exchangeName, true);

    try {
      <#list exchange.coveys as covey>
      ${exchange.variableName}.register(Covey.of(
          new MessageSender(${exchange.variableName}.connection()),
          ${covey.receiverInstantiation},
          ${covey.adapterInstantiation},
          ${covey.localClass}.class,
          ${covey.externalClass}.class,
          Message.class));

      </#list>
    } catch (final InactiveBrokerExchangeException exception) {
      stage.world().defaultLogger().error("Unable to register covey(s) for exchange");
      stage.world().defaultLogger().warn(exception.getMessage());
    }

    </#list>
    <#if producerExchanges?has_content>this.dispatcher = new ExchangeDispatcher(${producerExchanges});</#if>

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        <#list exchanges as exchange>
        ${exchange.variableName}.close();
        </#list>

        System.out.println("\n");
        System.out.println("==================");
        System.out.println("Stopping exchange.");
        System.out.println("==================");
    }));
  }

  <#if producerExchanges?has_content>
  @Override
  public Dispatcher<?> dispatcher() {
    return dispatcher;
  }
  </#if>

}