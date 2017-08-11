package com.example.rclass;

import com.example.IRClass;
import com.example.IRInnerClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

/**
 * desc:
 * author: wens
 * date: 2017/8/9.
 */

public class RClass implements IRClass {

    private final Map<String, RInnerClass> rClass = new HashMap<>();

    public RClass(TypeElement rClassElement) {
        List<TypeElement> rInnerTypeElements = extractRInnerTypeElements(rClassElement);

        for (TypeElement rInnerTypeElement : rInnerTypeElements) {
            RInnerClass rInnerClass = new RInnerClass(rInnerTypeElement);
            rClass.put(rInnerTypeElement.getSimpleName().toString(), rInnerClass);
        }
    }

    private List<TypeElement> extractRInnerTypeElements(TypeElement rClassElement) {
        List<? extends Element> rEnclosedElements = rClassElement.getEnclosedElements();
        return ElementFilter.typesIn(rEnclosedElements);
    }


    @Override
    public IRInnerClass get(Res res) {

        String id = res.rName();

        IRInnerClass rInnerClass = rClass.get(id);

        if (rInnerClass != null) {
            return rInnerClass;
        } else {
            return new RInnerClass(null);
        }
    }
}
