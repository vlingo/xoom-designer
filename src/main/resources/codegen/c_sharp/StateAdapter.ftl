using System;
<#list imports as import>
using ${import.qualifiedClassName};
</#list>
using Vlingo.Xoom.Common.Serialization;
using Vlingo.Xoom.Symbio;

namespace ${packageName};

/**
 * See
 * <a href="https://docs.vlingo.io/xoom-lattice/entity-cqrs#stateadapter-and-stateadapterprovider">
 *   StateAdapter and StateAdapterProvider
 * </a>
 */
public class ${adapterName} : StateAdapter<${sourceName}, TextState>
{

  public override int TypeVersion => 1;

  public override ${sourceName} FromRawState(TextState raw) => (${sourceName}) JsonSerialization.Deserialized(raw.Data, raw.Typed)!;

  public override ST FromRawState<ST>(TextState raw) => JsonSerialization.Deserialized<ST>(raw.Data);

  public override TextState ToRawState(string id, ${sourceName} state, int stateVersion, Metadata metadata) {
    var serialization = JsonSerialization.Serialized(state);
    return new TextState(id, typeof(${sourceName}), TypeVersion, serialization, stateVersion, metadata);
  }
}
