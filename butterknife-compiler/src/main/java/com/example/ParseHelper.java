package com.example;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;


/**
 * desc:注解解析绑定帮助类
 * author: wens
 * date: 2017/7/18.
 */

public class ParseHelper {

    private static final String BINDING_CLASS_SUFFIX = "$$ViewBinder";
    private static final String COLOR_STATE_LIST_TYPE = "android.content.res.ColorStateList";
    private static final String LIST_TYPE = List.class.getCanonicalName();
    private static final String ITERABLE_TYPE = "java.lang.Iterable<?>";
    static final String VIEW_TYPE = "android.view.View";
    static final int NO_ID = -1;

    private ParseHelper() {
        throw new AssertionError("No instances.");
    }

    /**
     * 解析 String 资源
     *
     * @param element
     * @param targetClassMap
     * @param erasedTargetNames
     * @param elementUtils
     */
    public static void parseResString(Element element, Map<TypeElement, BindingClass>
            targetClassMap, Set<TypeElement> erasedTargetNames, Elements elementUtils) {
        // 获取字段名和注解的资源ID
        String name = element.getSimpleName().toString();
        int resId = element.getAnnotation(BindString.class).value();
        //处理默认情况
        if (resId == -1) {
            TypeElement androidRType = elementUtils.getTypeElement("com.github.butterknifelib.R.string");
            List<? extends Element> idEnclosedElements = androidRType.getEnclosedElements();
            List<VariableElement> idFields = ElementFilter.fieldsIn(idEnclosedElements);
            for (VariableElement idField : idFields) {
                TypeKind fieldType = idField.asType().getKind();
                if (fieldType.isPrimitive() && fieldType.equals(TypeKind.INT)) {
                    if (idField.getSimpleName().toString().toLowerCase().replaceAll("_", "")
                            .equals(name.startsWith("m") ? name.substring(1, name.length()).toLowerCase() : name.toLowerCase())) {
                        resId = (int) idField.getConstantValue();
                        break;
                    }
                }
            }
        }

        //生成 MainActivity$$ViewBinder
        BindingClass bindingClass = _getOrCreateTargetClass(element, targetClassMap, elementUtils);

        //生成资源信息
        FieldResourceBinding binding = new FieldResourceBinding(resId, name, "getString");

        // 给BindingClass添加资源信息
        bindingClass.addResourceBinding(binding);

        // 保存包含注解元素的目标类，注意是使用注解的外围类，主要用来处理父类继承，例：MainActivity
        erasedTargetNames.add((TypeElement) element.getEnclosingElement());
    }

    /**
     * 获取存在的 BindingClass，没有则重新生成
     *
     * @param element        使用注解的元素
     * @param targetClassMap 映射表
     * @param elementUtils   元素工具类
     * @return BindingClass
     */
    private static BindingClass _getOrCreateTargetClass(Element element, Map<TypeElement, BindingClass> targetClassMap,
                                                        Elements elementUtils) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();//获取 mainActivity  类
        BindingClass bindingClass = targetClassMap.get(enclosingElement);
        // 以下以 mainActivity这个类为例
        if (bindingClass == null) {
            // 获取元素的完全限定名称：com.butterknife.MainActivity
            /**
             * 返回此类型元素的完全限定名称。更准确地说，返回规范 名称。对于没有规范名称的局部类和匿名类，返回一个空名称。
             一般类型的名称不包括对其形式类型参数的任何引用。例如，接口 java.util.set 的完全限定名称是 "java.util.set"。嵌套类型使用 "." 作为分隔符，如 "java.util.map
             .entry" 中所示。
             */
            String targetType = enclosingElement.getQualifiedName().toString();
            // 获取元素所在包名：com.butterknife
            String classPackage = elementUtils.getPackageOf(enclosingElement).getQualifiedName().toString();
            // 获取要生成的Class的名称：MainActivity$$ViewBinder
            int packageLen = classPackage.length() + 1;
            String className = targetType.substring(packageLen).replace('.', '$') + BINDING_CLASS_SUFFIX;
            // 生成Class的完全限定名称：com.butterknife.MainActivity$$ViewBinder
            String classFqcn = classPackage + "." + className;

            /* 不要用下面这个来生成Class名称，内部类会出错,比如ViewHolder */
//            String className = enclosingElement.getSimpleName() + BINDING_CLASS_SUFFIX;

            //com.butterknife      MainActivity$$ViewBinder       com.butterknife.MainActivity        com.butterknife
            // .MainActivity$$ViewBinder
            bindingClass = new BindingClass(classPackage, className, targetType, classFqcn);
            targetClassMap.put(enclosingElement, bindingClass);
        }

        return bindingClass;

    }

}
