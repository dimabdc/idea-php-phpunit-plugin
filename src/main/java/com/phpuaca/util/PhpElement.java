package com.phpuaca.util;

import com.jetbrains.php.lang.psi.elements.MethodReference;
import org.jetbrains.annotations.Nullable;

public interface PhpElement {
    @Nullable MethodReference findClosestAssignment();
}
