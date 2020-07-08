package com.phpuaca.filter;

import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpModifier;
import com.jetbrains.php.lang.psi.elements.Variable;
import com.phpuaca.filter.util.ClassFinder;
import com.phpuaca.filter.util.Result;
import com.phpuaca.util.PhpArrayParameter;
import com.phpuaca.util.PhpMethodChain;
import com.phpuaca.util.PhpVariable;
import com.sun.istack.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvocationMockerFilter extends Filter {
    protected String method;

    public InvocationMockerFilter(FilterContext context) {
        super(context);

        Variable variable = (Variable) PsiTreeUtil.getDeepestFirst(context.getMethodReference()).getParent();
        MethodReference methodReference = (new PhpVariable(variable)).findClosestAssignment();

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
}
