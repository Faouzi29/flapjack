package flapjack.model;

import junit.framework.TestCase;

import java.util.List;
import java.util.Arrays;

import flapjack.layout.SimpleRecordLayout;


public class SimpleRecordFactoryTest extends TestCase {

    public void test_build_WithFields() {
        SimpleRecordFactory factory = new SimpleRecordFactory();

        List fields = Arrays.asList(new String[]{"1", "2"});
        Object obj = factory.build(fields, new SimpleRecordLayout());

        assertNotNull(obj);
        assertTrue(obj instanceof SimpleRecord);

        SimpleRecord record = (SimpleRecord) obj;
        assertEquals("1", record.getField(0));
        assertEquals("2", record.getField(1));
    }

    public void test_build_WithEmptyFields() {
        SimpleRecordFactory factory = new SimpleRecordFactory();

        List fields = Arrays.asList(new String[0]);
        Object obj = factory.build(fields, new SimpleRecordLayout());

        assertNull(obj);
    }
    
    public void test_build_WithNullFields() {
        SimpleRecordFactory factory = new SimpleRecordFactory();

        Object obj = factory.build(null, new SimpleRecordLayout());

        assertNull(obj);
    }
    
}
