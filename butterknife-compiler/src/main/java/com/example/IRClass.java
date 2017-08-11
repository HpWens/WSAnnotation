package com.example;

import java.util.Locale;

/**
 * desc:
 * author: wens
 * date: 2017/8/9.
 */

public interface  IRClass {

    enum Res {
        LAYOUT, ID, STRING, ARRAY, COLOR, ANIM, BOOL, DIMEN, DRAWABLE, INTEGER, MOVIE, MENU, RAW, XML;
        public String rName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }

    IRInnerClass get(Res res);

}
