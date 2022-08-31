package io.vlingo.xoom.designer.codegen.csharp.storage;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import io.vlingo.xoom.designer.codegen.csharp.CsharpTemplateStandard;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum StorageType {

  NONE(),

  STATE_STORE("STATE_STORE", "StateStore", "StatefulTypeRegistry","",
      CsharpTemplateStandard.AGGREGATE_STATE),

  JOURNAL("JOURNAL", "Journal", "SourcedTypeRegistry", "",
      CsharpTemplateStandard.DOMAIN_EVENT);
  private static final String STORE_PROVIDER_NAME_SUFFIX = "Provider";

  public final String key;
  public final String title;
  public final String typeRegistryClassName;
  private final String typeRegistryPackage;
  public final TemplateStandard adapterSourceClassStandard;

  StorageType() {
    this(null, null, null, null, null);
  }

  StorageType(final String key, final String title, final String typeRegistryClassName,
              final String typeRegistryPackage, final TemplateStandard adapterSourceClassStandard) {
    this.key = key;
    this.title = title;
    this.typeRegistryClassName = typeRegistryClassName;
    this.typeRegistryPackage = typeRegistryPackage;
    this.adapterSourceClassStandard = adapterSourceClassStandard;
  }

  public static StorageType of(final String storage) {
    if(storage.isEmpty())
      return NONE;
    return valueOf(storage.toUpperCase());
  }

  public String resolveProviderNameFrom(final Model model) {
    final String prefix = model.isDomainModel() ? title : model.title + title;
    return prefix + STORE_PROVIDER_NAME_SUFFIX;
  }

  public Boolean requireAdapters(final Model model) {
    return !model.isQueryModel();
  }

  public Set<String> resolveAdaptersQualifiedName(final Model model, final List<Content> contents) {
    if (requireAdapters(model)) {
      return ContentQuery.findFullyQualifiedClassNames(adapterSourceClassStandard, contents);
    }
    return Collections.emptySet();
  }

  public Set<String> findPersistentQualifiedTypes(final Model model, final List<Content> contents) {
    if (model.isQueryModel() || isStateful()) {
      final TemplateStandard typeStandard = model.isQueryModel() ? CsharpTemplateStandard.DATA_OBJECT : CsharpTemplateStandard.AGGREGATE_STATE;
      return ContentQuery.findFullyQualifiedClassNames(typeStandard, contents);
    }
    return new HashSet<>();
  }

  public boolean isEnabled() {
    return !equals(NONE);
  }

  public Boolean isStateful() {
    return equals(STATE_STORE);
  }

  public boolean isSourced() {
    return equals(JOURNAL);
  }

}
