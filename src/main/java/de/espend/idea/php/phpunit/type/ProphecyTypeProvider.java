package de.espend.idea.php.phpunit.type;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider3;
import de.espend.idea.php.phpunit.type.utils.PhpTypeProviderUtil;
import de.espend.idea.php.phpunit.type.utils.ProphecyTypeUtil;
import de.espend.idea.php.phpunit.utils.PhpUnitPluginUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

/**
 * $this->prophesize(Foobar::class)->find()->will<caret>Return;
 *
 * @author Daniel Espendiller <daniel@espendiller.net>
 */
public class ProphecyTypeProvider implements PhpTypeProvider3 {
    private static char TRIM_KEY = '\u1536';

    @Override
    public char getKey() {
        return '\u1530';
    }

    @Nullable
    @Override
    public PhpType getType(PsiElement element) {
        if(element instanceof MethodReference) {
            Method method = PhpPsiUtil.getParentByCondition(element, Method.INSTANCEOF);
            if(method != null) {
                PhpClass containingClass = method.getContainingClass();

                // filter phpunit test methods
                if(containingClass != null && PhpUnitPluginUtil.isTestClassWithoutIndexAccess(containingClass)) {
                    String prophesize = ProphecyTypeUtil.getLocalProphesizeType((MethodReference) element);
                    if(prophesize != null) {
                        return new PhpType().add("#" + this.getKey() + prophesize + TRIM_KEY + ((MethodReference) element).getName());
                    }
                }
            }
        }

        return null;
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
        // SIGNATURE.METHOD_NAME
        String[] split = expression.split(String.valueOf(TRIM_KEY));
        if(split.length != 2) {
            return null;
        }

        PhpIndex phpIndex = PhpIndex.getInstance(project);

        String resolvedParameter = PhpTypeProviderUtil.getResolvedParameter(phpIndex, split[0]);
        if(resolvedParameter == null) {
            return null;
        }

        if(phpIndex.getAnyByFQN(resolvedParameter).stream().noneMatch(phpClass -> phpClass.findMethodByName(split[1]) != null)) {
            return null;
        }

        return phpIndex.getAnyByFQN("\\Prophecy\\Prophecy\\MethodProphecy");
    }
}
