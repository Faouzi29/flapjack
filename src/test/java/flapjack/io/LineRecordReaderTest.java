package flapjack.io;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;


public class LineRecordReaderTest extends TestCase {

    public void test_SingleLineWithoutLineEndings() throws IOException {
        LineRecordReader reader = new LineRecordReader(new ByteArrayInputStream("line1".getBytes()));

        assertEquals("line1", new String(reader.readRecord()));
        assertNull(reader.readRecord());
    }

    public void test_SingleLineWithCRLF() throws IOException {
        LineRecordReader reader = new LineRecordReader(new ByteArrayInputStream("line1\r\n".getBytes()));

        assertEquals("line1", new String(reader.readRecord()));
    }

    public void test_SingleLineWithLF() throws IOException {
        LineRecordReader reader = new LineRecordReader(new ByteArrayInputStream("line1\n".getBytes()));

        assertEquals("line1", new String(reader.readRecord()));
    }

    public void test_TwoLinesWithLF() throws IOException {
        LineRecordReader reader = new LineRecordReader(new ByteArrayInputStream("line1\nline2".getBytes()));

        assertEquals("line1", new String(reader.readRecord()));
        assertEquals("line2", new String(reader.readRecord()));
    }

    public void test_TwoLinesWithCRLF() throws IOException {
        LineRecordReader reader = new LineRecordReader(new ByteArrayInputStream("line1\r\nline2".getBytes()));

        assertEquals("line1", new String(reader.readRecord()));
        assertEquals("line2", new String(reader.readRecord()));
    }
}
