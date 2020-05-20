#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import io.micronaut.runtime.Micronaut;
import io.vlingo.xoom.VlingoServer;

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
            instance = new Bootstrap();
        }
        return instance;
    }

    private Bootstrap() {
        this.vlingoServer =
                Micronaut.run(Bootstrap.class)
                        .getBean(VlingoServer.class);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stopAndCleanup();
        }));
    }

    public void stopAndCleanup() {
        instance = null;
        vlingoServer.close();
    }

}
