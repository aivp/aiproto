package com.ling5821.aiproto.multiversion;

import com.ling5821.aiproto.Schema;
import com.ling5821.aiproto.annotation.JsonMessage;
import com.ling5821.aiproto.proto.DTPDescriptorProto;
import com.ling5821.aiproto.proto.PersonProto;
import com.ling5821.aiproto.util.SchemaUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.StringJoiner;

/**
 * @author lsj
 * @date 2021/8/26 15:08
 */
public class ProtoTest {
    public static void main(String[] args) {
        SchemaUtils.initialProtoBuf("com.ling5821.aiproto.proto", DTPDescriptorProto.messageOptions, "messageTypeId");
        Schema<PersonProto.Person> schema_t1 = SchemaUtils.getProtoBufSchema("1125", 1);
        Schema<PersonProto.Person> schema_t2 = SchemaUtils.getProtoBufSchema("1126", 1);

        ByteBuf buffer = Unpooled.buffer(32);
        PersonProto.Person person = PersonProto.Person.newBuilder().setId(1).setAge(15).setName("小白").setEmail("email").build();
        schema_t1.writeTo(buffer, person);
        String hex = ByteBufUtil.hexDump(buffer);
        System.out.println(hex);

        person = schema_t1.readFrom(buffer);
        System.out.println(person);
        System.out.println("=========================version: 1");

        buffer = Unpooled.buffer(32);
        schema_t2.writeTo(buffer, person);
        hex = ByteBufUtil.hexDump(buffer);
        System.out.println(hex);

        PersonProto.Person person2 = schema_t2.readFrom(buffer);
        System.out.println(person2);
        System.out.println("=========================version: 1");
    }
}
