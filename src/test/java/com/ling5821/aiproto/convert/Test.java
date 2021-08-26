package com.ling5821.aiproto.convert;

import com.ling5821.aiproto.DataType;
import com.ling5821.aiproto.Schema;
import com.ling5821.aiproto.annotation.Convert;
import com.ling5821.aiproto.annotation.Field;
import com.ling5821.aiproto.util.SchemaUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.util.Map;
import java.util.TreeMap;

public class Test {

    public static void main(String[] args) {
        Map<Integer, Schema<Foo>> multiVersionSchema = SchemaUtils.getSchema(Foo.class);
        Schema<Foo> schema_v1 = multiVersionSchema.get(1);
        Schema<Foo> schema_v2 = multiVersionSchema.get(2);

        ByteBuf buffer = Unpooled.buffer(32);
        schema_v1.writeTo(buffer, foo());
        String hex = ByteBufUtil.hexDump(buffer);
        System.out.println(hex);

        Foo foo = schema_v1.readFrom(buffer);
        System.out.println(foo);
        System.out.println("=========================version: 1");

        buffer = Unpooled.buffer(32);
        schema_v2.writeTo(buffer, foo());
        hex = ByteBufUtil.hexDump(buffer);
        System.out.println(hex);

        foo = schema_v2.readFrom(buffer);
        System.out.println(foo);
        System.out.println("=========================version: 2");
    }

    public static Foo foo() {
        Foo foo = new Foo();
        foo.setName("张三");
        foo.setId(128);
        Map<Integer, Object> attrs = new TreeMap<>();
        attrs.put(1, 123);
        attrs.put(2, "李四");
        attrs.put(3, new Attr1("test", 1));
        attrs.put(4, new Attr2(2, "test2"));
        foo.setAttributes(attrs);
        return foo;
    }

    public static class Foo {

        private String name;
        private int id;
        private Map<Integer, Object> attributes;

        @Field(index = 0, type = DataType.STRING, lengthSize = 1, desc = "名称", version = 1)
        @Field(index = 0, type = DataType.STRING, length = 10, desc = "名称", version = 2)
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Field(index = 1, type = DataType.UNSIGNED_SHORT, desc = "ID", version = 1)
        @Field(index = 1, type = DataType.INT, desc = "ID", version = 2)
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Convert(converter = AttributeConverter.class)
        @Field(index = 3, type = DataType.MAP, desc = "属性", version = {1, 2})
        public Map<Integer, Object> getAttributes() {
            return attributes;
        }

        public void setAttributes(Map<Integer, Object> attributes) {
            this.attributes = attributes;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Foo{");
            sb.append("name='").append(name).append('\'');
            sb.append(", id=").append(id);
            sb.append(", attributes=").append(attributes);
            sb.append('}');
            return sb.toString();
        }
    }
}
