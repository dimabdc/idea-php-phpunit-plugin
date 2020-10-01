package com.phpuaca.filter;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FilterConfigGroup {
    Map<String, FilterConfigItem> config;

    public FilterConfigGroup() {
        config = new HashMap<>();
    }

    public FilterConfigGroup add(FilterConfigItem filterConfigItem) {
        config.put(filterConfigItem.getMethodName(), filterConfigItem);
        return this;
    }

    @Nullable
    public FilterConfigItem getItem(String methodName) {
        return config.get(methodName);
    }
}
