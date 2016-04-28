/*
 * Copyright (c) 2014, Francis Galiegue (fgaliegue@gmail.com)
 *
 * This software is dual-licensed under:
 *
 * - the Lesser General Public License (LGPL) version 3.0 or, at your option, any
 *   later version;
 * - the Apache Software License (ASL) version 2.0.
 *
 * The text of this file and of both licenses is available at the root of this
 * project or, if you have the jar distribution, in directory META-INF/, under
 * the names LGPL-3.0.txt and ASL-2.0.txt respectively.
 *
 * Direct link to the sources:
 *
 * - LGPL 3.0: https://www.gnu.org/licenses/lgpl-3.0.txt
 * - ASL 2.0: http://www.apache.org/licenses/LICENSE-2.0.txt
 */

package com.github.fge.jsonpatch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jackson.jsonpointer.JsonPointerException;
import com.google.common.collect.ImmutableList;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public final class AppendOperationTest
        extends JsonPatchOperationTest {
    public AppendOperationTest()
            throws IOException {
        super("append");
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testAppend() throws JsonPointerException, JsonPatchException, JsonProcessingException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("test", "Hello ");
        JsonPatchOperation operation = new AppendOperation(
                new JsonPointer("/test"), TextNode.valueOf(" World!"));
        JsonPatch patch = new JsonPatch(ImmutableList.of(operation));
        Map value = mapper.treeToValue(patch.apply(mapper.valueToTree(map)), HashMap.class);

        assertEquals("Hello World!", value.get("test"));
    }
}
