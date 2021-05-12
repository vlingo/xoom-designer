// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.designer.task.projectgeneration.restapi;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.*;
import io.vlingo.xoom.http.resource.DynamicResourceHandler;
import io.vlingo.xoom.http.resource.Resource;
import io.vlingo.xoom.http.resource.StaticFilesResource;
import org.apache.commons.io.IOUtils;

import javax.activation.MimetypesFileTypeMap;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;

import static io.vlingo.xoom.http.Response.Status.*;
import static io.vlingo.xoom.http.ResponseHeader.ContentLength;
import static io.vlingo.xoom.http.resource.ResourceBuilder.get;
import static io.vlingo.xoom.http.resource.ResourceBuilder.resource;

public class NativeStaticFilesResource extends DynamicResourceHandler {
  private String rootPath;

  public NativeStaticFilesResource(final Stage stage) {
    super(stage);
  }

  public Completes<Response> index(String path) {
    try {
      ScriptEngine graaljsEngine = new ScriptEngineManager().getEngineByName("graal.js");

      final byte[] fileContent = readFile(path);
      graaljsEngine.eval(fileContent.toString());

      return Completes.withSuccess(Response.of(Ok, Header.Headers.of(
          ResponseHeader.of(RequestHeader.ContentType, guessContentType(path)),
          ResponseHeader.of(ContentLength, fileContent.length)),
          Body.from(fileContent, Body.Encoding.UTF8).content()));
    } catch (IOException | ScriptException e) {
      e.printStackTrace();
      return Completes.withSuccess(internalServerError(e));
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return Completes.withSuccess(notFound());
    }
  }

  public void serveFile(final String contentFile, final String root, final String validSubPaths) {
    if (rootPath == null) {
      final String initialSlash = root.startsWith("/") ? "" : "/";
      rootPath = initialSlash + (root.endsWith("/") ? root.substring(0, root.length() - 1) : root);
    }

    final String uri = contentFile.isEmpty() ? "/index.html" : context().request.uri.toString();

    Response response = Arrays.asList(
        rootPath + uri,
        withIndexHtmlAppended(rootPath + uri)
    ).stream()
        .map(this::cleanPath)
        .filter(this::isFile)
        .findFirst()
        .map(this::fileResponse)
        .orElseGet(this::notFound);

    completes().with(response);
  }

  private Response fileResponse(String path) {
    try {
      final byte[] fileContent = readFile(path);
      return Response.of(
          Ok,
          Header.Headers.of(
              ResponseHeader.of(RequestHeader.ContentType, guessContentType(path)),
              ResponseHeader.of(ContentLength, fileContent.length)),
          Body.from(fileContent, Body.Encoding.UTF8).content());
    } catch (IOException e) {
      return internalServerError(e);
    } catch (IllegalArgumentException e) {
      return notFound();
    }
  }

  private boolean isFile(String path) {
    try {
      URL res = StaticFilesResource.class.getResource(path);
      if (res == null) {
        return false;
      }

      if (!res.getProtocol().equals("jar")) {
        return new File(res.toURI()).isFile();
      }

      //jar:file:/C:/.../some.jar!/...
      try (InputStream in = getClass().getResourceAsStream(path)) {
        byte[] bytes = new byte[2];
        //read a char: if it's a directory, input.read returns -1
        if (in.read(bytes) == -1) {
          return false;
        }
      }

      return true;
    } catch (Throwable e) {
      return false;
    }
  }

  private String cleanPath(String path) {
    return String.join(" ", path.split("%20"));
  }

  private Response internalServerError(Exception e) {
    logger().error("Internal server error because: " + e.getMessage(), e);
    return Response.of(InternalServerError);
  }

  private Response notFound() {
    return Response.of(NotFound);
  }

  private String withIndexHtmlAppended(final String path) {
    final StringBuilder builder = new StringBuilder(path);

    if (!path.endsWith("/")) {
      builder.append("/");
    }

    builder.append("index.html");

    return builder.toString();
  }

  private byte[] readFile(final String path) throws IOException {
    System.err.println(
        StaticFilesResource.class.getResource(path)
    );
    final InputStream contentStream = StaticFilesResource.class.getResourceAsStream(path);
    if (contentStream != null && contentStream.available() > 0) {
      return IOUtils.toByteArray(contentStream);
    }
    throw new IllegalArgumentException("File not found.");
  }

  private String guessContentType(final String path) {
    final String contentType =
        new MimetypesFileTypeMap().getContentType(Paths.get(path).toFile());
    return (contentType != null) ? contentType : "application/octet-stream";
  }

  @Override
  public Resource<?> routes() {
    return resource("Native Static Files Resource",
        get("/app/{path}").param(String.class)
            .handle(this::index));
  }

}
