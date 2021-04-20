// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.designer.task.projectgeneration;

import java.io.*;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ProjectCompressor {

  private static final int BUFFER_SIZE = 4096;

  public static String compress(final String projectPath) throws IOException {
    final File projectDirectory = new File(projectPath);
    final ByteArraySupplier byteArraySupplier = ByteArraySupplier.empty();
    try (final ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
         final ZipOutputStream zipStream = new ZipOutputStream(byteArrayStream)) {
      byteArraySupplier.stream(byteArrayStream);
      compressFiles(projectDirectory, projectDirectory.getName(), zipStream);
    }
    return Base64.getEncoder().encodeToString(byteArraySupplier.get());
  }

  private static void compressFiles(final File folder,
                                    final String parentFolder,
                                    final ZipOutputStream zipStream) throws IOException {
    for (File file : folder.listFiles()) {
      if (file.isDirectory()) {
        compressFiles(file, parentFolder + "/" + file.getName(), zipStream);
        continue;
      }
      compressFile(zipStream, parentFolder, file);
    }
  }

  private static void compressFile(final ZipOutputStream zipStream,
                                   final String parentFolder,
                                   final File file) throws IOException {
    zipStream.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));
    try (final FileInputStream fileStream = new FileInputStream(file);
         final BufferedInputStream bufferedStream = new BufferedInputStream(fileStream)) {
      long bytesRead = 0;
      byte[] bytesIn = new byte[BUFFER_SIZE];
      int read = 0;
      while ((read = bufferedStream.read(bytesIn)) != -1) {
        zipStream.write(bytesIn, 0, read);
        bytesRead += read;
      }
      zipStream.closeEntry();
    }
  }

  private static class ByteArraySupplier {
    private ByteArrayOutputStream stream;

    static ByteArraySupplier empty() {
      return new ByteArraySupplier();
    }

    void stream(final ByteArrayOutputStream stream) {
      this.stream = stream;
    }

    byte[] get() {
      if(stream == null) {
        return new byte[0];
      }
      return stream.toByteArray();
    }
  }
}
