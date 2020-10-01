package com.phpuaca.filter;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FilterConfig {
    Map<String, FilterConfigGroup> config;

    public FilterConfig() {
        config = new HashMap<>();
    }

    public FilterConfig(FilterConfig filterConfig) {
        config = filterConfig.config;
    }

    public FilterConfig add(FilterConfigItem filterConfigItem) {
        FilterConfigGroup group = getGroup(filterConfigItem);
        group.add(filterConfigItem);
        return this;
    }

    @Nullable
    public FilterConfigItem getItem(String className, String methodName) {
        FilterConfigGroup group = getGroup(className);
        return group.getItem(methodName);
    }

    @Nullable
    public FilterConfigItem getItem(String methodName) {
        for (FilterConfigGroup entry : config.values()) {
            FilterConfigItem item = entry.getItem(methodName);
            if (item != null) {
                return item;
            }
        }

        return null;
    }

    private FilterConfigGroup getGroup(FilterConfigItem filterConfigItem) {
        String className = filterConfigItem.getClassName();
        FilterConfigGroup group = getGroup(className);
        config.putIfAbsent(className, group);

        return group;
    }

    public FilterConfigGroup getGroup(String className) {
        FilterConfigGroup group = config.get(className);
        if (null == group) {
            group = new FilterConfigGroup();
        }

        return group;
    }
}
