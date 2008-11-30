/**
 * Copyright 2008 Dan Dudley
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
package flapjack.annotation.model;

import flapjack.model.RecordFactory;
import flapjack.test.User;
import flapjack.test.UserRecordLayout;
import flapjack.test2.PhoneRecordLayout;
import flapjack.test2.Phone;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class MappedRecordFactoryResolverTest extends TestCase {

    public void test_resolve() {
        MappedRecordFactoryResolver resolver = new MappedRecordFactoryResolver();
        resolver.setPackages(Arrays.asList("flapjack.test"));

        UserRecordLayout layout = new UserRecordLayout();

        RecordFactory recordFactory = resolver.resolve(layout);

        assertNotNull(recordFactory);
        assertTrue(recordFactory instanceof MappedRecordFactory);

        Map<String, String> fields = new HashMap<String, String>();
        fields.put("First Name", "foo");
        fields.put("Last Name", "bar");

        Object obj = recordFactory.build(fields, layout);
        assertNotNull(obj);
        assertTrue(obj instanceof User);

        User user = (User) obj;
        assertEquals("foo", user.getFirstName());
        assertEquals("bar", user.getLastName());
    }

    public void test_resolve_MultipleClasses() {
        MappedRecordFactoryResolver resolver = new MappedRecordFactoryResolver();
        resolver.setPackages(Arrays.asList("flapjack.test", "flapjack.test2"));

        PhoneRecordLayout layout = new PhoneRecordLayout();

        RecordFactory recordFactory = resolver.resolve(layout);

        assertNotNull(recordFactory);
        assertTrue(recordFactory instanceof MappedRecordFactory);

        Map<String, String> fields = new HashMap<String, String>();
        fields.put("Area", "123");
        fields.put("Number", "456-7890");

        Object obj = recordFactory.build(fields, layout);
        assertNotNull(obj);
        assertTrue(obj instanceof Phone);

        Phone phone = (Phone) obj;
        assertEquals("123", phone.getAreaCode());
        assertEquals("456-7890", phone.getNumber());
    }

}