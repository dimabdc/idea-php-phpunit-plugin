package com.phpuaca.filter;

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocProperty;
import com.jetbrains.php.lang.psi.elements.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

abstract public class Filter {
    protected MethodReference phpMethod;

    private boolean isMethodsAllowed = false;
    private boolean isFieldsAllowed = false;

    private final List<String> allowedMethods = new ArrayList<>();
    private final List<String> allowedFields = new ArrayList<>();
    private final List<String> allowedModifiers = new ArrayList<>();
    private final List<String> disallowedMethods = new ArrayList<>();
    private final List<String> describedMethods = new ArrayList<>();

    private PhpClass phpClass;

    public Filter(@NotNull FilterContext context) {
        phpMethod = context.getMethodReference();
        allowMethods();
    }

    public String getPhpMethodName() {
        return phpMethod.getName();
    }

    public MethodReference getPhpMethodReference() {
        return phpMethod;
    }

    public void allowMethod(String methodName) {
        disallowedMethods.remove(methodName);
        allowedMethods.add(methodName);
    }

    public void disallowMethod(String methodName) {
        allowedMethods.remove(methodName);
        disallowedMethods.add(methodName);
    }

    public void describeMethod(String methodName) {
        describedMethods.add(methodName);
    }

    public void allowModifier(String modifierName) {
        allowedModifiers.add(modifierName);
    }

    public void allowModifier(@NotNull PhpModifier modifier) {
        allowModifier(modifier.toString());
    }

    public void allowMethods() {
        isMethodsAllowed = true;
    }

    public void allowMethods(@NotNull Method[] methods) {
        allowMethods(Arrays.stream(methods)
            .map(Method::getName)
            .collect(Collectors.toList()));
    }

    public void allowMethods(@NotNull List<String> methodNames) {
        for (String methodName : methodNames) {
            allowMethod(methodName);
        }
    }

    public void describeMethods(@NotNull Method[] methods) {
        describeMethods(Arrays.stream(methods)
            .map(Method::getName)
            .collect(Collectors.toList()));
    }

    public void describeMethods(@NotNull List<String> methodNames) {
        for (String methodName : methodNames) {
            describeMethod(methodName);
        }
    }

    public void allowFields() {
        isFieldsAllowed = true;
    }

    public boolean isMethodAllowed(String methodName) {
        return isMethodsAllowed && !disallowedMethods.contains(methodName) && (allowedMethods.isEmpty() || allowedMethods.contains(methodName));
    }

    public boolean isMethodAllowed(@NotNull Method method) {
        return isMethodAllowed(method.getName()) && isModifierAllowed(method.getModifier());
    }

    public boolean isMethodDescribed(String methodName) {
        return describedMethods.contains(methodName);
    }

    public boolean isMethodDescribed(@NotNull Method method) {
        return isMethodDescribed(method.getName());
    }

    protected boolean isFieldAllowed(String fieldName) {
        return isFieldsAllowed && (allowedFields.isEmpty() || allowedFields.contains(fieldName));
    }

    public boolean isFieldAllowed(Field field) {
        return !(field instanceof PhpDocProperty) && isFieldAllowed(field.getName()) && isModifierAllowed(field.getModifier());
    }

    protected boolean isModifierAllowed(String modifierName) {
        return allowedModifiers.isEmpty() || allowedModifiers.contains(modifierName);
    }

    protected boolean isModifierAllowed(@NotNull PhpModifier modifier) {
        return isModifierAllowed(modifier.toString());
    }

    public void setPhpClass(PhpClass phpClass) {
        this.phpClass = phpClass;
    }

    public PhpClass getPhpClass() {
        return phpClass;
    }
}
