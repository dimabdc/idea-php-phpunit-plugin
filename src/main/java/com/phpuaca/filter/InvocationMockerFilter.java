package com.phpuaca.filter;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.*;
import com.phpuaca.filter.util.ClassFinder;
import com.phpuaca.filter.util.Result;
import com.phpuaca.util.*;
import com.sun.istack.NotNull;

import java.util.*;

public class InvocationMockerFilter extends Filter {
    protected String method;

    public InvocationMockerFilter(FilterContext context) {
        super(context);

        PsiElement variable = PsiTreeUtil.findChildOfAnyType(context.getMethodReference(), Variable.class, FieldReference.class);
        MethodReference methodReference = getPhpElement(variable).findClosestAssignment();

        if (methodReference == null) {
            return;
        }

        Result classFinderResult = (new ClassFinder()).find(methodReference);
        if (classFinderResult == null) {
            return;
        }

        allowModifier(PhpModifier.PUBLIC_ABSTRACT_DYNAMIC);
        allowModifier(PhpModifier.PUBLIC_IMPLEMENTED_DYNAMIC);
        allowModifier(PhpModifier.PROTECTED_ABSTRACT_DYNAMIC);
        allowModifier(PhpModifier.PROTECTED_IMPLEMENTED_DYNAMIC);

        setPhpClass(classFinderResult.getPhpClass());

        boolean methodsDeclared = false;
        List<String> methodNames = Arrays.asList("setMethods", "addMethods", "onlyMethods");
        for (String methodName : methodNames) {
            MethodReference definitionMethodReference = (new PhpMethodChain(methodReference)).findMethodReference(methodName);
            if (definitionMethodReference == null) {
                continue;
            }
            if (addAllowMethods(definitionMethodReference, classFinderResult)) {
                methodsDeclared = true;
            }
        }

        if (!methodsDeclared) {
            addAllowMethods(methodReference, classFinderResult);
        }
    }

    private boolean addAllowMethods(@NotNull MethodReference definitionMethodReference, Result classFinderResult) {
        ParameterList parameterList = definitionMethodReference.getParameterList();
        if (parameterList != null) {
            PhpArrayParameter phpArrayParameter = PhpArrayParameter.create(parameterList, classFinderResult.getParameterNumber());
            if (phpArrayParameter != null) {
                allowMethods(phpArrayParameter.getValues());
                return true;
            }
        }
        return false;
    }

    private PhpElement getPhpElement(PsiElement element) {
        if (element instanceof Variable) {
            return new PhpVariable((Variable) element);
        }

        return new PhpFieldReference((FieldReference) element);
    }
}