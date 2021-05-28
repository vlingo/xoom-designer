// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration;

import io.vlingo.xoom.designer.task.projectgeneration.code.java.projections.ProjectionType;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.storage.Model;
import io.vlingo.xoom.designer.task.projectgeneration.code.java.storage.StorageType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.designer.task.projectgeneration.code.java.Template.*;

public class CodeGenerationProperties {

  public static final String DATA_SCHEMA_CATEGORY = "data";
  public static final String EVENT_SCHEMA_CATEGORY = "event";
  public static final String DEFAULT_SCHEMA_VERSION = "1.0.0";

  public static final String CHAR_TYPE = "char";
  public static final List<String> DATE_TIME_TYPES = Arrays.asList("LocalDate", "LocalDateTime");
  public static final List<String> SCALAR_NUMERIC_TYPES = Arrays.asList("byte", "short", "int", "long", "double", "integer");
  public static final List<String> SCALAR_TYPES = Stream.of(SCALAR_NUMERIC_TYPES, Arrays.asList("boolean", CHAR_TYPE, "string"))
          .flatMap(List::stream).collect(Collectors.toList());

  @SuppressWarnings("serial")
  public static final Map<String, String> SPECIAL_TYPES_IMPORTS =
          Collections.unmodifiableMap(
                  new HashMap<String, String>() {{
                    put("LocalDate", "java.time.LocalDate");
                    put("LocalDateTime", "java.time.LocalDateTime");
                    put("List", "java.util.*");
                    put("Set", "java.util.*");
                    put("ArrayList", "java.util.*");
                    put("HashSet", "java.util.*");
                  }}
          );

  public static final Map<String, String> FIELD_TYPE_TRANSLATION =
          Collections.unmodifiableMap(
                  new HashMap<String, String>() {{
                    put("Date", "LocalDate");
                    put("DateTime", "LocalDateTime");
                  }}
          );
  @SuppressWarnings("serial")
  public static final Map<StorageType, String> AGGREGATE_TEMPLATES =
          Collections.unmodifiableMap(
                  new HashMap<StorageType, String>() {{
                    put(StorageType.STATE_STORE, STATEFUL_ENTITY.filename);
                    put(StorageType.JOURNAL, EVENT_SOURCE_ENTITY.filename);
                  }}
          );

  @SuppressWarnings("serial")
  public static final Map<StorageType, String> AGGREGATE_METHOD_TEMPLATES =
          Collections.unmodifiableMap(
                  new HashMap<StorageType, String>() {{
                    put(StorageType.STATE_STORE, STATEFUL_ENTITY_METHOD.filename);
                    put(StorageType.JOURNAL, EVENT_SOURCE_ENTITY_METHOD.filename);
                  }}
          );

  @SuppressWarnings("serial")
  public static final Map<StorageType, String> ADAPTER_TEMPLATES =
          Collections.unmodifiableMap(
                  new HashMap<StorageType, String>() {{
                    put(StorageType.STATE_STORE, STATE_ADAPTER.filename);
                    put(StorageType.JOURNAL, ENTRY_ADAPTER.filename);
                  }}
          );

  @SuppressWarnings("serial")
  public static final Map<ProjectionType, String> PROJECTION_TEMPLATES =
          Collections.unmodifiableMap(
                  new HashMap<ProjectionType, String>() {{
                    put(ProjectionType.EVENT_BASED, EVENT_BASED_PROJECTION.filename);
                    put(ProjectionType.OPERATION_BASED, OPERATION_BASED_PROJECTION.filename);
                  }}
          );

  @SuppressWarnings("serial")
  public static final Map<StorageType, String> COMMAND_MODEL_STORE_TEMPLATES =
          Collections.unmodifiableMap(
                  new HashMap<StorageType, String>() {{
                    put(StorageType.STATE_STORE, STATE_STORE_PROVIDER.filename);
                    put(StorageType.JOURNAL, JOURNAL_PROVIDER.filename);
                  }}
          );

  @SuppressWarnings("serial")
  public static final Map<StorageType, String> QUERY_MODEL_STORE_TEMPLATES =
          Collections.unmodifiableMap(
                  new HashMap<StorageType, String>() {{
                    put(StorageType.STATE_STORE, STATE_STORE_PROVIDER.filename);
                    put(StorageType.JOURNAL, STATE_STORE_PROVIDER.filename);
                  }}
          );

  public static Map<StorageType, String> storeProviderTemplatesFrom(final Model model) {
    if (model.isQueryModel()) {
      return QUERY_MODEL_STORE_TEMPLATES;
    }
    return COMMAND_MODEL_STORE_TEMPLATES;
  }

}
