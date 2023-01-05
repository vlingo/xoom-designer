package io.vlingo.xoom.designer.codegen.csharp.storage;

import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;

public enum StorageType {

  NONE(),

  STATE_STORE("STATE_STORE", "StateStore", "StatefulTypeRegistry",
      CsharpTemplateStandard.AGGREGATE_STATE);

  public final String key;
  public final String title;
  public final String typeRegistryClassName;
  public final TemplateStandard adapterSourceClassStandard;

  StorageType() {
    this(null, null, null, null);
  }

  StorageType(final String key, final String title, final String typeRegistryClassName,
              final TemplateStandard adapterSourceClassStandard) {
    this.key = key;
    this.title = title;
    this.typeRegistryClassName = typeRegistryClassName;
    this.adapterSourceClassStandard = adapterSourceClassStandard;
  }

  public static StorageType of(final String storage) {
    return valueOf(storage.toUpperCase());
  }

  public boolean isEnabled() {
    return !equals(NONE);
  }

}
