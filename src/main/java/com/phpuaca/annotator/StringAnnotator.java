package com.phpuaca.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocMethod;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.phpuaca.filter.Filter;
import com.phpuaca.filter.FilterFactory;
import com.phpuaca.helper.AvailabilityHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StringAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
        AvailabilityHelper availabilityHelper = new AvailabilityHelper();

        if (!availabilityHelper.checkScope(psiElement)) {
            return;
        }

        Filter filter = FilterFactory.getInstance().getFilter(psiElement);
        if (filter == null) {
            return;
        }

        PhpClass phpClass = filter.getPhpClass();
        if (phpClass == null) {
            return;
        }

        String name = StringUtil.unquoteString(psiElement.getText());
        Method method = findMethod(phpClass, name);
        if (method == null) {
            if (Objects.equals(filter.getPhpMethodName(), "addMethods")) {
                return;
            }

            if (filter.isMethodAllowed(name) && Objects.equals(filter.getPhpMethodName(), "method")) {
                return;
            }

            annotationHolder.newAnnotation(HighlightSeverity.WARNING, "Method '" + name + "' not found in class " + phpClass.getName()).create();
        } else {
            boolean isMethodAllowed = filter.isMethodAllowed(method);
            boolean isAddMethods = Objects.equals(filter.getPhpMethodName(), "addMethods");
            if ((!isMethodAllowed && !isAddMethods) || (isMethodAllowed && isAddMethods)) {
                annotationHolder.newAnnotation(HighlightSeverity.WARNING, "Method '" + name + "' is not allowed to use here").create();
            }
        }
    }

    @Nullable
    private Method findMethod(PhpClass phpClass, String name) {
        do {
            Method method = phpClass.findOwnMethodByName(name);
            if (method != null && !(method instanceof PhpDocMethod)) {
                return method;
            }

            for (PhpClass trait : phpClass.getTraits()) {
                method = findMethod(trait, name);
                if (method != null) {
                    return method;
                }
            }
        } while ((phpClass = phpClass.getSuperClass()) != null);

        return null;
    }
}
