package com.example;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * desc:
 * author: wens
 * date: 2017/7/19.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface ViewById {

    @IdRes int[] value() default 0;

}
