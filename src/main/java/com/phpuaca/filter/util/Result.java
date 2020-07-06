package com.phpuaca.filter.util;

import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;

public class Result {
    private final PhpClass phpClass;
    private final int parameterNumber;

    public Result(@NotNull PhpClass phpClass, int parameterNumber) {
        this.phpClass = phpClass;
        this.parameterNumber = parameterNumber;
    }

    public PhpClass getPhpClass() {
        return phpClass;
    }

    public int getParameterNumber() {
        return parameterNumber;
    }
}
