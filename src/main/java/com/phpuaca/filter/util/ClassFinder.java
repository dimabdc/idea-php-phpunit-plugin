package com.phpuaca.filter.util;

import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.Variable;
import com.jetbrains.php.lang.psi.elements.impl.PhpClassImpl;
import com.phpuaca.filter.FilterConfigItem;
import com.phpuaca.filter.FilterFactory;
import com.phpuaca.util.PhpClassResolver;
import com.phpuaca.util.PhpMethodChain;
import com.phpuaca.util.PhpVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final public class ClassFinder {

    @Nullable
    public Result find(@NotNull MethodReference methodReference) {
        MethodReference mockBuilderMethodReference = getMockBuilderMethodReference(methodReference);
        if (mockBuilderMethodReference == null) {
            return null;
        }

        String methodNameToFind = getMockBuilderMethodName(mockBuilderMethodReference);
        FilterConfigItem filterConfigItem = FilterFactory.getInstance().getConfig().getItem(methodNameToFind);
        if (filterConfigItem == null) {
            return null;
        }

        PhpClass phpClass = (new PhpClassResolver()).resolveByMethodReferenceContainingParameterListWithClassReference(mockBuilderMethodReference);
        if (phpClass == null) {
            return null;
        }

        return new Result(phpClass, filterConfigItem.getParameterNumber());
    }

    @Nullable
    public Result find(@NotNull Variable variable) {
        MethodReference methodReference = (new PhpVariable(variable)).findClosestAssignment();
        return methodReference == null ? null : find(methodReference);
    }

    @Nullable
    private MethodReference getMockBuilderMethodReference(@NotNull MethodReference methodReference) {
        MethodReference mockBuilderMethodReference = (new PhpMethodChain(methodReference)).findMethodReference("getMockBuilder");
        if (mockBuilderMethodReference != null) {
            return mockBuilderMethodReference;
        }

        String methodName = methodReference.getName();
        if (methodName != null && (methodName.startsWith("getMock") || methodName.startsWith("createMock"))) {
            return methodReference;
        }

        return null;
    }

    private String getMockBuilderMethodName(@NotNull MethodReference methodReference) {
        String methodNameToFind = "setMethods";

        String methodName = methodReference.getName();
        if (methodName != null && !methodName.equals("getMockBuilder") && (methodName.startsWith("getMock") || methodName.startsWith("createMock"))) {
            if (methodName.startsWith("createMock")) {
                methodNameToFind = "getMock";
            } else {
                methodNameToFind = methodName;
            }
        }

        return methodNameToFind;
    }
}
