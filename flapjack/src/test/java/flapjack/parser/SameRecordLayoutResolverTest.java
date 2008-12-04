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
package flapjack.parser;

import junit.framework.TestCase;
import flapjack.layout.SimpleRecordLayout;
import flapjack.layout.RecordLayout;


public class SameRecordLayoutResolverTest extends TestCase {

    public void test_constructor() {
        try {
            new SameRecordLayoutResolver(null);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Cannot construct with a 'null' RecordLayout", err.getMessage());
        }
    }
    
    public void test_constructor_NoDefaultConstructor() {
        try {
            new SameRecordLayoutResolver(NoDefaultConstructor.class);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Cannot construct without a default constructor", err.getMessage());
        }
    }
    
    public void test_resolve() {
        SameRecordLayoutResolver resolver = new SameRecordLayoutResolver(SimpleRecordLayout.class);

        RecordLayout recordLayout = resolver.resolve(null);
        assertTrue(recordLayout instanceof SimpleRecordLayout);
    }

    private static class NoDefaultConstructor extends SimpleRecordLayout {
        private NoDefaultConstructor(String blah) {
        }
    }
}