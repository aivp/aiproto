package com.ling5821.aiproto.convert;

import com.ling5821.aiproto.IdStrategy;
import com.ling5821.aiproto.PrepareLoadStrategy;
import com.ling5821.aiproto.schema.NumberSchema;
import com.ling5821.aiproto.schema.StringSchema;

public class AttributeType extends PrepareLoadStrategy {

    public static final IdStrategy INSTANCE = new AttributeType();

    @Override
    protected void addSchemas(PrepareLoadStrategy schemaRegistry) {
        schemaRegistry.addSchema(1, NumberSchema.IntSchema.INSTANCE)
            .addSchema(2, StringSchema.Chars.getInstance((byte)0, "GBK"))

            .addSchema(3, Attr1.class).addSchema(4, Attr2.Attr2Schema.INSTANCE);
    }
}