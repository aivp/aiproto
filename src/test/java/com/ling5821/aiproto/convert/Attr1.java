package com.ling5821.aiproto.convert;

import com.ling5821.aiproto.DataType;
import com.ling5821.aiproto.annotation.Field;

public class Attr1 {

    private String name;
    private int id;

    public Attr1() {
    }

    public Attr1(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Field(index = 0, type = DataType.STRING, lengthSize = 1, desc = "名称")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Field(index = 1, type = DataType.INT, desc = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Attr1{");
        sb.append("name='").append(name).append('\'');
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}
