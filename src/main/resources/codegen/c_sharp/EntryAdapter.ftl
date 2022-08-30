using System;
<#list imports as import>
using ${import.qualifiedClassName};
</#list>
using Vlingo.Xoom.Common.Serialization;
using Vlingo.Xoom.Symbio;

namespace ${packageName};

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#entryadapter-and-entryadapterprovider">
 *   EntryAdapter and EntryAdapterProvider
 * </a>
 */
public class ${adapterName} : EntryAdapter<${sourceName}, TextEntry>
{

  public ${sourceName} FromEntry(TextEntry entry) => JsonSerialization.Deserialized(entry.EntryData, entry.Typed);

  public TextEntry ToEntry(${sourceName} source, Metadata metadata)
  {
    var serialization = JsonSerialization.Serialized(source);
    return new TextEntry<${sourceName}>(1, serialization, metadata);
  }

  public TextEntry ToEntry(${sourceName} source, string id, Metadata metadata)
  {
    var serialization = JsonSerialization.Serialized(source);
    return new TextEntry<${sourceName}>(id, 1, serialization, metadata);
  }

  public TextEntry ToEntry(${sourceName} source, int version, string id, Metadata metadata)
  {
    var serialization = JsonSerialization.Serialized(source);
    return new TextEntry<${sourceName}>(id, 1, serialization, version, metadata);
  }
}
