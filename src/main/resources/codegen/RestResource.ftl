package ${packageName};

import io.vlingo.http.resource.Resource;
import static io.vlingo.http.resource.ResourceBuilder.resource;

public class ${resourceName}  {

    public Resource<?> routes() {
        return resource("${resourceName}" /*Add Request Handlers here as a second parameter*/);
    }

}