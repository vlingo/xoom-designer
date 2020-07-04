package ${packageName};

import io.vlingo.actors.Stage;
import io.vlingo.xoom.Endpoint;
import io.vlingo.http.resource.Resource;
import static io.vlingo.http.resource.ResourceBuilder.resource;

public class ${resourceName} extends Endpoint {

    public ${resourceName}(final Stage stage) {
        super(stage);
    }

    @Override
    public Resource<?> routes() {
        return resource("${resourceName}" /*Add Request Handlers here as a second parameter*/);
    }

}