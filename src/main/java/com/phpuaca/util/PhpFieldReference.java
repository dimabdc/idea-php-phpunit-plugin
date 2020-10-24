package com.phpuaca.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.containers.MultiMap;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.elements.impl.PhpClassImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class PhpFieldReference implements PhpElement {

    private final FieldReference fieldReference;

    public PhpFieldReference(@NotNull FieldReference fieldReference) {
        this.fieldReference = fieldReference;
    }

    @Nullable
    public MethodReference findClosestAssignment() {
        String variableName = fieldReference.getName();
        PsiElement element = fieldReference.resolve();
        PhpClass phpClass;

        if (element != null) {
            phpClass = ((Field)element).getContainingClass();
        } else {
            phpClass = PsiTreeUtil.getParentOfType(fieldReference, PhpClass.class);
        }

        if (phpClass == null) {
            return null;
        }

        MultiMap<String, AssignmentExpression> accessMap = PhpClassImpl.getPhpUnitSetUpAssignmentsPerField(phpClass);
        if (!accessMap.containsKey(fieldReference.getName())) {
            return null;
        }

        Collection<AssignmentExpression> expressions = accessMap.get(fieldReference.getName());
        for (AssignmentExpression expression : expressions) {
            expression.getVariable();
            PhpPsiElement statementVariable = expression.getVariable();
            if (!(statementVariable instanceof FieldReference)) {
                continue;
            }

            String statementVariableName = statementVariable.getName();
            if (statementVariableName == null || !statementVariableName.equals(variableName)) {
                continue;
            }

            MethodReference methodReference = PsiTreeUtil.getChildOfType(expression, MethodReference.class);
            if (methodReference != null) {
                return methodReference;
            }

            Variable variable = PsiTreeUtil.getChildOfType(expression, Variable.class);
            if (variable != null) {
                methodReference = (new PhpVariable(variable)).findClosestAssignment();
                if (methodReference != null) {
                    return methodReference;
                }
            }
        }

        return null;
    }
}
