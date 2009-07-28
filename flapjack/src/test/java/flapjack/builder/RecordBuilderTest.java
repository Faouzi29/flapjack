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
package flapjack.builder;

import flapjack.io.RecordWriter;
import flapjack.layout.SimpleRecordLayout;
import flapjack.layout.TextPaddingDescriptor;
import flapjack.model.BinaryFieldFactory;
import flapjack.model.ObjectMapping;
import flapjack.model.ObjectMappingStore;
import flapjack.util.ReverseValueConverter;
import flapjack.util.TypeConverter;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class RecordBuilderTest extends MockObjectTestCase {
    private Mock recordLayoutResolver;
    private ObjectMappingStore objectMappingStore;
    private RecordBuilder builder;
    private TypeConverter typeConverter;
    private Mock writer;

    protected void setUp() throws Exception {
        super.setUp();
        recordLayoutResolver = mock(BuilderRecordLayoutResolver.class);
        objectMappingStore = new ObjectMappingStore();
        typeConverter = new TypeConverter();
        writer = mock(RecordWriter.class);

        builder = new RecordBuilder();
        builder.setBuilderRecordLayoutResolver((BuilderRecordLayoutResolver) recordLayoutResolver.proxy());
        builder.setObjectMappingStore(objectMappingStore);
        builder.setTypeConverter(typeConverter);
    }

    public void test_build_NotEnoughDataForPaddedField() {
        Person person = new Person("Joe", "Smith");
        ObjectMapping objectMapping = new ObjectMapping(Person.class);
        objectMapping.field("First Name", "firstName");
        objectMappingStore.add(objectMapping);

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("First Name", 4, new TextPaddingDescriptor(TextPaddingDescriptor.Padding.RIGHT, ' '));

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));
        writer.expects(once()).method("write").with(eq("Joe ".getBytes()));
        writer.expects(once()).method("close");

        builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
    }

    public void test_build_NotEnoughDataForUnpaddedField() {
        Person person = new Person("Joe", "Smith");
        ObjectMapping objectMapping = new ObjectMapping(Person.class);
        objectMapping.field("First Name", "firstName");
        objectMappingStore.add(objectMapping);

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("First Name", 4);

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));
        writer.expects(once()).method("close");

        try {
            builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
            fail();
        } catch (BuilderException err) {
            assertEquals("Not enough data given! Did you forget the padding? Expected 4, but was 3, for field=\"First Name\" on layout=\"" + recordLayout.getId() + "\"", err.getMessage());
        }
    }

    public void test_build_CouldNotFindFieldMapping() {
        Person person = new Person("Joe", "Smith");
        objectMappingStore.add(new ObjectMapping(Person.class));

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("First Name", 3);

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));
        writer.expects(once()).method("close");

        try {
            builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
            fail();
        } catch (BuilderException err) {
            assertEquals("Could not find a FieldMapping for field=\"First Name\" on class " + Person.class.getName(), err.getMessage());
        }
    }

    public void test_build_CouldNotFindObjectMapping() {
        Person person = new Person("Joe", "Smith");

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{new SimpleRecordLayout("person")})));
        writer.expects(once()).method("close");

        try {
            builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
            fail();
        } catch (BuilderException err) {
            assertEquals("Could not find an ObjectMapping for " + Person.class.getName(), err.getMessage());
        }
    }

    public void test_build_CouldNotResolveRecordLayout() {
        Person person = new Person("Joe", "Smith");

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(new ArrayList()));
        writer.expects(once()).method("close");

        try {
            builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
            fail();
        } catch (BuilderException err) {
            assertEquals("Could not resolve RecordLayout(s) for " + Person.class.getName(), err.getMessage());
        }
    }

    public void test_build_ThrowExceptionWriting() {
        ObjectMapping personMapping = new ObjectMapping(Person.class);
        personMapping.field("First Name", "firstName");

        objectMappingStore.add(personMapping);

        Person person = new Person("Joe", "Smith");

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("First Name", 3);

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));

        IOException error = new IOException();

        try {
            writer.expects(once()).method("write").with(eq("Joe".getBytes())).will(throwException(error));
            writer.expects(once()).method("close");

            builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
            fail();
        } catch (BuilderException err) {
            assertEquals("A problem occured while building file", err.getMessage());
            assertSame(error, err.getCause());
        }
    }

    public void test_build_MultipleDomainObjectsOfDifferentType() throws IOException {
        ObjectMapping personMapping = new ObjectMapping(Person.class);
        personMapping.field("First Name", "firstName");
        personMapping.field("Last Name", "lastName");

        ObjectMapping addressMapping = new ObjectMapping(Address.class);
        addressMapping.field("Line 1", "line1");

        objectMappingStore.add(personMapping);
        objectMappingStore.add(addressMapping);

        Person person = new Person("Joe", "Smith");
        Address address = new Address("123 Easy Street");

        SimpleRecordLayout personRecordLayout = new SimpleRecordLayout("person");
        personRecordLayout.field("First Name", 3);
        personRecordLayout.field("Last Name", 5);

        SimpleRecordLayout addressRecordLayout = new SimpleRecordLayout("address");
        addressRecordLayout.field("Line 1", 10);

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{personRecordLayout})));
        recordLayoutResolver.expects(once()).method("resolve").with(eq(address)).will(returnValue(Arrays.asList(new Object[]{addressRecordLayout})));

        writer.expects(once()).method("write").with(eq("JoeSmith".getBytes()));
        writer.expects(once()).method("write").with(eq("123 Easy Street".getBytes()));
        writer.expects(once()).method("close");

        builder.build(Arrays.asList(new Object[]{person, address}), (RecordWriter) writer.proxy());
    }

    public void test_build_MultipleDomainObjectsOfTheSameType() throws IOException {
        ObjectMapping personMapping = new ObjectMapping(Person.class);
        personMapping.field("First Name", "firstName");
        personMapping.field("Last Name", "lastName");

        objectMappingStore.add(personMapping);

        Person person = new Person("Joe", "Smith");
        Person person2 = new Person("Tim", "Roger");

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("First Name", 3);
        recordLayout.field("Last Name", 5);

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));
        recordLayoutResolver.expects(once()).method("resolve").with(eq(person2)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));

        writer.expects(once()).method("write").with(eq("JoeSmith".getBytes()));
        writer.expects(once()).method("write").with(eq("TimRoger".getBytes()));
        writer.expects(once()).method("close");

        builder.build(Arrays.asList(new Object[]{person, person2}), (RecordWriter) writer.proxy());
    }

    public void test_build_SingleDomainObject_WithCompoundField() throws IOException {
        Mock binaryFieldFactory = mock(BinaryFieldFactory.class);

        ObjectMapping personMapping = new ObjectMapping(Person.class);
        personMapping.field(new String[]{"dob month", "dob day", "dob year"}, "dob", null, (BinaryFieldFactory) binaryFieldFactory.proxy());

        objectMappingStore.add(personMapping);

        Person person = new Person("Joe", "Smith");
        person.dob = new Date();

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("dob month", 2);
        recordLayout.field("dob day", 2);
        recordLayout.field("dob year", 4);

        byte[] data = new byte[]{1, 2, 3, 4};
        binaryFieldFactory.expects(once()).method("build").with(eq(person.dob), eq(typeConverter), eq(recordLayout.getFieldDefinitions())).will(returnValue(data));
        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));

        writer.expects(once()).method("write").with(eq(data));
        writer.expects(once()).method("close");

        builder.build(person, (RecordWriter) writer.proxy());
    }

    public void test_build_SingleDomainObject() throws IOException {
        ObjectMapping personMapping = new ObjectMapping(Person.class);
        personMapping.field("First Name", "firstName");
        personMapping.field("Last Name", "lastName");

        objectMappingStore.add(personMapping);

        Person person = new Person("Joe", "Smith");

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("First Name", 3);
        recordLayout.field("Last Name", 5);

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));

        writer.expects(once()).method("write").with(eq("JoeSmith".getBytes()));
        writer.expects(once()).method("close");

        builder.build(person, (RecordWriter) writer.proxy());
    }

    public void test_build_CustomValueConverter() throws IOException {
        ObjectMapping personMapping = new ObjectMapping(Person.class);
        personMapping.field("First Name", "firstName", ReverseValueConverter.class);

        objectMappingStore.add(personMapping);

        Person person = new Person("Joe", "Smith");

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("First Name", 3);

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));

        typeConverter.registerConverter(new ReverseValueConverter());

        writer.expects(once()).method("write").with(eq("eoJ".getBytes()));
        writer.expects(once()).method("close");

        builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
    }

    private static class Person {
        private String firstName;
        private String lastName;
        private Date dob;

        public Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    private static class Address {
        private String line1;

        private Address(String line1) {
            this.line1 = line1;
        }
    }
}
