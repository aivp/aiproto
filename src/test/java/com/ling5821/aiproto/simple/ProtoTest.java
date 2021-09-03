package com.ling5821.aiproto.simple;

import com.ling5821.aiproto.DataType;
import com.ling5821.aiproto.Schema;
import com.ling5821.aiproto.annotation.Field;
import com.ling5821.aiproto.proto.Person;
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
        Map<Integer, Schema<PersonWrapperChild>> multiVersionSchema = SchemaUtils.getProtoBufSchema(PersonWrapperChild.class);
        Schema<PersonWrapperChild> schema = multiVersionSchema.get(1);
        if (schema == null) {
            System.out.println("schema is null");
            return;
        }

        PersonWrapperChild protoWrapper = new PersonWrapperChild();
        ByteBuf buffer = Unpooled.buffer(32);
        Person person = Person.newBuilder().setId(1).setAge(15).setName("小白").setEmail("email").build();
        protoWrapper.setData(person);
        schema.writeTo(buffer, protoWrapper);
        System.out.println(ByteBufUtil.hexDump(buffer));

        PersonWrapper personWRead = schema.readFrom(buffer);
        System.out.println(personWRead.getData());
        System.out.println(personWRead.getRealData().getClass().getName());

        buffer = Unpooled.buffer(32);
        schema = SchemaUtils.getProtoBufSchema(PersonWrapperChild.class, 1);
        schema.writeTo(buffer, protoWrapper);

        System.out.println(ByteBufUtil.hexDump(buffer));

        personWRead = schema.readFrom(buffer);
        System.out.println(personWRead.getData());

        System.out.println(personWRead.getRealData());
    }

    public static class PersonWrapper {
        private Person data;

        @Field(index = 0, type = DataType.PROTO_BUF)
        public Person getData() {
            return data;
        }

        public void setData(Person data) {
            this.data = data;
        }

        public PersonWrapper getRealData() {
            return this;
        }
    }

    public static class PersonWrapperChild extends PersonWrapper {

    }
}
