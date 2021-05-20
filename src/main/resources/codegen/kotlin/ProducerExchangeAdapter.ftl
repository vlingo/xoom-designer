package ${packageName}

import io.vlingo.xoom.common.version.SemanticVersion
import io.vlingo.xoom.lattice.exchange.ExchangeAdapter
import io.vlingo.xoom.lattice.exchange.rabbitmq.Message

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/exchange#exchangeadapter">ExchangeAdapter</a>
 */
public class ${exchangeAdapterName} : ExchangeAdapter<IdentifiedDomainEvent, IdentifiedDomainEvent, Message> {

  companion object {
    val SCHEMA_PREFIX = "${schemaGroupName}"
  }

  val ${exchangeMapperName} mapper = ${exchangeMapperName}()

  public override fun fromExchange(exchangeMessage: Message): IdentifiedDomainEvent {
    return mapper.externalToLocal(exchangeMessage)
  }

  public override fun toExchange(event: IdentifiedDomainEvent): Message {
    val message = mapper.localToExternal(event)
    message.messageParameters.typeName(resolveFullSchemaReference(event))
    return message
  }

  public override fun supports(exchangeMessage: Object): boolean {
    if(!exchangeMessage.javaClass.equals(Message::class.java)) {
      return false
    }
    val schemaName = (exchangeMessage as Message).messageParameters.typeName()
    return schemaName.startsWith(SCHEMA_PREFIX)
  }

  fun resolveFullSchemaReference(event: IdentifiedDomainEvent): String {
    val semanticVersion = SemanticVersion.toString(event.sourceTypeVersion)
    return String.format("%s:%s:%s", SCHEMA_PREFIX, event.javaClass.getSimpleName(), semanticVersion)
  }

}
