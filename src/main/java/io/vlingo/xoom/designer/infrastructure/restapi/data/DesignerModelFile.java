// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.infrastructure.restapi.data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.designer.infrastructure.ByteArraySupplier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

public class DesignerModelFile {

  public final String encoded;

  public static DesignerModelFile from(final DesignerModel data) {
    try {
      return new DesignerModelFile(JsonSerialization.serialized(data));
    } catch (final IOException exception) {
      exception.printStackTrace();
      throw new DesignerModelFileException("Unable to create GenerationSettingsFile", exception);
    }
  }

  private DesignerModelFile(final String serializedSettingsJson) throws IOException {
    this.encoded = encode(createFile(serializedSettingsJson));
  }

  private Path createFile(final String serializedSettingsJson) throws IOException {
    final Path file = Paths.get(UUID.randomUUID() + ".json");
    Files.write(file, format(serializedSettingsJson).getBytes());
    return file;
  }

  private String encode(final Path file) throws IOException {
    return Base64.getEncoder().encodeToString(Files.readAllBytes(file));
  }

  private String format(final String generationSettings) {
    final JsonElement parsed = JsonParser.parseString(generationSettings);
    return new GsonBuilder().setPrettyPrinting().create().toJson(parsed);
  }

  public DesignerModel mapData() {
    try {
      final ByteArraySupplier byteArraySupplier = ByteArraySupplier.empty();
      try (final ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream(); ) {
        byteArraySupplier.stream(byteArrayStream);
        byteArrayStream.write(Base64.getDecoder().decode(encoded));
      }
      return JsonSerialization.deserialized(new String(byteArraySupplier.get()), DesignerModel.class);
    } catch (final Exception exception) {
      throw new DesignerModelFileException("Unable to map data from encoded file", exception);
    }
  }

}
