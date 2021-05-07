package com.ling5821.aiproto.schema;

import com.ling5821.aiproto.DataType;
import com.ling5821.aiproto.Schema;
import io.netty.buffer.ByteBuf;

/**
 * @author lsj
 * @date 2021/1/22 14:59
 * 默认端序为大端字节序,小端序可以使用**LE
 */
public class NumberSchema {
    public static Schema getSchema(DataType dataType) {
        Schema schema;
        switch (dataType) {
            case BYTE:
                schema = ByteSchema.INSTANCE;
                break;
            case UNSIGNED_BYTE:
                schema = UnsignedByteSchema.INSTANCE;
                break;
            case UNSIGNED_BYTE_INT:
                schema = UnsignedByteIntSchema.INSTANCE;
                break;
            case SHORT:
                schema = ShortSchema.INSTANCE;
                break;
            case SHORT_LE:
                schema = ShortLESchema.INSTANCE;
                break;
            case UNSIGNED_SHORT:
                schema = UnsignedShortSchema.INSTANCE;
                break;
            case UNSIGNED_SHORT_LE:
                schema = UnsignedShortLESchema.INSTANCE;
                break;
            case INT:
                schema = IntSchema.INSTANCE;
                break;
            case INT_LE:
                schema = IntLESchema.INSTANCE;
                break;
            case UNSIGNED_INT:
                schema = UnsignedIntSchema.INSTANCE;
                break;
            case UNSIGNED_INT_LE:
                schema = UnsignedIntLESchema.INSTANCE;
                break;
            case LONG:
                schema = LongSchema.INSTANCE;
                break;
            case LONG_LE:
                schema = LongLESchema.INSTANCE;
                break;
            case FLOAT:
                schema = FloatSchema.INSTANCE;
                break;
            case FLOAT_LE:
                schema = FloatLESchema.INSTANCE;
                break;
            case DOUBLE:
                schema = DoubleSchema.INSTANCE;
                break;
            case DOUBLE_LE:
                schema = DoubleLESchema.INSTANCE;
                break;
            default:
                throw new RuntimeException("不支持的类型");
        }
        return schema;
    }

    /* 字节类型模板 */

    /**
     * 有符号字节类型模板
     */
    public static class ByteSchema implements Schema<Byte> {
        public static final Schema<Byte> INSTANCE = new ByteSchema();

        @Override
        public Byte readFrom(ByteBuf input) {
            return input.readByte();
        }

        @Override
        public void writeTo(ByteBuf output, Byte message) {
            output.writeByte(message);
        }
    }

    /**
     * 无符号字节类型模板
     */
    public static class UnsignedByteSchema implements Schema<Short> {
        public static final Schema<Short> INSTANCE = new UnsignedByteSchema();

        @Override
        public Short readFrom(ByteBuf input) {
            return input.readUnsignedByte();
        }

        @Override
        public void writeTo(ByteBuf output, Short message) {
            output.writeByte(message);
        }
    }

    public static class UnsignedByteIntSchema implements Schema<Integer> {
        public static final Schema<Integer> INSTANCE = new UnsignedByteIntSchema();

        @Override
        public Integer readFrom(ByteBuf input) {
            return (int)input.readUnsignedByte();
        }

        @Override
        public void writeTo(ByteBuf output, Integer message) {
            output.writeByte(message);
        }
    }

    /* 短整型模板 */

    /**
     * 有符号短整型模板
     */
    public static class ShortSchema implements Schema<Short> {
        public static final Schema<Short> INSTANCE = new ShortSchema();

        @Override
        public Short readFrom(ByteBuf input) {
            return input.readShort();
        }

        @Override
        public void writeTo(ByteBuf output, Short message) {
            output.writeShort(message);
        }
    }

    /**
     * 有符号短整型模板(小端字节序)
     */
    public static class ShortLESchema implements Schema<Short> {
        public static final Schema<Short> INSTANCE = new ShortLESchema();

        @Override
        public Short readFrom(ByteBuf input) {
            return input.readShortLE();
        }

        @Override
        public void writeTo(ByteBuf output, Short message) {
            output.writeShortLE(message);
        }
    }

    /**
     * 无符号短整型模板
     */
    public static class UnsignedShortSchema implements Schema<Integer> {
        public static final Schema<Integer> INSTANCE = new UnsignedShortSchema();

        @Override
        public Integer readFrom(ByteBuf input) {
            return input.readUnsignedShort();
        }

        @Override
        public void writeTo(ByteBuf output, Integer message) {
            output.writeShort(message);
        }
    }

    /**
     * 无符号短整型模板(小端字节序)
     */
    public static class UnsignedShortLESchema implements Schema<Integer> {
        public static final Schema<Integer> INSTANCE = new UnsignedShortLESchema();

        @Override
        public Integer readFrom(ByteBuf input) {
            return input.readUnsignedShortLE();
        }

        @Override
        public void writeTo(ByteBuf output, Integer message) {
            output.writeShortLE(message);
        }
    }

    /* 整型模板 */

    /**
     * 有符号整型模板
     */
    public static class IntSchema implements Schema<Integer> {
        public static final Schema<Integer> INSTANCE = new IntSchema();

        @Override
        public Integer readFrom(ByteBuf input) {
            return input.readInt();
        }

        @Override
        public void writeTo(ByteBuf output, Integer message) {
            output.writeInt(message);
        }
    }

    /**
     * 有符号整型模板(小端字节序)
     */
    public static class IntLESchema implements Schema<Integer> {
        public static final Schema<Integer> INSTANCE = new IntLESchema();

        @Override
        public Integer readFrom(ByteBuf input) {
            return input.readIntLE();
        }

        @Override
        public void writeTo(ByteBuf output, Integer message) {
            output.writeIntLE(message);
        }
    }

    /**
     * 无符号整型模板
     */
    public static class UnsignedIntSchema implements Schema<Long> {
        public static final Schema<Long> INSTANCE = new UnsignedIntSchema();

        @Override
        public Long readFrom(ByteBuf input) {
            return input.readUnsignedInt();
        }

        @Override
        public void writeTo(ByteBuf output, Long message) {
            output.writeInt(message.intValue());
        }
    }

    /**
     * 无符号整型模板(小端字节序)
     */
    public static class UnsignedIntLESchema implements Schema<Long> {
        public static final Schema<Long> INSTANCE = new UnsignedIntLESchema();

        @Override
        public Long readFrom(ByteBuf input) {
            return input.readUnsignedIntLE();
        }

        @Override
        public void writeTo(ByteBuf output, Long message) {
            output.writeIntLE(message.intValue());
        }
    }


    /* 长整型模板 */

    /**
     * 有符号长整型模板
     */
    public static class LongSchema implements Schema<Long> {
        public static final Schema<Long> INSTANCE = new LongSchema();

        @Override
        public Long readFrom(ByteBuf input) {
            return input.readLong();
        }

        @Override
        public void writeTo(ByteBuf output, Long message) {
            output.writeLong(message);
        }
    }

    /**
     * 有符号长整型模板(小端字节序)
     */
    public static class LongLESchema implements Schema<Long> {
        public static final Schema<Long> INSTANCE = new LongLESchema();

        @Override
        public Long readFrom(ByteBuf input) {
            return input.readLongLE();
        }

        @Override
        public void writeTo(ByteBuf output, Long message) {
            output.writeLongLE(message);
        }
    }

    /* 单精度浮点类型模板 */

    /**
     * 有符号单精度浮点数模板
     */
    public static class FloatSchema implements Schema<Float> {
        public static final Schema<Float> INSTANCE = new FloatSchema();

        @Override
        public Float readFrom(ByteBuf input) {
            return input.readFloat();
        }

        @Override
        public void writeTo(ByteBuf output, Float message) {
            output.writeFloat(message);
        }
    }

    /**
     * 有符号单精度浮点数模板(小端字节序)
     */
    public static class FloatLESchema implements Schema<Float> {
        public static final Schema<Float> INSTANCE = new FloatLESchema();

        @Override
        public Float readFrom(ByteBuf input) {
            return input.readFloatLE();
        }

        @Override
        public void writeTo(ByteBuf output, Float message) {
            output.writeFloatLE(message);
        }
    }

    /* 双精度浮点数模板 */

    /**
     * 有符号双精度浮点数模板
     */
    public static class DoubleSchema implements Schema<Double> {
        public static final Schema<Double> INSTANCE = new DoubleSchema();

        @Override
        public Double readFrom(ByteBuf input) {
            return input.readDouble();
        }

        @Override
        public void writeTo(ByteBuf output, Double message) {
            output.writeDouble(message);
        }
    }

    /**
     * 有符号双精度浮点数模板(小端字节序)
     */
    public static class DoubleLESchema implements Schema<Double> {
        public static final Schema<Double> INSTANCE = new DoubleLESchema();

        @Override
        public Double readFrom(ByteBuf input) {
            return input.readDoubleLE();
        }

        @Override
        public void writeTo(ByteBuf output, Double message) {
            output.writeDoubleLE(message);
        }
    }
}
