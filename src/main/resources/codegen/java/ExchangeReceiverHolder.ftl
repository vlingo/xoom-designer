package ${packageName};

import io.vlingo.xoom.lattice.exchange.ExchangeReceiver;
import io.vlingo.xoom.lattice.grid.Grid;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

public class ${exchangeReceiverHolderName} {

<#list exchangeReceivers as receiver>
  /**
   * See <a href="https://docs.vlingo.io/xoom-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
   */
  static class ${receiver.schemaTypeName} implements ExchangeReceiver<${receiver.localTypeName}> {

    private final Grid stage;

    public ${receiver.schemaTypeName}(final Grid stage) {
      this.stage = stage;
    }

    @Override
    public void receive(final ${receiver.localTypeName} data) {
      <#list receiver.valueObjectInitializers as initializer>
      ${initializer}
      </#list>
      <#if receiver.dispatchToFactoryMethod>
      ${receiver.modelProtocol}.${receiver.modelMethod}(${receiver.modelMethodParameters});
      <#else>
      stage.actorOf(${receiver.modelProtocol}.class, stage.addressFactory().from(data.id), Definition.has(${receiver.modelActor}.class, Definition.parameters(data.id)))
              .andFinallyConsume(${receiver.modelVariable} -> ${receiver.modelVariable}.${receiver.modelMethod}(${receiver.modelMethodParameters}));
      </#if>
    }
  }

</#list>
}
