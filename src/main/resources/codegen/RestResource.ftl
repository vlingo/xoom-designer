package ${packageName};

import io.vlingo.actors.Stage;
import io.vlingo.http.resource.Resource;
import io.vlingo.http.resource.ResourceHandler;
import static io.vlingo.http.resource.ResourceBuilder.resource;

public class ${resourceName} extends ResourceHandler {

    private final Stage stage;

    public ${resourceName}(final Stage stage) {
        this.stage = stage;
    }

    @Override
    public Resource<?> routes() {
        return resource("${resourceName}" /*Add Request Handlers here as a second parameter*/);
    }

}