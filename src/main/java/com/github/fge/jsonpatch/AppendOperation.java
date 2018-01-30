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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.google.common.collect.Iterables;

/**
 * JSON Patch {@code append} operation
 * <p>For this operation, {@code path} points to the value to append, and
 * {@code value} is the value should be appended.</p>
 * <p>It is an error condition if {@code path} does not point to an actual JSON
 * value.</p>
 */
public final class AppendOperation
        extends PathValueOperation {
    public final static String OP = "append";

    @JsonCreator
    public AppendOperation(@JsonProperty("path") final JsonPointer path,
                           @JsonProperty("value") final JsonNode value) {
        super(OP, path, value);
    }

    @Override
    public JsonNode apply(final JsonNode node)
            throws JsonPatchException {
        if (path.path(node).isMissingNode()) {
            throw new JsonPatchException(BUNDLE.getMessage("jsonPatch.noSuchPath"));
        }
        final String part = value.asText();
        if (path.isEmpty()) {
            return value.deepCopy();
        }
        final JsonNode ret = node.deepCopy();
        final JsonNode parent = path.parent().get(ret);
        final String rawToken = Iterables.getLast(path).getToken().getRaw();
        if (parent.isObject()) {
            ObjectNode p = (ObjectNode) parent;
            p.put(rawToken, p.get(rawToken).asText() + part);
        }
        return ret;
    }
}
