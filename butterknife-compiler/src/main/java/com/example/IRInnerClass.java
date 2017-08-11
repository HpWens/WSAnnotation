package com.example;

/**
 * desc:
 * author: wens
 * date: 2017/8/9.
 */

public interface IRInnerClass {

    boolean containsIdValue(Integer idValue);

    boolean containsField(String name);

    String getIdQualifiedName(Integer idValue);

    String getIdQualifiedName(String name);
}
