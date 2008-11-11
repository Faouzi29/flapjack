package flapjack.parser;

import junit.framework.TestCase;


public class DefaultParseResultFactoryTest extends TestCase {

    public void test_build() {
        ParseResult result = new DefaultParseResultFactory().build();

        assertNotNull(result);
        assertTrue(result instanceof DefaultParseResult);
    }
}
