package flapjack.parser;

import flapjack.layout.FieldDefinition;
import flapjack.layout.RecordLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ByteRecordFieldParser implements RecordFieldParser {
    public Object parse(byte[] bytes, RecordLayout recordLayout) throws ParseException {
        List fields = new ArrayList();

        if (bytes.length == 0) {
            return fields;
        }

        int offset = 0;
        FieldDefinition fieldDef = null;
        Iterator it = recordLayout.getFieldDefinitions().iterator();
        try {
            while (it.hasNext()) {
                fieldDef = (FieldDefinition) it.next();
                byte temp[] = new byte[fieldDef.getLength()];
                System.arraycopy(bytes, offset, temp, 0, temp.length);
                fields.add(temp);
                offset += temp.length;
            }
        } catch (Exception err) {
            throw new ParseException(err, recordLayout, fieldDef);
        }

        return fields;
    }
}
