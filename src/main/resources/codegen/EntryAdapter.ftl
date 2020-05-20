package ${packageName};

<#list imports as import>
import ${import.fullyQualifiedClassName};
</#list>

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public final class ${adapterName} implements EntryAdapter<${stateName},TextEntry> {
  @Override
  public ${stateName} fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final ${stateName} source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(${stateName}.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final ${stateName} source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, ${stateName}.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final ${stateName} source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, ${stateName}.class, 1, serialization, version, metadata);
  }
}
