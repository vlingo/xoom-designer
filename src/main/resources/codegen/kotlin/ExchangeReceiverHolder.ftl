package ${packageName}

import io.vlingo.xoom.actors.Definition
import io.vlingo.xoom.actors.Stage
import io.vlingo.xoom.lattice.exchange.ExchangeReceiver

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

public class ${exchangeReceiverHolderName} {

<#list exchangeReceivers as receiver>
  /**
   * See <a href="https://docs.vlingo.io/xoom-lattice/exchange#exchangereceiver">ExchangeReceiver</a>
   */
  class ${receiver.schemaTypeName} : ExchangeReceiver<${receiver.localTypeName}> {

    val stage: Stage

    public constructor(stage: Stage) {
      this.stage = stage
    }

    public override fun receive(data: ${receiver.localTypeName}) {
      <#if receiver.modelFactoryMethod>
      ${receiver.modelProtocol}.${receiver.modelMethod}(${receiver.modelMethodParameters})
      <#else>
      stage.actorOf(${receiver.modelProtocol}::class.java, stage.addressFactory().from(data.id), Definition.has(${receiver.modelActor}::class.java, Definition.parameters(data.id)))
              .andFinallyConsume{${receiver.modelVariable} -> ${receiver.modelVariable}.${receiver.modelMethod}(${receiver.modelMethodParameters})}
      </#if>
    }
  }

</#list>
}
