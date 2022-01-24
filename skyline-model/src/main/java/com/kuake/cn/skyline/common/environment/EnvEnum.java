package com.kuake.cn.skyline.common.environment;

/**
 * 描述：环境枚举类
 * @author: kuake.cn
 * @create: 2021-03-28 15:05
 **/
public enum EnvEnum {
    DEV("dev", "dev/environment.properties"),
    FAT("fat", "dev/environment.properties"),
    PRO("pro", "dev/environment.properties");
    private String name;
    private String path;

    EnvEnum(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static EnvEnum getInstanceByName(String name) {
        EnvEnum[] values = EnvEnum.values();
        for (EnvEnum value:
             values) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return DEV;
    }
}
