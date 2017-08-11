package com.example.rclass;

import com.example.IRClass;
import com.example.RClassNotFoundException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * desc:
 * author: wens
 * date: 2017/8/9.
 */

public class AndroidRClassFinder {

    private final ProcessingEnvironment processingEnv;

    public AndroidRClassFinder(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    public IRClass find() throws RClassNotFoundException {

        Elements elementUtils = processingEnv.getElementUtils();

        TypeElement androidRType = elementUtils.getTypeElement("android.R");

        if (androidRType == null) {
            throw new RClassNotFoundException("The android.R class cannot be found");
        }

        return new RClass(androidRType);
    }

}
