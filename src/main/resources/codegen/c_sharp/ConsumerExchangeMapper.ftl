<#list imports as import>
using ${import.qualifiedClassName};
</#list>
using Vlingo.Xoom.Common.Serialization;

namespace ${packageName};

/**
 * See <a href="https://docs.vlingo.io/xoom-lattice/exchange#exchangemapper">ExchangeMapper</a>
 */
public class ${exchangeMapperName} : IExchangeMapper<${localTypeName}, string>
{

  public string LocalToExternal(${localTypeName} local)
  {
    return JsonSerialization.Serialized(local);
  }

  public ${localTypeName} ExternalToLocal(string external)
  {
    return JsonSerialization.Deserialized<${localTypeName}>(external);
  }
}
