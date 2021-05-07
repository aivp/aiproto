package com.ling5821.aiproto;

import com.ling5821.aiproto.schema.ArraySchema;
import com.ling5821.aiproto.schema.NumberSchema;

import java.util.HashMap;
import java.util.Map;

/**
 * PrepareLoadStrategy
 *
 * @author lsj
 * @date 2021/3/24 16:22
 */
public abstract class PrepareLoadStrategy extends IdStrategy {

    private final Map<Object, Schema> typeClassMapping = new HashMap<>();

    protected PrepareLoadStrategy() {
        this.addSchemas(this);
    }

    protected abstract void addSchemas(PrepareLoadStrategy schemaRegistry);

    @Override
    public <T> Schema<T> getSchema(Class<T> typeClass) {
        return typeClassMapping.get(typeClass);
    }

    public PrepareLoadStrategy addSchema(Object key, Schema schema) {
        if (schema == null)
            throw new RuntimeException("key[" + key + "],schema is null");
        typeIdMapping.put(key, schema);
        return this;
    }

    public PrepareLoadStrategy addSchema(Object key, Class typeClass) {
        loadSchema(typeClassMapping, key, typeClass);
        return this;
    }

    public PrepareLoadStrategy addSchema(Object key, DataType dataType) {
        switch (dataType) {
            case BYTE:
                this.typeIdMapping.put(key, NumberSchema.ByteSchema.INSTANCE);
                break;
            case UNSIGNED_BYTE:
                this.typeIdMapping.put(key, NumberSchema.UnsignedByteSchema.INSTANCE);
                break;
            case UNSIGNED_BYTE_INT:
                this.typeIdMapping.put(key, NumberSchema.UnsignedByteIntSchema.INSTANCE);
                break;
            case SHORT:
                this.typeIdMapping.put(key, NumberSchema.ShortSchema.INSTANCE);
                break;
            case SHORT_LE:
                this.typeIdMapping.put(key, NumberSchema.ShortLESchema.INSTANCE);
                break;
            case UNSIGNED_SHORT:
                this.typeIdMapping.put(key, NumberSchema.UnsignedShortSchema.INSTANCE);
                break;
            case UNSIGNED_SHORT_LE:
                this.typeIdMapping.put(key, NumberSchema.UnsignedShortLESchema.INSTANCE);
                break;
            case INT:
                this.typeIdMapping.put(key, NumberSchema.IntSchema.INSTANCE);
                break;
            case INT_LE:
                this.typeIdMapping.put(key, NumberSchema.IntLESchema.INSTANCE);
                break;
            case UNSIGNED_INT:
                this.typeIdMapping.put(key, NumberSchema.UnsignedIntSchema.INSTANCE);
                break;
            case UNSIGNED_INT_LE:
                this.typeIdMapping.put(key, NumberSchema.UnsignedIntLESchema.INSTANCE);
                break;
            case LONG:
                this.typeIdMapping.put(key, NumberSchema.LongSchema.INSTANCE);
                break;
            case LONG_LE:
                this.typeIdMapping.put(key, NumberSchema.LongLESchema.INSTANCE);
                break;
            case FLOAT:
                this.typeIdMapping.put(key, NumberSchema.FloatSchema.INSTANCE);
                break;
            case FLOAT_LE:
                this.typeIdMapping.put(key, NumberSchema.FloatLESchema.INSTANCE);
                break;
            case DOUBLE:
                this.typeIdMapping.put(key, NumberSchema.DoubleSchema.INSTANCE);
                break;
            case DOUBLE_LE:
                this.typeIdMapping.put(key, NumberSchema.DoubleLESchema.INSTANCE);
                break;
            case BYTES:
                this.typeIdMapping.put(key, ArraySchema.ByteArraySchema.INSTANCE);
                break;
            default:
                throw new RuntimeException("不支持的类型转换");
        }
        return this;
    }
}
