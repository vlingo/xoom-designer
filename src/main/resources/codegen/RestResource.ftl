package ${packageName};

import io.vlingo.actors.World;
import io.vlingo.http.resource.RequestHandler;
import io.vlingo.xoom.resource.Endpoint;
import io.vlingo.xoom.resource.annotations.Resource;
import io.vlingo.xoom.server.VlingoScene;

@Resource
public class ${resourceName} implements Endpoint {

    private final World world;

    public ${resourceName}(final VlingoScene vlingoScene) {
        this.world = vlingoScene.getWorld();
    }

    @Override
    public RequestHandler[] getHandlers() {
        return new RequestHandler[]{
        };
    }

    @Override
    public String getName() {
        return "${aggregateName}";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

}