package flapjack.parser;

import flapjack.layout.FieldDefinition;


public class StringFieldParser {

    /**
     * Parses the field out of the line based on the position and length given
     *
     * @param line            - the current record line
     * @param fieldDefinition - the field to be parsed out of the given line
     * @return the field value
     */
    public String parse(String line, FieldDefinition fieldDefinition) {
        return parse(line, fieldDefinition, false);
    }

    /**
     * Parses the field out of the line based on the position and length given
     *
     * @param line            - the current record line
     * @param fieldDefinition - the field to be parsed out of the given line
     * @param trim            - should trim the String
     * @return the field value
     */
    public String parse(String line, FieldDefinition fieldDefinition, boolean trim) {
        int position = fieldDefinition.getPosition();
        int endPosition = position + fieldDefinition.getLength();
        String value = line.substring(position, endPosition);
        if (trim)
            return value.trim();
        else
            return value;
    }
}