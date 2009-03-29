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

public class FieldMapping {
    private String domainFieldName;
    private String recordFieldName;
    private Class valueConverter;

    public FieldMapping(String recordFieldName, String domainFieldName) {
        this(recordFieldName, domainFieldName, null);
    }

    public FieldMapping(String recordFieldName, String domainFieldName, Class valueConverterClass) {
        this.recordFieldName = recordFieldName;
        this.domainFieldName = domainFieldName;
        this.valueConverter = valueConverterClass;
    }

    public String getDomainFieldName() {
        return domainFieldName;
    }

    public String getRecordFieldName() {
        return recordFieldName;
    }

    public Class getValueConverterClass() {
        return valueConverter;
    }
}
