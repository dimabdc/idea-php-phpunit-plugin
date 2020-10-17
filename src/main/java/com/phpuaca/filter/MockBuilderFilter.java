package com.phpuaca.filter;

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocMethod;
import com.jetbrains.php.lang.psi.elements.*;
import com.phpuaca.filter.util.ClassFinder;
import com.phpuaca.filter.util.Result;
import com.phpuaca.util.PhpArrayParameter;

import java.util.*;

public class MockBuilderFilter extends Filter {

    public MockBuilderFilter(FilterContext context) {
        super(context);

        allowModifier(PhpModifier.PUBLIC_ABSTRACT_DYNAMIC);
        allowModifier(PhpModifier.PUBLIC_IMPLEMENTED_DYNAMIC);
        allowModifier(PhpModifier.PROTECTED_ABSTRACT_DYNAMIC);
        allowModifier(PhpModifier.PROTECTED_IMPLEMENTED_DYNAMIC);

        MethodReference methodReference = context.getMethodReference();

        Result classFinderResult = (new ClassFinder()).find(methodReference);
        if (classFinderResult != null) {
            setPhpClass(classFinderResult.getPhpClass());
        }

        disallowMethod(PhpClass.CONSTRUCTOR);
        disallowMethod(PhpClass.DESTRUCTOR);

        if (Objects.equals(methodReference.getName(), "addMethods")) {
            describeMethods(getClassMethods(getPhpClass()));
        }

        if (Objects.equals(methodReference.getName(), "onlyMethods")) {
            allowMethods(getClassMethods(getPhpClass()));
        }

        ParameterList parameterList = methodReference.getParameterList();
        if (parameterList == null) {
            return;
        }

        PhpArrayParameter phpArrayParameter = PhpArrayParameter.create(parameterList, context.getFilterConfigItem().getParameterNumber());
        if (phpArrayParameter != null) {
            describeMethods(phpArrayParameter.getValues());
        }
    }

    private Method[] getClassMethods(PhpClass mockClass) {
        List<Method> methods = new ArrayList<>();
        do {
            if (mockClass.isInterface()) {
                Collections.addAll(methods, getInterfaceMethods(mockClass));
            }

            Collections.addAll(methods, getClassOwnMethods(mockClass));
            Collections.addAll(methods, getClassTraitMethods(mockClass));
        } while ((mockClass = mockClass.getSuperClass()) != null);

        return methods.toArray(Method.EMPTY);
    }

    private Method[] getInterfaceMethods(PhpClass mockClasses) {
        List<Method> methods = new ArrayList<>();
        for (PhpClass mockInterface: mockClasses.getImplementedInterfaces()) {
            Collections.addAll(methods, getClassMethods(mockInterface));
        }

        return methods.toArray(Method.EMPTY);
    }

    private Method[] getClassOwnMethods(PhpClass mockClass) {
        List<Method> methods = new ArrayList<>();
        for (Method method : mockClass.getOwnMethods()) {
            if (method instanceof PhpDocMethod) {
                continue;
            }
            methods.add(method);
        }

        return methods.toArray(Method.EMPTY);
    }

    private Method[] getClassTraitMethods(PhpClass mockClass) {
        List<Method> methods = new ArrayList<>();
        for (PhpClass mockTrait : mockClass.getTraits()) {
            Collections.addAll(methods, getClassMethods(mockTrait));
        }

        return methods.toArray(Method.EMPTY);
    }
}
