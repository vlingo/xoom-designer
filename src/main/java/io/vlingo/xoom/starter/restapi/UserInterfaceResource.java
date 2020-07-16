// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.starter.restapi;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalNotification;
import io.vlingo.actors.Logger;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.http.*;
import io.vlingo.http.resource.*;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.Response.Status.InternalServerError;
import static io.vlingo.http.ResponseHeader.ContentLength;
import static io.vlingo.http.resource.ResourceBuilder.get;
import static io.vlingo.http.resource.ResourceBuilder.resource;

public class UserInterfaceResource extends ResourceHandler {

    private final LoadingCache<String, byte[]> staticFileCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .removalListener(this::onRemoval)
            .build(new CacheLoader<String, byte[]>() {
                @Override
                @ParametersAreNonnullByDefault
                public byte[] load(String path) throws IOException {
                    return readFileFromClasspath(path);
                }
            });

    @SuppressWarnings("unused")
    private final Stage stage;
    @SuppressWarnings("unused")
    private final Logger logger;

    public UserInterfaceResource(final Stage stage) {
        this.stage = stage;
        this.logger = stage.world().defaultLogger();
    }

    @Override
    public Resource<?> routes() {
        final RequestHandler0.Handler0 serve0 = this::serve;
        final RequestHandler1.Handler1<String> serve1 = this::serve;
        final RequestHandler2.Handler2<String, String> serve2 = this::serve;
        final RequestHandler3.Handler3<String, String, String> serve3 = this::serve;
        final RequestHandler4.Handler4<String, String, String, String> serve4 = this::serve;

        return resource("ui", 10,
                get("/")
                        .handle(this::redirectToApp),
                get("/xoom-starter/")
                        .handle(serve0),
                get("/xoom-starter/{file}")
                        .param(String.class)
                        .handle(serve1),
                get("/xoom-starter/{path1}/{file}")
                        .param(String.class)
                        .param(String.class)
                        .handle(serve2),
                get("/xoom-starter/{path1}/{path2}/{file}")
                        .param(String.class)
                        .param(String.class)
                        .param(String.class)
                        .handle(serve3),
                get("/xoom-starter/{path1}/{path2}/{path3}/{file}")
                        .param(String.class)
                        .param(String.class)
                        .param(String.class)
                        .param(String.class)
                        .handle(serve4)
        );
    }

    private Completes<Response> redirectToApp() {
        return Completes.withSuccess(
                Response.of(MovedPermanently,
                        Header.Headers.of(
                                ResponseHeader.of(ResponseHeader.ContentLength, 0),
                                ResponseHeader.of("Location", "/xoom-starter/")
                        )));
    }

    private Completes<Response> serve(final String... pathSegments) {
        if (pathSegments.length == 0 || pathSegments[pathSegments.length - 1].split("\\.").length == 1)
            return serve(Stream.concat(Stream.of(pathSegments), Stream.of("index.html")).toArray(String[]::new));

        String path = pathFrom(pathSegments);
        try {
            byte[] content;
            try {
                content = staticFileCache.get(path);
            } catch (ExecutionException ex) {
                throw ex.getCause();
            }
            return Completes.withSuccess(
                    Response.of(Ok,
                            Header.Headers.of(
                                    ResponseHeader.of(RequestHeader.ContentType, guessContentType(path)),
                                    ResponseHeader.of(ContentLength, content.length)),
                            Body.bytesToUTF8(content)
                    )
            );
        } catch (FileNotFoundException e) {
            return Completes.withSuccess(Response.of(NotFound, path + " not found."));
        } catch (Throwable e) {
            return Completes.withSuccess(Response.of(InternalServerError));
        }
    }

    private String guessContentType(final String path) throws IOException {
        // This implementation uses javax.activation.MimetypesFileTypeMap; the mime types are defined
        // in META-INF/mime.types as JDK8's java.nio.file.Files#probeContentType is highly platform dependent
        // and reportedly not reliable, see e.g. https://bugs.openjdk.java.net/browse/JDK-8186071
        MimetypesFileTypeMap m = new MimetypesFileTypeMap();
        String contentType = m.getContentType(Paths.get(path).toFile());
        return (contentType != null) ? contentType : "application/octet-stream";
    }

    private String pathFrom(final String[] pathSegments) {
        return Stream.of(pathSegments)
                .map(p -> p.startsWith("/") ? p.substring(1) : p)
                .map(p -> p.endsWith("/") ? p.substring(0, p.length() - 1) : p)
                .collect(Collectors.joining("/", "frontend/", ""));
    }

    private byte[] readFileFromClasspath(final String path) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);

        if (is == null)
            throw new FileNotFoundException();

        return read(is);
    }

    private static byte[] read(final InputStream is) throws IOException {
        byte[] readBytes;
        byte[] buffer = new byte[4096];

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int read;
            while ((read = is.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
            readBytes = baos.toByteArray();
        } finally {
            is.close();
        }
        return readBytes;
    }

    private void onRemoval(RemovalNotification<Object, Object> notification) {
        staticFileCache.invalidate(notification.getKey());
    }

}
