package flapjack.util;

import java.util.HashMap;
import java.util.Map;


public class TypeConverter {
    private Map stringConverters = new HashMap();

    public TypeConverter() {
        registerConverter(new IntegerValueConverter());
    }

    public void registerConverter(ValueConverter stringConverter) {
        stringConverters.put(stringConverter.type(), stringConverter);
    }

    public Object convert(Class clazz, String text) throws IllegalArgumentException {
        ValueConverter converter = (ValueConverter) stringConverters.get(clazz);
        if (converter == null) {
            throw new IllegalArgumentException("No " + ValueConverter.class.getName() + " registered for type " + clazz.getName());
        }
        try {
            return converter.convert(text);
        } catch (RuntimeException err) {
            throw new IllegalArgumentException("Problem converting \""+text+"\" to "+clazz.getName(), err);
        }
    }

    public Object convert(Class clazz, String text, String text2) {
        return convert(clazz, text + text2);
    }
}
