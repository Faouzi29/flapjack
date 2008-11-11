package flapjack.parser;

import flapjack.io.RecordReaderFactory;

import java.io.File;
import java.io.IOException;

public class FlatFileParser {
    private RecordReaderFactory recordReaderFactory;
    private RecordParser recordParser;

    public FlatFileParser() {
        setRecordParser(new RecordParserImpl());
    }

    public ParseResult parse(File file) throws IOException {
        return recordParser.parse(recordReaderFactory.build(file));
    }

    public void setRecordReaderFactory(RecordReaderFactory recordReader) {
        this.recordReaderFactory = recordReader;
    }

    public void setRecordParser(RecordParser recordParser) {
        this.recordParser = recordParser;
    }
}
