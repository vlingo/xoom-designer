#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;
import io.vlingo.xoom.VlingoServer;
import io.vlingo.xoom.server.VlingoScene;

public class Bootstrap {

    private static Bootstrap instance;
    private VlingoServer vlingoServer;

    public static void main(String[] args) {
        Bootstrap.boot();
    }

    public static void boot() {
        Bootstrap.instance();
    }

    public static Bootstrap instance() {
        if (instance == null) {
            instance = new Bootstrap(Micronaut.run(Bootstrap.class));
        }
        return instance;
    }

    private Bootstrap(final ApplicationContext applicationContext) {
        this.vlingoServer = applicationContext.getBean(VlingoServer.class);

        this.registerWorld(applicationContext, vlingoServer.getVlingoScene());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stopAndCleanup();
        }));
    }

    private void registerWorld(final ApplicationContext applicationContext, final VlingoScene scene) {
        applicationContext.registerSingleton(scene.getWorld());
    }

    public void stopAndCleanup() {
        instance = null;
        vlingoServer.close();
    }

}
