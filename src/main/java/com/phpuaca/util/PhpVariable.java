package com.phpuaca.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.SmartList;
import com.jetbrains.php.lang.psi.elements.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhpVariable implements PhpElement {

    private Variable variable;

    public PhpVariable(@NotNull Variable variable) {
        this.variable = variable;
    }

    @Nullable
    public MethodReference findClosestAssignment() {
        String variableName = variable.getName();
        PsiElement cursor = variable;

        while (true) {
            cursor = PsiTreeUtil.getParentOfType(cursor, GroupStatement.class);
            if (cursor == null) {
                break;
            }

            SmartList<AssignmentExpression> statements = new SmartList<>();
            statements.addAll(PsiTreeUtil.findChildrenOfType(cursor, AssignmentExpression.class));

            Variable latestStatementVariable = null;

            for (AssignmentExpression expression : statements) {

                PhpPsiElement statementVariable = expression.getVariable();
                if (!(statementVariable instanceof Variable)) {
                    continue;
                }

                String statementVariableName = statementVariable.getName();
                if (statementVariableName == null || !statementVariableName.equals(variableName)) {
                    continue;
                }

                latestStatementVariable = (Variable)statementVariable;
            }

            if (latestStatementVariable != null) {
                MethodReference methodReference = PsiTreeUtil.findChildOfType(latestStatementVariable.getParent(), MethodReference.class);
                if (methodReference != null) {
                    return methodReference;
                }
            }
        }

        return null;
    }
}
