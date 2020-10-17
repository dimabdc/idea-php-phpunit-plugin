package com.phpuaca.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhpParameter {

    final private static int NUMBER_NOT_FOUND = -1;
    final private static int NUMBER_UNDEFINED = -2;

    private final PsiElement parameter;
    private int number;

    public PhpParameter(@NotNull PsiElement parameter) {
        this(parameter, NUMBER_UNDEFINED);
    }

    public PhpParameter(@NotNull PsiElement parameter, int number) {
        this.parameter = parameter;
        this.number = number;
    }

    final public PsiElement getParameter() {
        return parameter;
    }

    final public int getNumber() {
        if (number == NUMBER_UNDEFINED) {
            number = calcNumber();
        }
        return number;
    }

    private int calcNumber() {
        ParameterList parameterList = PsiTreeUtil.getParentOfType(parameter, ParameterList.class);
        if (parameterList != null) {
            int index = ArrayUtils.indexOf(parameterList.getParameters(), parameter);
            if (index >= 0) {
                return index + 1;
            }
        }

        return NUMBER_NOT_FOUND;
    }

    @Nullable
    public static PhpParameter create(@NotNull ParameterList parameterList, int parameterNumber) {
        PhpParameter phpParameter = null;
        PsiElement[] parameters = parameterList.getParameters();
        int position = parameterNumber - 1;
        if (position < parameters.length && parameters[position] instanceof PhpPsiElement) {
            phpParameter = new PhpParameter(parameters[position], parameterNumber);
        }
        return phpParameter;
    }
}
