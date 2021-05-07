package ${packageName};

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.http.Body;
import io.vlingo.xoom.http.Response;
import io.vlingo.xoom.http.resource.RequestHandler0;
import io.vlingo.xoom.http.resource.RequestHandler1;
import io.vlingo.xoom.http.resource.RequestHandler2;
import io.vlingo.xoom.http.resource.RequestHandler3;
import io.vlingo.xoom.http.resource.RequestHandler4;
import io.vlingo.xoom.http.resource.Resource;
import io.vlingo.xoom.http.resource.ResourceHandler;
import io.vlingo.xoom.lattice.grid.Grid;

import javax.activation.MimetypesFileTypeMap;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;

import static io.vlingo.xoom.http.Header.Headers.of;
import static io.vlingo.xoom.http.RequestHeader.ContentType;
import static io.vlingo.xoom.http.Response.Status.InternalServerError;
import static io.vlingo.xoom.http.Response.Status.MovedPermanently;
import static io.vlingo.xoom.http.Response.Status.NotFound;
import static io.vlingo.xoom.http.Response.Status.Ok;
import static io.vlingo.xoom.http.ResponseHeader.ContentLength;
import static io.vlingo.xoom.http.ResponseHeader.of;
import static io.vlingo.xoom.http.resource.ResourceBuilder.get;
import static io.vlingo.xoom.http.resource.ResourceBuilder.resource;

public class UiResource extends ResourceHandler {

  private final MimetypesFileTypeMap mimeMap = new MimetypesFileTypeMap();

  public UiResource(final Grid grid) {
    super();
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
        get("/app/")
            .handle(serve0),
        get("/app/{file}")
            .param(String.class)
            .handle(serve1),
        get("/app/{path1}/{file}")
            .param(String.class)
            .param(String.class)
            .handle(serve2),
        get("/app/{path1}/{path2}/{file}")
            .param(String.class)
            .param(String.class)
            .param(String.class)
            .handle(serve3),
        get("/app/{path1}/{path2}/{path3}/{file}")
            .param(String.class)
            .param(String.class)
            .param(String.class)
            .param(String.class)
            .handle(serve4)
      );
  }

  private Completes<Response> redirectToApp() {
    return Completes.withSuccess(
      Response.of(
        MovedPermanently,
        of(of(ContentLength, 0), of("Location", "/app/"))
      )
    );
  }

  private Completes<Response> serve(String... pathSegments) {
    String path = Paths.get("/frontend", pathSegments).toString();
    URL res = getClass().getResource(path);
    String contentType = null;
    if (res == null || path.equals("/frontend")) {
      path = "/frontend/index.html";
      res = getClass().getResource(path);
      contentType = "text/html";
    }

    if (res == null){
      return Completes.withFailure(Response.of(NotFound));
    }

    if (contentType == null){
      contentType = guessContentType(path);
    }

    try(InputStream is = res.openStream()) {
      byte[] content = read(is); // TODO: implement caching
      return Completes.withSuccess(
        Response.of(
          Ok,
          of(of(ContentType, contentType), of(ContentLength, content.length)),
          Body.bytesToUTF8(content)
        )
      );
    } catch (Exception e) {
      logger().error("Failed to read UI Resource", e);
      return Completes.withFailure(Response.of(InternalServerError));
    }
  }

  private String guessContentType(final String path) {
    return mimeMap.getContentType(path);
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
}
