package com.phpuaca.filter;

import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpModifier;
import com.phpuaca.filter.util.ClassFinder;
import com.phpuaca.filter.util.Result;
import com.phpuaca.util.PhpArrayParameter;

import java.util.Objects;

public class MockBuilderFilter extends Filter {

    public MockBuilderFilter(FilterContext context) {
        super(context);

        allowMethods();
        allowModifier(PhpModifier.PUBLIC_ABSTRACT_DYNAMIC);
        allowModifier(PhpModifier.PUBLIC_IMPLEMENTED_DYNAMIC);
        allowModifier(PhpModifier.PROTECTED_ABSTRACT_DYNAMIC);
        allowModifier(PhpModifier.PROTECTED_IMPLEMENTED_DYNAMIC);

        MethodReference methodReference = context.getMethodReference();

        Result classFinderResult = (new ClassFinder()).find(methodReference);
        if (classFinderResult != null) {
            setPhpClass(classFinderResult.getPhpClass());
        }

        allowMethods();
        disallowMethod(PhpClass.CONSTRUCTOR);
        disallowMethod(PhpClass.DESTRUCTOR);

        PhpClass phpclass = getPhpClass();

        if (Objects.equals(methodReference.getName(), "addMethods")) {
            do {
                describeMethods(phpclass.getOwnMethods());
            } while ((phpclass = phpclass.getSuperClass()) != null);
        } else {
            ParameterList parameterList = methodReference.getParameterList();
            if (parameterList != null) {
                PhpArrayParameter phpArrayParameter = PhpArrayParameter.create(parameterList, context.getFilterConfigItem().getParameterNumber());
                if (phpArrayParameter != null) {
                    describeMethods(phpArrayParameter.getValues());
                }
            }
        }
    }
}
