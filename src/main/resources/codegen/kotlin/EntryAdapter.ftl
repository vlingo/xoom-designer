package ${packageName}

<#list imports as import>
import ${import.qualifiedClassName}
</#list>

import io.vlingo.xoom.common.serialization.JsonSerialization
import io.vlingo.xoom.symbio.BaseEntry.TextEntry
import io.vlingo.xoom.symbio.EntryAdapter
import io.vlingo.xoom.symbio.Metadata

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#entryadapter-and-entryadapterprovider">
 *   EntryAdapter and EntryAdapterProvider
 * </a>
 */
public class ${adapterName} : EntryAdapter<${sourceName}, TextEntry> {

  public override fun fromEntry(entry: TextEntry): ${sourceName} {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed())
  }

  public override fun toEntry(source: ${sourceName}, metadata: Metadata): TextEntry {
    val serialization = JsonSerialization.serialized(source)
    return TextEntry(${sourceName}::class.java, 1, serialization, metadata)
  }

  public override fun toEntry(source: ${sourceName}, id: String, metadata: Metadata): TextEntry  {
    val serialization = JsonSerialization.serialized(source)
    return TextEntry(id, ${sourceName}::class.java, 1, serialization, metadata)
  }

  public override fun toEntry(source: ${sourceName}, version: Int, id: String, metadata: Metadata): TextEntry  {
    val serialization = JsonSerialization.serialized(source)
    return TextEntry(id, ${sourceName}::class.java, 1, serialization, version, metadata)
  }
}
