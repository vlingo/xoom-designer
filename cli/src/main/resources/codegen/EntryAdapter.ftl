package ${packageName};

<#list imports as import>
import ${import.fullyQualifiedClassName};
</#list>

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public final class ${sourceClass}Adapter implements EntryAdapter<${sourceClass},TextEntry> {
  @Override
  public ${sourceClass} fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final ${sourceClass} source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(${sourceClass}.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final ${sourceClass} source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, ${sourceClass}.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final ${sourceClass} source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, ${sourceClass}.class, 1, serialization, version, metadata);
  }
}
