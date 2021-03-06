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

import flapjack.layout.FieldDefinition;
import flapjack.parser.FieldData;
import flapjack.util.TypeConverter;
import flapjack.util.ValueConverter;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;


public class SingleFieldMapping extends AbstractFieldMapping {
    private static final String NO_VALUE_CONVERTER = "Could not find a {0} registered! Are you sure you registered {0} in the TypeConverter?";
    private Class valueConverter;
    private DomainFieldFactory domainFieldFactory;
    private BinaryFieldFactory binaryFieldFactory;

    public SingleFieldMapping(String recordFieldName, String domainFieldName) {
        super(Arrays.asList(new String[]{recordFieldName}), domainFieldName);
    }

    public SingleFieldMapping(String recordFieldName, String domainFieldName, Class valueConverter) {
        super(Arrays.asList(new String[]{recordFieldName}), domainFieldName);
        this.valueConverter = valueConverter;
    }


    public DomainFieldFactory getDomainFieldFactory() {
        if (domainFieldFactory == null) {
            domainFieldFactory = new DomainFieldFactory() {
                public Object build(ListMap fields, Class domainFieldType, TypeConverter typeConverter) {
                    FieldData fieldData = (FieldData) fields.get(0);
                    return findValueConverter(domainFieldType, typeConverter, fieldData.definition).toDomain(fieldData.data);
                }
            };
        }
        return domainFieldFactory;
    }

    public BinaryFieldFactory getBinaryFieldFactory() {
        if (binaryFieldFactory == null) {
            binaryFieldFactory = new BinaryFieldFactory() {
                public FieldByteMap build(Object domain, TypeConverter typeConverter, List fieldDefinitions) {
                    FieldByteMap byteMap = new FieldByteMap();
                    FieldDefinition fieldDefinition = (FieldDefinition) fieldDefinitions.get(0);
                    byte[] bytes = new byte[0];
                    if (domain != null) {
                        bytes = findValueConverter(domain.getClass(), typeConverter, fieldDefinition).toBytes(domain);
                    }
                    byteMap.put(fieldDefinition, bytes);
                    return byteMap;
                }
            };
        }
        return binaryFieldFactory;
    }

    private ValueConverter findValueConverter(Class domainType, TypeConverter typeConverter, FieldDefinition fieldDefinition) {
        if (valueConverter != null) {
            if (!typeConverter.isRegistered(valueConverter)) {
                throw new IllegalArgumentException(MessageFormat.format(NO_VALUE_CONVERTER, new String[]{valueConverter.getName()}));
            }
            return typeConverter.find(valueConverter);
        } else {
            return typeConverter.type(domainType, fieldDefinition);
        }
    }
}
