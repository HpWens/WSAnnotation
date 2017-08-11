package com.example;

import android.support.annotation.ColorRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * desc:
 * author: wens
 * date: 2017/7/19.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface BindColor {
    @ColorRes int value() default 0;
}
