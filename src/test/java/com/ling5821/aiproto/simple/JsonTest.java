package com.ling5821.aiproto.simple;

import com.ling5821.aiproto.FieldFactory;
import com.ling5821.aiproto.Schema;
import com.ling5821.aiproto.annotation.JsonMessage;
import com.ling5821.aiproto.util.SchemaUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.util.Map;

/**
 * @author lsj
 * @date 2021/8/26 15:08
 */
public class JsonTest {
    public static void main(String[] args) {
        FieldFactory.EXPLAIN = true;
        Map<Integer, Schema<Foo>> multiVersionSchema = SchemaUtils.getJsonSchema(Foo.class);
        Schema<Foo> schema = multiVersionSchema.get(1);
        if (schema == null) {
            System.out.println("schema is null");
            return;
        }

        ByteBuf buffer = Unpooled.buffer(32);
        Foo foo = foo();
        schema.writeTo(buffer, foo);
        String hex = ByteBufUtil.hexDump(buffer);
        System.out.println(hex);

        foo = schema.readFrom(buffer);
        System.out.println(foo);


        buffer = Unpooled.buffer(32);
        schema = SchemaUtils.getJsonSchema(Foo.class, 1);
        schema.writeTo(buffer, foo);

        hex = ByteBufUtil.hexDump(buffer);
        System.out.println(hex);

        foo = schema.readFrom(buffer);
        System.out.println(foo);
    }


    public static Foo foo() {
        Foo foo = new Foo();
        foo.setName("张三");
        foo.setId((short)128);
        foo.setAge((short)23);
        return foo;
    }

    @JsonMessage(value = {"Foo"}, version = {1})
    public static class Foo {

        private String name;

        private short id;

        private short age;

        private long timestamp;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public short getId() {
            return id;
        }

        public void setId(short id) {
            this.id = id;
        }

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
