package com.example;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

/**
 * desc: 绑定处理类，一个 BindingClass 对应一个要生成的类
 * author: wens
 * date: 2017/7/18.
 */

public final class BindingClass {

    private static final ClassName FINDER = ClassName.get("com.github.butterknifelib", "Finder");
    private static final ClassName VIEW_BINDER = ClassName.get("com.github.butterknifelib", "ViewBinder");
    private static final ClassName VIEW = ClassName.get("android.view", "View");
    private static final ClassName UTILS = ClassName.get("com.github.butterknifelib", "Utils");
    private static final ClassName CONTEXT = ClassName.get("android.content", "Context");
    private static final ClassName RESOURCES = ClassName.get("android.content.res", "Resources");
    private static final ClassName CONTEXT_COMPAT = ClassName.get("android.support.v4.content", "ContextCompat");
    private static final ClassName ON_CLICK_LISTENER = ClassName.get("android.view.View", "OnClickListener");

    private final List<FieldResourceBinding> resourceBindings = new ArrayList<>();
    private final String classPackage;   // com.butterknife
    private final String className;     //  MainActivity$$ViewBinder
    private final String targetClass;  //  com.butterknife.MainActivity
    private final String classFqcn; // 全路径     com.butterknife.MainActivity$$ViewBinder

    private static final ClassName ILL = ClassName.get("java.lang", "IllegalAccessException");

    private BindingClass parentBinding;

    /**
     * 绑定处理类
     *
     * @param classPackage 包名：com.butterknife
     * @param className    生成的类：MainActivity$$ViewBinder
     * @param targetClass  目标类：com.butterknife.MainActivity
     * @param classFqcn    生成Class的完全限定名称：com.butterknife.MainActivity$$ViewBinder
     */
    public BindingClass(String classPackage, String className, String targetClass, String classFqcn) {
        this.classPackage = classPackage;
        this.className = className;
        this.targetClass = targetClass;
        this.classFqcn = classFqcn;
    }


    /**
     * @return JavaFile
     */
    public JavaFile brewJava() {
        //构建一个类
        TypeSpec.Builder result = TypeSpec.classBuilder(className) //MainActivity$$ViewBinder
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("T", ClassName.bestGuess(targetClass)));
        //  T  extends MainActivity

        if (_hasParentBinding()) {
            result.superclass(ParameterizedTypeName.get(ClassName.bestGuess(parentBinding.classFqcn),
                    TypeVariableName.get("T")));
        } else {
            result.addSuperinterface(ParameterizedTypeName.get(VIEW_BINDER, TypeVariableName.get("T")));
        }

        //实现 ViewBinder 接口的接口方法
        result.addMethod(_createBindMethod());

        return JavaFile.builder(classPackage, result.build())
                .addFileComment("Generated code from Butter Knife. Do not modify!")
                .build();
    }


    private MethodSpec _createBindMethod() {
        MethodSpec.Builder result = MethodSpec.methodBuilder("bind")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(FINDER, "finder", Modifier.FINAL)
                .addParameter(TypeVariableName.get("T"), "target", Modifier.FINAL)
                .addParameter(Object.class, "source");

        if (_hasParentBinding()) {
            // 调用父类的bind()方法
            result.addStatement("super.bind(finder, target, source)");
        }

        if (_hasResourceBinding()) {
            // 过滤警告
            result.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                    .addMember("value", "$S", "ResourceType")
                    .build());

            result.addStatement("$T context = finder.getContext(source)", CONTEXT);
            result.addStatement("$T res = context.getResources()", RESOURCES);
            // Resource

            //重点了解$S   $T   $L
            for (FieldResourceBinding binding : resourceBindings) {

//                if (binding.getId() == -1) {
//                    getBindingId(result, binding.getId(), binding.getName());
//                }

                result.addStatement("target.$L = res.$L($L)", binding.getName(), binding.getMethod(),
                        binding.getId());
            }
        }

        return result.build();
    }


    private void getBindingId(MethodSpec.Builder builder, int id, String name) {
        builder.addStatement("Class idClass = R.string.class");
        builder.addCode(" try {");
        builder.addStatement("$T field = idClass.getField(target.$N)", Field.class, name);
        builder.addCode("if (field != null) {");
        builder.addStatement("$T = field.getInt(idClass)", id);
        builder.addCode("}");
        builder.addCode(" } catch (NoSuchFieldException e) {  } catch ($T e) { }", ILL);
    }


    /**
     * @param binding
     */
    public void addResourceBinding(FieldResourceBinding binding) {
        if (!resourceBindings.contains(binding)) {
            resourceBindings.add(binding);
        }
    }

    /**
     * 设置父类
     *
     * @param parentBinding
     */
    public void setParentBinding(BindingClass parentBinding) {
        this.parentBinding = parentBinding;
    }

    private boolean _hasResourceBinding() {
        return !(resourceBindings.isEmpty());
    }

    private boolean _hasParentBinding() {
        return parentBinding != null;
    }
}
