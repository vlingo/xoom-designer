package ${packageName};

import io.vlingo.xoom.common.version.SemanticVersion;
import io.vlingo.xoom.lattice.exchange.ExchangeAdapter;
import io.vlingo.xoom.lattice.exchange.rabbitmq.Message;

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/exchange#exchangeadapter">ExchangeAdapter</a>
 */
public class ${exchangeAdapterName} implements ExchangeAdapter<IdentifiedDomainEvent, IdentifiedDomainEvent, Message> {

  private static final String SCHEMA_PREFIX = "${schemaGroupName}";

  private final ${exchangeMapperName} mapper = new ${exchangeMapperName}();

  @Override
  public IdentifiedDomainEvent fromExchange(final Message exchangeMessage) {
    return mapper.externalToLocal(exchangeMessage);
  }

  @Override
  public Message toExchange(final IdentifiedDomainEvent event) {
    final Message message = mapper.localToExternal(event);
    message.messageParameters.typeName(resolveFullSchemaReference(event));
    return message;
  }

  @Override
  public boolean supports(final Object exchangeMessage) {
    return false;
  }

  private String resolveFullSchemaReference(final IdentifiedDomainEvent event) {
    final String semanticVersion = SemanticVersion.toString(event.sourceTypeVersion);
    return String.format("%s:%s:%s", SCHEMA_PREFIX, event.getClass().getSimpleName(), semanticVersion);
  }

}
