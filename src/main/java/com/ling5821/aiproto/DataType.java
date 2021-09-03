package com.ling5821.aiproto;

/**
 * @author lsj
 * @date 2021/1/22 10:32
 */
public enum DataType {
    /**
     * 有符号字节整型
     */
    BYTE(1),

    /**
     * 无符号字节整型
     */
    UNSIGNED_BYTE(1),

    /**
     * 无符号字节整型转int
     */
    UNSIGNED_BYTE_INT(1),

    /**
     * 有符号短整型(2个字节, 大端字节序)
     */
    SHORT(2),

    /**
     * 有符号短整型(2个字节, 小端字节序)
     */
    SHORT_LE(2),

    /**
     * 无符号短整型(2个字节, 大端字节序)
     */
    UNSIGNED_SHORT(2),

    /**
     * 无符号短整型(2个字节, 小端字节序)
     */
    UNSIGNED_SHORT_LE(2),

    /**
     * 有符号整型(4个字节, 大端字节序)
     */
    INT(4),

    /**
     * 有符号整型(4个字节, 小端字节序)
     */
    INT_LE(4),

    /**
     * 无符号整型(4个字节, 大端字节序)
     */
    UNSIGNED_INT(4),

    /**
     * 无符号整型(4个字节, 小端字节序)
     */
    UNSIGNED_INT_LE(4),

    /**
     * 有符号长整型(8个字节, 大端字节序)
     */
    LONG(8),

    /**
     * 有符号长整型(8个字节, 小端字节序)
     */
    LONG_LE(8),

    /**
     * 无符号长整型(8个字节, 大端字节序)
     * 暂时不支持
     */
    //    UNSIGNED_LONG(8),

    /**
     * 无符号长整型(8个字节, 小端字节序)
     * 不支持
     */
    //    UNSIGNED_LONG_LE(8),

    /**
     * 有符号单精度浮点数(4个字节, 大端字节序)
     */
    FLOAT(4),

    /**
     * 有符号单精度浮点数(4个字节, 小端字节序)
     */
    FLOAT_LE(4),

    /**
     * 无符号单精度浮点数(4个字节, 大端字节序)
     * 不支持
     */
    //    UNSIGNED_FLOAT(4),

    /**
     * 无符号单精度浮点数(4个字节, 小端字节序)
     * 不支持
     */
    //    UNSIGNED_FLOAT_LE(4),

    /**
     * 有符号双精度浮点数(8个字节, 大端字节序)
     */
    DOUBLE(8),

    /**
     * 有符号双精度浮点数(8个字节, 小端字节序)
     */
    DOUBLE_LE(8),

    /**
     * 无符号双精度浮点数(8个字节, 大端字节序)
     *
     */
    //    UNSIGNED_DOUBLE(8),

    /**
     * 无符号双精度浮点数(8个字节, 小端字节序)
     * 不支持
     */
    //    UNSIGNED_DOUBLE_LE(8),

    /**
     * 字节数据
     */
    BYTES(-1),

    /**
     * 字符串
     */
    STRING(-1),

    /**
     * 对象
     */
    OBJ(-1),

    /**
     * 列表
     */
    LIST(-1),

    /**
     * 字典
     */
    MAP(-1),

    /**
     * ProtoBuf 3 字段类型
     */
    PROTO_BUF(-1),
    ;
    public int length;

    DataType(int length) {
        this.length = length;
    }
}
