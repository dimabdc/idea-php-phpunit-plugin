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
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

/**
 * $foobar = $this->prophesize(Foobar::class);
 * $foobar->reveal();
 *
 * @author Daniel Espendiller <daniel@espendiller.net>
 */
public class RevealProphecyTypeProvider implements PhpTypeProvider3 {
    @Override
    public char getKey() {
        return '\u1537';
    }

    @Nullable
    @Override
    public PhpType getType(PsiElement element) {
        if(element instanceof MethodReference && "reveal".equals(((MethodReference) element).getName())) {
            PhpExpression classReference = ((MethodReference) element).getClassReference();
            if(classReference instanceof Variable || classReference instanceof MethodReference) {
                Method method = PhpPsiUtil.getParentByCondition(element, Method.INSTANCEOF);
                if(method != null) {
                    PhpClass containingClass = method.getContainingClass();

                    // filter phpunit test methods
                    if(containingClass != null && containingClass.getName().endsWith("Test")) {
                        String prophesize = ProphecyTypeUtil.getProphesizeSignatureFromTypes(classReference.getType().getTypes());
                        if(prophesize != null) {
                            return new PhpType().add("#" + this.getKey() + prophesize);
                        }
                    }
                }
            }
        }

        return null;
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
        PhpIndex phpIndex = PhpIndex.getInstance(project);

        String resolvedParameter = PhpTypeProviderUtil.getResolvedParameter(phpIndex, expression);
        if(resolvedParameter == null) {
            return null;
        }

        return phpIndex.getAnyByFQN(resolvedParameter);
    }
}
