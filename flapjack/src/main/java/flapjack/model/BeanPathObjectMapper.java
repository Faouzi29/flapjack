/**
 * Copyright 2008-2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License. 
 */
package flapjack.model;

import flapjack.util.ClassUtil;
import flapjack.util.TypeConverter;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BeanPathObjectMapper {
    private static final String NO_FIELD_MAPPING = "Could not locate field mapping for field=\"{0}\"";
    private static final String NO_FIELD_ON_OBJECT = "Could not map {0} -> {1} on {2}, \"{1}\" could NOT be found";

    private TypeConverter typeConverter = new TypeConverter();
    private Map mappings = new HashMap();
    private boolean ignoreUnmappedFields;

    public void mapOnTo(Object parsedFields, Object domain) throws IllegalArgumentException {
        Map fields = (Map) parsedFields;
        Iterator it = fields.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            String beanPath = (String) mappings.get(key);
            if (beanPath == null) {
                if (!ignoreUnmappedFields) {
                    throw new IllegalArgumentException(MessageFormat.format(NO_FIELD_MAPPING, new String[]{key}));
                }
            } else {
                Field field = ClassUtil.findField(domain, beanPath);
                if (field == null) {
                    throw new IllegalArgumentException(MessageFormat.format(NO_FIELD_ON_OBJECT, new String[]{key, beanPath, domain.getClass().getName()}));
                }
                ClassUtil.setBean(domain, beanPath, typeConverter.convert(field.getType(), (byte[]) fields.get(key)));
            }
        }
    }

    public void registerMapping(String recordFieldName, String domainFieldName) {
        mappings.put(recordFieldName, domainFieldName);
    }

    public void setIgnoreUnmappedFields(boolean ignoreUnmappedFields) {
        this.ignoreUnmappedFields = ignoreUnmappedFields;
    }
}
