package ${packageName};

import io.vlingo.actors.Stage;
import io.vlingo.xoom.Endpoint;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;
import static io.vlingo.http.resource.ResourceBuilder.resource;

public class ${resourceName} extends ResourceHandlers {

    private final Stage stage;

    public ${resourceName}(final Stage stage) {
        this.stage = stage;
    }

    @Override
    public Resource<?> routes() {
        return resource("${resourceName}" /*Add Request Handlers here as a second parameter*/);
    }

}