package io.vlingo.xoom.starter.task.gui;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalNotification;
import io.vlingo.common.Completes;
import io.vlingo.http.Body;
import io.vlingo.http.Header;
import io.vlingo.http.Response;
import io.vlingo.http.ResponseHeader;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.http.resource.RequestHandler0;
import io.vlingo.http.resource.RequestHandler1;
import io.vlingo.xoom.resource.Endpoint;
import io.vlingo.xoom.resource.annotations.Resource;

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

import static io.vlingo.http.RequestHeader.ContentType;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.ResponseHeader.ContentLength;
import static io.vlingo.http.resource.ResourceBuilder.get;

@Resource
public class GraphicalUserInterfaceResourceHandler implements Endpoint {

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

    @Override
    public RequestHandler[] getHandlers() {
        final RequestHandler0.Handler0 serve0 = this::serve;
        final RequestHandler1.Handler1<String> serve1 = this::serve;
        return new RequestHandler[]{
                get("").handle(() -> redirectToApp("/")),
                get("/").handle(() -> redirectToApp("/xoom-starter")),
                get("/xoom-starter").handle(serve0),
                get("/xoom-starter/settings/context").handle(() -> redirectToApp("/xoom-starter")),
                get("/xoom-starter/settings/model").handle(() -> redirectToApp("/xoom-starter")),
                get("/xoom-starter/settings/deployment").handle(() -> redirectToApp("/xoom-starter")),
                get("/xoom-starter/settings/generation").handle(() -> redirectToApp("/xoom-starter")),
                get("/xoom-starter/").handle(() -> redirectToApp("/xoom-starter")),
                get("/xoom-starter/{file}").param(String.class).handle(serve1),
        };
    }

    private Completes<Response> redirectToApp(String path) {
        return Completes.withSuccess(
                Response.of(MovedPermanently, Header.Headers.of(
                        ResponseHeader.of("Location", path),
                        ResponseHeader.of(ContentLength, 0)), ""));
    }

    private Completes<Response> serve(final String... pathSegments) {
        try {
            try {
                if (pathSegments.length == 0 || pathSegments[pathSegments.length - 1].split("\\.").length == 1) {
                    return serve(Stream.concat(Stream.of(pathSegments), Stream.of("index.html")).toArray(String[]::new));
                }
                final String path = pathFrom(pathSegments);
                final byte[] content = staticFileCache.get(path);
                return Completes.withSuccess(
                        Response.of(Ok,
                                Header.Headers.of(
                                        ResponseHeader.of(ContentType, guessContentType(path)),
                                        ResponseHeader.of(ContentLength, content.length)),
                                Body.bytesToUTF8(content)
                        )
                );
            } catch (ExecutionException e) {
                throw e.getCause();
            }
        } catch (FileNotFoundException e) {
            return Completes.withSuccess(Response.of(NotFound, e.getMessage() + " not found."));
        } catch (Throwable e) {
            return Completes.withSuccess(Response.of(InternalServerError));
        }
    }

    private String guessContentType(final String path) {
        MimetypesFileTypeMap m = new MimetypesFileTypeMap();
        String contentType = m.getContentType(Paths.get(path).toFile());
        return (contentType != null) ? contentType : "application/octet-stream";
    }

    private String pathFrom(final String[] pathSegments) {
        return Stream.of(pathSegments)
                .map(p -> p.startsWith("/") ? p.substring(1) : p)
                .map(p -> p.endsWith("/") ? p.substring(0, p.length() - 1) : p)
                .collect(Collectors.joining("/", "xoom-starter/", ""));
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
        }
        return readBytes;
    }

    private void onRemoval(RemovalNotification<Object, Object> notification) {
        staticFileCache.invalidate(notification.getKey());
    }

    @Override
    public String getName() {
        return "xoom-starter";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

}
