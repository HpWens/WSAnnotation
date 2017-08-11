package com.example.rclass;

import com.example.CaseHelper;
import com.example.IRInnerClass;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;

/**
 * desc:
 * author: wens
 * date: 2017/8/9.
 */

public class RInnerClass implements IRInnerClass{

    private final Map<Integer, String> idQualifiedNamesByIdValues = new HashMap<>();
    private final Set<String> idQualifiedNames = new HashSet<>();

    private final String rInnerQualifiedName;

    public RInnerClass(TypeElement rInnerTypeElement) {
        if (rInnerTypeElement != null) {

            rInnerQualifiedName = rInnerTypeElement.getQualifiedName().toString();

            List<? extends Element> idEnclosedElements = rInnerTypeElement.getEnclosedElements();

            List<VariableElement> idFields = ElementFilter.fieldsIn(idEnclosedElements);

            for (VariableElement idField : idFields) {
                TypeKind fieldType = idField.asType().getKind();
                if (fieldType.isPrimitive() && fieldType.equals(TypeKind.INT)) {
                    String idQualifiedName = rInnerQualifiedName + "." + idField.getSimpleName();
                    idQualifiedNames.add(idQualifiedName);
                    Integer idFieldId = (Integer) idField.getConstantValue();
                    if (idFieldId != null) {
                        idQualifiedNamesByIdValues.put(idFieldId, idQualifiedName);
                    }
                }
            }
        } else {
            rInnerQualifiedName = "";
        }
    }

    @Override
    public boolean containsIdValue(Integer idValue) {
        return idQualifiedNamesByIdValues.containsKey(idValue);
    }

    @Override
    public String getIdQualifiedName(Integer idValue) {
        return idQualifiedNamesByIdValues.get(idValue);
    }

    @Override
    public boolean containsField(String name) {
        boolean containsField = idQualifiedNames.contains(rInnerQualifiedName + "." + name);

        if (!containsField) {
            String snakeCaseName = CaseHelper.camelCaseToSnakeCase(name);
            containsField = idQualifiedNames.contains(rInnerQualifiedName + "." + snakeCaseName);
        }

        return containsField;
    }

    @Override
    public String getIdQualifiedName(String name) {
        String idQualifiedName = rInnerQualifiedName + "." + name;

        if (idQualifiedNames.contains(idQualifiedName)) {
            return idQualifiedName;
        } else {
            String snakeCaseName = CaseHelper.camelCaseToSnakeCase(name);
            idQualifiedName = rInnerQualifiedName + "." + snakeCaseName;
            if (idQualifiedNames.contains(idQualifiedName)) {
                return idQualifiedName;
            } else {
                return null;
            }
        }
    }
}
