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
public class ${adapterName} : EntryAdapter
{
  public override ISource FromEntry(IEntry entry) => JsonSerialization.Deserialized<${sourceName}>(entry.EntryRawData);

  public override IEntry ToEntry(ISource source, Metadata metadata) =>
  new ObjectEntry<${sourceName}>(typeof(${sourceName}), 1, (${sourceName}) source, metadata);

  public override IEntry ToEntry(ISource source, int version, Metadata metadata)
  => new ObjectEntry<${sourceName}>(typeof(${sourceName}), 1, (${sourceName}) source, version, metadata);

  public override IEntry ToEntry(ISource source, int version, string id, Metadata metadata)
  => new ObjectEntry<${sourceName}>(id, typeof(${sourceName}), 1, (${sourceName}) source, version, metadata);

  public override Type SourceType { get; }= typeof(${sourceName});
}
