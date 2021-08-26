package com.ling5821.aiproto.multiversion;

import com.ling5821.aiproto.Schema;
import com.ling5821.aiproto.annotation.JsonMessage;
import com.ling5821.aiproto.util.SchemaUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.StringJoiner;

/**
 * @author aidong
 * @date 2021/8/26 15:08
 */
public class JsonTest {
    public static void main(String[] args) {
        SchemaUtils.initial("com.ling5821.aiproto.multiversion");
        Schema<Foo> schema_v1 = SchemaUtils.getJsonSchema("Foo", 1);
        Schema<Foo2> schema_v2 = SchemaUtils.getJsonSchema("Foo", 2);

        ByteBuf buffer = Unpooled.buffer(32);
        Foo foo = foo();
        schema_v1.writeTo(buffer, foo);
        String hex = ByteBufUtil.hexDump(buffer);
        System.out.println(hex);

        foo = schema_v1.readFrom(buffer);
        System.out.println(foo);
        System.out.println("=========================version: 1");

        buffer = Unpooled.buffer(32);
        schema_v2.writeTo(buffer, foo2());
        hex = ByteBufUtil.hexDump(buffer);
        System.out.println(hex);

        Foo2 foo2 = schema_v2.readFrom(buffer);
        System.out.println(foo2);
        System.out.println("=========================version: 1");
    }


    public static Foo foo() {
        Foo foo = new Foo();
        foo.setName("张三");
        foo.setId((short)128);
        foo.setAge((short)23);
        return foo;
    }

    public static Foo2 foo2() {
        Foo2 foo2 = new Foo2();
        foo2.setName2("张三2");
        foo2.setAge3(85);
        foo2.setTimestamp(Instant.now().toEpochMilli());
        foo2.setTime(LocalDateTime.now());
        foo2.setId2(4343546565L);
        return foo2;
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

    @JsonMessage(value = {"Foo"}, version = {2})
    public static class Foo2 {

        private String name2;

        private LocalDateTime time;

        private long id2;

        private int age3;

        private long timestamp;

        public String getName2() {
            return name2;
        }

        public void setName2(String name2) {
            this.name2 = name2;
        }

        public LocalDateTime getTime() {
            return time;
        }

        public void setTime(LocalDateTime time) {
            this.time = time;
        }

        public long getId2() {
            return id2;
        }

        public void setId2(long id2) {
            this.id2 = id2;
        }

        public int getAge3() {
            return age3;
        }

        public void setAge3(int age3) {
            this.age3 = age3;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Foo2.class.getSimpleName() + "[", "]").add("name2='" + name2 + "'")
                .add("time=" + time).add("id2=" + id2).add("age3=" + age3).add("timestamp=" + timestamp).toString();
        }
    }
}
