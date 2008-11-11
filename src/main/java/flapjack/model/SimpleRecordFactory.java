package flapjack.model;

import flapjack.layout.RecordLayout;

import java.util.List;


public class SimpleRecordFactory implements RecordFactory {

    public Object build(Object obj, RecordLayout recordLayout) {
        List fields = (List) obj;
        if (fields == null || fields.size() == 0) {
            return null;
        }
        return new SimpleRecord(fields);
    }
}
