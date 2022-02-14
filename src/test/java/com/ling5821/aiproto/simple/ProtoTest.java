package com.ling5821.aiproto.simple;

import com.ling5821.aiproto.Schema;
import com.ling5821.aiproto.proto.PersonProto;
import com.ling5821.aiproto.proto.PersonProto.Person;
import com.ling5821.aiproto.proto.XBaiProto.XBai;
import com.ling5821.aiproto.proto.XHongProto.XHong;
import com.ling5821.aiproto.util.SchemaUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.util.Map;

/**
 * @author lsj
 * @date 2021/8/26 15:08
 */
public class ProtoTest {
    public static void main(String[] args) {
        Map<Integer, Schema<PersonProto.Person>> multiVersionSchema = SchemaUtils.getProtoBufSchema(PersonProto.Person.class);
        Schema<PersonProto.Person> schema = multiVersionSchema.get(1);
        if (schema == null) {
            System.out.println("schema is null");
            return;
        }

        ByteBuf buffer = Unpooled.buffer(32);
        PersonProto.Person person = Person.newBuilder()
            .setId(1)
            .setAge(15)
            .setName("小白")
            .setEmail("email")
            .setBai(XBai.newBuilder()
                .setId(1)
                .setName("bai")
                .build()
            )
            .setHong(XHong.newBuilder()
                .setId(2)
                .setWeight("222")
                .setHeight(15)
                .build()
            )
            .build();
        schema.writeTo(buffer, person);
        System.out.println(ByteBufUtil.hexDump(buffer));

        PersonProto.Person personWRead = schema.readFrom(buffer);
        System.out.println(personWRead.getHong());
        System.out.println(personWRead.getBai());

        buffer = Unpooled.buffer(32);
        schema = SchemaUtils.getProtoBufSchema(PersonProto.Person.class, 1);
        schema.writeTo(buffer, person);

        System.out.println(ByteBufUtil.hexDump(buffer));

        personWRead = schema.readFrom(buffer);
        System.out.println(personWRead);
    }
}
