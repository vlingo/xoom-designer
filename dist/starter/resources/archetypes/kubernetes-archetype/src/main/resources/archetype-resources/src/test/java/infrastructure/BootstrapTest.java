#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infrastructure;

import org.junit.jupiter.api.Test;

public class BootstrapTest {

    @Test
    public void testBoot() throws Exception {
        Bootstrap.main(new String[]{});
    }

}
