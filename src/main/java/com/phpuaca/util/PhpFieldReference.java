package com.phpuaca.util;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.SmartList;
import com.intellij.util.containers.MultiMap;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.elements.impl.PhpClassImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public class PhpFieldReference implements PhpElement {

    private final FieldReference fieldReference;

    public PhpFieldReference(@NotNull FieldReference fieldReference) {
        this.fieldReference = fieldReference;
    }

    @Nullable
    public MethodReference findClosestAssignment() {
        Collection<AssignmentExpression> expressions = getAssignmentsByField(fieldReference);
        for (AssignmentExpression expression : expressions) {
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

    private Collection<AssignmentExpression> getAssignmentsByField(FieldReference field) {
        Collection<AssignmentExpression> expressions = new SmartList<>();
        expressions.addAll(getAssignmentsInCurrentStatementGroup(field));
        expressions.addAll(getPhpUnitSetUpAssignmentsPerField(field));
        return expressions;
    }

    private Collection<AssignmentExpression> getAssignmentsInCurrentStatementGroup(FieldReference field) {
        String fieldName = field.getName();
        if (StringUtil.isEmpty(fieldName)) {
            return Collections.emptyList();
        }

        GroupStatement groupStatement = PsiTreeUtil.getParentOfType(fieldReference, GroupStatement.class);
        if (groupStatement == null) {
            return Collections.emptyList();
        }

        AssignmentExpression lastAssignmentExpressions = null;
        for (AssignmentExpression expression: PsiTreeUtil.findChildrenOfAnyType(groupStatement, AssignmentExpression.class)) {
            PhpPsiElement variable = expression.getVariable();
            if (!(variable instanceof FieldReference)) {
                continue;
            }

            if (!fieldName.equals(variable.getName())) {
                continue;
            }

            lastAssignmentExpressions = expression;
        }

        if (lastAssignmentExpressions == null) {
            return Collections.emptyList();
        }

        return new SmartList<>(lastAssignmentExpressions);
    }

    private Collection<AssignmentExpression> getPhpUnitSetUpAssignmentsPerField(FieldReference field) {
        PsiElement element = field.resolve();
        PhpClass phpClass;

        if (element != null) {
            phpClass = ((Field)element).getContainingClass();
        } else {
            phpClass = PsiTreeUtil.getParentOfType(field, PhpClass.class);
        }

        if (phpClass == null) {
            return Collections.emptyList();
        }

        MultiMap<String, AssignmentExpression> accessMap = PhpClassImpl.getPhpUnitSetUpAssignmentsPerField(phpClass);
        if (!accessMap.containsKey(field.getName())) {
            return Collections.emptyList();
        }

        return accessMap.get(fieldReference.getName());
    }
}
