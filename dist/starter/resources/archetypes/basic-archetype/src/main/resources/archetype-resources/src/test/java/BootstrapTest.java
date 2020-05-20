#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.junit.jupiter.api.Test;

public class BootstrapTest {

    @Test
    public void testBoot() {
        Bootstrap.main(new String[]{});
    }

}
