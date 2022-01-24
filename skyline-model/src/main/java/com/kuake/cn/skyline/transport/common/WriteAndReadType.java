package com.kuake.cn.skyline.transport.common;

public enum WriteAndReadType {
    NULL("null", 0),
    BYTE("byte", 1),
    INT("int", 4);

    private String name;

    private Integer code;

    WriteAndReadType(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static WriteAndReadType getInstanceByCode(Integer code) {
        WriteAndReadType[] values = WriteAndReadType.values();

        for (WriteAndReadType value:
             values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }

        return values[0];
    }

}
