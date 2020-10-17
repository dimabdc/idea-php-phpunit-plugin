package com.phpuaca.filter;

import com.jetbrains.php.lang.psi.elements.MethodReference;
import org.jetbrains.annotations.NotNull;

public class FilterContext {

    private final FilterConfigItem filterConfigItem;
    private final MethodReference methodReference;

    public FilterContext(@NotNull FilterConfigItem filterConfigItem, @NotNull MethodReference methodReference) {
        this.filterConfigItem = filterConfigItem;
        this.methodReference = methodReference;
    }

    @NotNull
    public FilterConfigItem getFilterConfigItem() {
        return filterConfigItem;
    }

    @NotNull
    public MethodReference getMethodReference() {
        return methodReference;
    }
}
