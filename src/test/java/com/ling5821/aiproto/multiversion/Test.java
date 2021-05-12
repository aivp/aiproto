package com.ling5821.aiproto.multiversion;

import com.ling5821.aiproto.DataType;
import com.ling5821.aiproto.Schema;
import com.ling5821.aiproto.annotation.Field;
import com.ling5821.aiproto.util.SchemaUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.util.Map;

public class Test {

    public static void main(String[] args) {
        Map<Integer, Schema<Foo>> multiVersionSchema = SchemaUtils.getSchema(Foo.class);
        Schema<Foo> schema_v0 = multiVersionSchema.get(0);
        Schema<Foo> schema_v1 = multiVersionSchema.get(1);

        ByteBuf buffer = Unpooled.buffer(32);
        Foo foo = foo();
        schema_v0.writeTo(buffer, foo);
        String hex = ByteBufUtil.hexDump(buffer);
        System.out.println(hex);

        foo = schema_v0.readFrom(buffer);
        System.out.println(foo);
        System.out.println("=========================version: 0");

        buffer = Unpooled.buffer(32);
        schema_v1.writeTo(buffer, foo());
        hex = ByteBufUtil.hexDump(buffer);
        System.out.println(hex);

        foo = schema_v1.readFrom(buffer);
        System.out.println(foo);
        System.out.println("=========================version: 1");
    }

    public static Foo foo() {
        Foo foo = new Foo();
        foo.setName("张三");
        foo.setId((short)128);
        foo.setAge((short)23);
        return foo;
    }

    public static class Foo {

        private String name;
        private short id;
        private short age;

        private long timestamp;

        @Field(index = 0, type = DataType.STRING, lengthSize = 1, desc = "名称")
        @Field(index = 0, type = DataType.STRING, length = 10, desc = "名称", version = 1)
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Field(index = 1, type = DataType.SHORT, desc = "ID")
        @Field(index = 1, type = DataType.SHORT_LE, desc = "ID", version = 1)
        public short getId() {
            return id;
        }

        public void setId(short id) {
            this.id = id;
        }

        @Field(index = 3, type = DataType.SHORT_LE, desc = "年龄", version = 1)
        public short getAge() {
            return age;
        }

        public void setAge(short age) {
            this.age = age;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Foo{");
            sb.append("name='").append(name).append('\'');
            sb.append(", id=").append(id);
            sb.append(", age=").append(age);
            sb.append('}');
            return sb.toString();
        }
    }
}
