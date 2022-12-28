<#list imports as import>
using ${import.qualifiedClassName};
</#list>

namespace ${packageName};

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/exchange#exchangeadapter">ExchangeAdapter</a>
 */
public class ${exchangeAdapterName} : IExchangeAdapter<IdentifiedDomainEvent, IdentifiedDomainEvent, Message>
{

  private static String SCHEMA_PREFIX = "${schemaGroupName}";

  private ${exchangeMapperName} _mapper = new ${exchangeMapperName}();

  public IdentifiedDomainEvent FromExchange(Message exchangeMessage)
  {
    return _mapper.ExternalToLocal(exchangeMessage);
  }

  public Message ToExchange(IdentifiedDomainEvent event)
  {
    var message = _mapper.LocalToExternal(event);
    message.MessageParameters.TypeName(ResolveFullSchemaReference(event));
    return message;
  }

  public bool Supports(Object exchangeMessage)
  {
    return false;
  }

  private string ResolveFullSchemaReference(IdentifiedDomainEvent event)
  {
    var semanticVersion = SemanticVersion.ToString(event.SourceTypeVersion);
    return $"{SCHEMA_PREFIX}{typeof(event)}{semanticVersion}";
  }

}
