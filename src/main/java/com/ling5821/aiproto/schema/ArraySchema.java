package com.ling5821.aiproto.schema;

import com.ling5821.aiproto.DataType;
import com.ling5821.aiproto.Schema;
import io.netty.buffer.ByteBuf;

/**
 * @author lsj
 * @date 2021/1/25 10:53
 */
public class ArraySchema {
    public static Schema getSchema(DataType dataType) {
        Schema schema;
        switch (dataType) {
            case BYTE:
                schema = ByteArraySchema.INSTANCE;
                break;
            case UNSIGNED_BYTE:
                schema = UnsignedByteArraySchema.INSTANCE;
                break;
            case SHORT:
                schema = ShortArraySchema.INSTANCE;
                break;
            case SHORT_LE:
                schema = ShortLEArraySchema.INSTANCE;
                break;
            case UNSIGNED_SHORT:
                schema = UnsignedShortArraySchema.INSTANCE;
                break;
            case UNSIGNED_SHORT_LE:
                schema = UnsignedShortLEArraySchema.INSTANCE;
                break;
            case INT:
                schema = IntArraySchema.INSTANCE;
                break;
            case INT_LE:
                schema = IntLEArraySchema.INSTANCE;
                break;
            case UNSIGNED_INT:
                schema = UnsignedIntArraySchema.INSTANCE;
                break;
            case UNSIGNED_INT_LE:
                schema = UnsignedIntLEArraySchema.INSTANCE;
                break;
            case LONG:
                schema = LongArraySchema.INSTANCE;
                break;
            case LONG_LE:
                schema = LongLEArraySchema.INSTANCE;
                break;
            case FLOAT:
                schema = FloatArraySchema.INSTANCE;
                break;
            case FLOAT_LE:
                schema = FloatLEArraySchema.INSTANCE;
                break;
            case DOUBLE:
                schema = DoubleArraySchema.INSTANCE;
                break;
            case DOUBLE_LE:
                schema = DoubleLEArraySchema.INSTANCE;
                break;
            default:
                throw new RuntimeException("不支持的类型转换");
        }
        return schema;
    }

    /**
     * 有符号字节数组模板
     */
    public static class ByteArraySchema implements Schema<byte[]> {
        public static final Schema<byte[]> INSTANCE = new ByteArraySchema();

        private ByteArraySchema() {
        }

        @Override
        public byte[] readFrom(ByteBuf input) {
            return readFrom(input, -1);
        }

        @Override
        public byte[] readFrom(ByteBuf input, int length) {
            if (length < 0) {
                length = input.readableBytes();
            }
            byte[] array = new byte[length];
            input.readBytes(array);
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, byte[] array) {
            output.writeBytes(array);
        }

        @Override
        public void writeTo(ByteBuf output, int length, byte[] array) {
            output.writeBytes(array, 0, length);
        }
    }

    /**
     * 无符号字节数组模板
     */
    public static class UnsignedByteArraySchema implements Schema<short[]> {
        public static final Schema<short[]> INSTANCE = new UnsignedByteArraySchema();

        private UnsignedByteArraySchema() {
        }

        @Override
        public short[] readFrom(ByteBuf input) {
            return readFrom(input, -1);
        }

        @Override
        public short[] readFrom(ByteBuf input, int length) {
            if (length < 0) {
                length = input.readableBytes();
            }

            short[] array = new short[length];
            for (int i = 0; i < length; i++) {
                array[i] = input.readUnsignedByte();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, short[] array) {
            for (short value : array) {
                output.writeByte(value);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, short[] array) {
            for (int i = 0; i < length; i++) {
                output.writeByte(array[i]);
            }
        }
    }

    /**
     * 有符号短整型数组模板
     */
    public static class ShortArraySchema implements Schema<short[]> {
        public static final Schema<short[]> INSTANCE = new ShortArraySchema();

        private ShortArraySchema() {
        }

        @Override
        public short[] readFrom(ByteBuf input) {
            return readFrom(input, -1);
        }

        @Override
        public short[] readFrom(ByteBuf input, int length) {
            if (length < 0) {
                length = input.readableBytes();
            }
            int total = length >> 1;
            short[] array = new short[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readShort();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, short[] array) {
            for (short value : array) {
                output.writeShort(value);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, short[] array) {
            for (int i = 0, total = length >> 1; i < total; i++) {
                output.writeShort(array[i]);
            }
        }
    }

    /**
     * 有符号短整型数组模板(小端字节序)
     */
    public static class ShortLEArraySchema implements Schema<short[]> {
        public static final Schema<short[]> INSTANCE = new ShortLEArraySchema();

        private ShortLEArraySchema() {
        }

        @Override
        public short[] readFrom(ByteBuf input) {
            return readFrom(input, -1);
        }

        @Override
        public short[] readFrom(ByteBuf input, int length) {
            if (length < 0) {
                length = input.readableBytes();
            }
            int total = length >> 1;
            short[] array = new short[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readShortLE();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, short[] array) {
            for (short value : array) {
                output.writeShortLE(value);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, short[] array) {
            for (int i = 0, total = length >> 1; i < total; i++) {
                output.writeShortLE(array[i]);
            }
        }
    }

    /**
     * 无符号短整型数组模板
     */
    public static class UnsignedShortArraySchema implements Schema<int[]> {
        public static final Schema<int[]> INSTANCE = new UnsignedShortArraySchema();

        private UnsignedShortArraySchema() {
        }

        @Override
        public int[] readFrom(ByteBuf input) {
            return readFrom(input, -1);
        }

        @Override
        public int[] readFrom(ByteBuf input, int length) {
            if (length < 0) {
                length = input.readableBytes();
            }
            int total = length >> 1;
            int[] array = new int[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readUnsignedShort();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, int[] array) {
            for (int value : array) {
                output.writeShort(value);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, int[] array) {
            for (int i = 0, total = length >> 1; i < total; i++) {
                output.writeShort(array[i]);
            }
        }
    }

    /**
     * 无符号短整型数组模板(小端字节序)
     */
    public static class UnsignedShortLEArraySchema implements Schema<int[]> {
        public static final Schema<int[]> INSTANCE = new UnsignedShortLEArraySchema();

        private UnsignedShortLEArraySchema() {
        }

        @Override
        public int[] readFrom(ByteBuf input) {
            return readFrom(input, -1);
        }

        @Override
        public int[] readFrom(ByteBuf input, int length) {
            if (length < 0) {
                length = input.readableBytes();
            }
            int total = length >> 1;
            int[] array = new int[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readUnsignedShortLE();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, int[] array) {
            for (int value : array) {
                output.writeShortLE(value);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, int[] array) {
            for (int i = 0, total = length >> 1; i < total; i++) {
                output.writeShortLE(array[i]);
            }
        }
    }

    /**
     * 有符号整型数组模板
     */
    public static class IntArraySchema implements Schema<int[]> {
        public static final Schema<int[]> INSTANCE = new IntArraySchema();

        private IntArraySchema() {
        }

        @Override
        public int[] readFrom(ByteBuf input) {
            return readFrom(input, -1);
        }

        @Override
        public int[] readFrom(ByteBuf input, int length) {
            if (length < 0) {
                length = input.readableBytes();
            }
            int total = length >> 2;
            int[] array = new int[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readInt();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, int[] array) {
            for (int value : array) {
                output.writeInt(value);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, int[] array) {
            for (int i = 0, total = length >> 2; i < total; i++) {
                output.writeInt(array[i]);
            }
        }
    }

    /**
     * 有符号整型数组模板(小端字节序)
     */
    public static class IntLEArraySchema implements Schema<int[]> {
        public static final Schema<int[]> INSTANCE = new IntLEArraySchema();

        private IntLEArraySchema() {
        }

        @Override
        public int[] readFrom(ByteBuf input) {
            return readFrom(input, -1);
        }

        @Override
        public int[] readFrom(ByteBuf input, int length) {
            if (length < 0) {
                length = input.readableBytes();
            }
            int total = length >> 2;
            int[] array = new int[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readIntLE();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, int[] array) {
            for (int value : array) {
                output.writeIntLE(value);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, int[] array) {
            for (int i = 0, total = length >> 2; i < total; i++) {
                output.writeIntLE(array[i]);
            }
        }
    }

    /**
     * 无符号整型数组模板
     */
    public static class UnsignedIntArraySchema implements Schema<long[]> {
        public static final Schema<long[]> INSTANCE = new UnsignedIntArraySchema();

        private UnsignedIntArraySchema() {
        }

        @Override
        public long[] readFrom(ByteBuf input) {
            return readFrom(input, -1);
        }

        @Override
        public long[] readFrom(ByteBuf input, int length) {
            if (length < 0) {
                length = input.readableBytes();
            }
            int total = length >> 2;
            long[] array = new long[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readUnsignedInt();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, long[] array) {
            for (long value : array) {
                output.writeInt((int)value);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, long[] array) {
            for (int i = 0, total = length >> 2; i < total; i++) {
                output.writeInt((int)array[i]);
            }
        }
    }

    /**
     * 无符号整型数组模板(小端字节序)
     */
    public static class UnsignedIntLEArraySchema implements Schema<long[]> {
        public static final Schema<long[]> INSTANCE = new UnsignedIntLEArraySchema();

        private UnsignedIntLEArraySchema() {
        }

        @Override
        public long[] readFrom(ByteBuf input) {
            return readFrom(input, -1);
        }

        @Override
        public long[] readFrom(ByteBuf input, int length) {
            if (length < 0) {
                length = input.readableBytes();
            }
            int total = length >> 2;
            long[] array = new long[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readUnsignedIntLE();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, long[] array) {
            for (long value : array) {
                output.writeIntLE((int)value);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, long[] array) {
            for (int i = 0, total = length >> 2; i < total; i++) {
                output.writeIntLE((int)array[i]);
            }
        }
    }

    /**
     * 有符号长整型数组模板
     */
    public static class LongArraySchema implements Schema<long[]> {
        public static final Schema INSTANCE = new LongArraySchema();

        private LongArraySchema() {
        }

        @Override
        public long[] readFrom(ByteBuf input) {
            return readFrom(input, -1);
        }

        @Override
        public long[] readFrom(ByteBuf input, int length) {
            if (length < 0) {
                length = input.readableBytes();
            }
            int total = length >> 3;
            long[] array = new long[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readLong();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, long[] array) {
            for (long value : array) {
                output.writeLong(value);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, long[] array) {
            for (int i = 0, total = length >> 3; i < total; i++) {
                output.writeLong(array[i]);
            }
        }
    }

    /**
     * 有符号长整型数组模板(小端字节序)
     */
    public static class LongLEArraySchema implements Schema<long[]> {
        public static final Schema INSTANCE = new LongLEArraySchema();

        private LongLEArraySchema() {
        }

        @Override
        public long[] readFrom(ByteBuf input) {
            return readFrom(input, -1);
        }

        @Override
        public long[] readFrom(ByteBuf input, int length) {
            if (length < 0) {
                length = input.readableBytes();
            }
            int total = length >> 3;
            long[] array = new long[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readLongLE();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, long[] array) {
            for (long value : array) {
                output.writeLongLE(value);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, long[] array) {
            for (int i = 0, total = length >> 3; i < total; i++) {
                output.writeLongLE(array[i]);
            }
        }
    }

    /**
     * 有符号单精度浮点数数组模板
     */
    public static class FloatArraySchema implements Schema<float[]> {
        public static final Schema<float[]> INSTANCE = new FloatArraySchema();

        private FloatArraySchema() {
        }

        @Override
        public float[] readFrom(ByteBuf input) {
            return readFrom(input, -1);
        }

        @Override
        public float[] readFrom(ByteBuf input, int length) {
            if (length < 0) {
                length = input.readableBytes();
            }
            int total = length >> 2;
            float[] array = new float[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readFloat();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, float[] array) {
            for (float value : array) {
                output.writeFloat(value);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, float[] array) {
            for (int i = 0, total = length >> 2; i < total; i++) {
                output.writeFloat(array[i]);
            }
        }
    }

    /**
     * 有符号单精度浮点数数组模板(小端字节序)
     */
    public static class FloatLEArraySchema implements Schema<float[]> {
        public static final Schema<float[]> INSTANCE = new FloatLEArraySchema();

        private FloatLEArraySchema() {
        }

        @Override
        public float[] readFrom(ByteBuf input) {
            return readFrom(input, -1);
        }

        @Override
        public float[] readFrom(ByteBuf input, int length) {
            if (length < 0) {
                length = input.readableBytes();
            }
            int total = length >> 2;
            float[] array = new float[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readFloatLE();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, float[] array) {
            for (float value : array) {
                output.writeFloatLE(value);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, float[] array) {
            for (int i = 0, total = length >> 2; i < total; i++) {
                output.writeFloatLE(array[i]);
            }
        }
    }

    /**
     * 有符号双精度浮点数数组模板
     */
    public static class DoubleArraySchema implements Schema<double[]> {
        public static final Schema<double[]> INSTANCE = new DoubleArraySchema();

        private DoubleArraySchema() {
        }

        @Override
        public double[] readFrom(ByteBuf input) {
            return readFrom(input, -1);
        }

        @Override
        public double[] readFrom(ByteBuf input, int length) {
            if (length < 0) {
                length = input.readableBytes();
            }
            int total = length >> 2;
            double[] array = new double[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readDouble();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, double[] array) {
            for (double value : array) {
                output.writeDouble(value);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, double[] array) {
            for (int i = 0, total = length >> 2; i < total; i++) {
                output.writeDouble(array[i]);
            }
        }
    }

    /**
     * 有符号双精度浮点数数组模板(小端字节序)
     */
    public static class DoubleLEArraySchema implements Schema<double[]> {
        public static final Schema<double[]> INSTANCE = new DoubleLEArraySchema();

        private DoubleLEArraySchema() {
        }

        @Override
        public double[] readFrom(ByteBuf input) {
            return readFrom(input, -1);
        }

        @Override
        public double[] readFrom(ByteBuf input, int length) {
            if (length < 0) {
                length = input.readableBytes();
            }
            int total = length >> 2;
            double[] array = new double[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readDoubleLE();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, double[] array) {
            for (double value : array) {
                output.writeDoubleLE(value);
            }
        }

        @Override
        public void writeTo(ByteBuf output, int length, double[] array) {
            for (int i = 0, total = length >> 2; i < total; i++) {
                output.writeDoubleLE(array[i]);
            }
        }
    }
}
