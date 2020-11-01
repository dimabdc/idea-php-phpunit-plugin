package com.phpuaca.filter;

public class FilterConfigItem {

    private final String className;
    private final String methodName;
    private final int parameterNumber;
    private final Class<?> filterClass;

    public FilterConfigItem(String className, String methodName, int parameterNumber, Class<?> filterClass) {
        this.className = className;
        this.methodName = methodName;
        this.parameterNumber = parameterNumber;
        this.filterClass = filterClass;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getParameterNumber() {
        return parameterNumber;
    }

    public Class<?> getFilterClass() {
        return filterClass;
    }
}
