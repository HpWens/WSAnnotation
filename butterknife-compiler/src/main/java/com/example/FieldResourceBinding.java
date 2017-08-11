package com.example;

/**
 * desc: 资源信息
 * author: wens
 * date: 2017/7/18.
 */

public class FieldResourceBinding {

    // 资源ID
    private final int id;
    // 字段变量名称
    private final String name;
    // 获取资源数据的方法
    private final String method;

    public FieldResourceBinding(int id, String name, String method) {
        this.id = id;
        this.name = name;
        this.method = method;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMethod() {
        return method;
    }

}
