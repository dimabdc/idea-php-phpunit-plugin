package de.espend.idea.php.phpunit.type;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.containers.MultiMap;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.elements.impl.PhpClassImpl;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4;
import com.jetbrains.php.phpunit.PhpUnitUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Pipe all fields in setUp to make type them visible in PhpClass scope
 *
 * function setUp() { $this->foobar = .. }
 *
 * @author Daniel Espendiller <daniel@espendiller.net>
 */
public class SetUpTypeProvider implements PhpTypeProvider4 {

    private static final char TRIM_KEY = '\u1212';

    @Override
    public char getKey() {
        return '\u1589';
    }

    @Nullable
    @Override
    public PhpType getType(PsiElement element) {
        if(element instanceof FieldReference) {
            if (!PhpUnitUtil.isPhpUnitTestFile(element.getContainingFile())) {
                return null;
            }

            String variableName = ((FieldReference)element).getName();
            if (variableName == null) {
                return null;
            }

            PsiElement field = ((FieldReference)element).resolve();
            if (field != null) { // if field is declared in class
                return null;
            }

            PhpClass phpClass = PsiTreeUtil.getParentOfType(element, PhpClass.class);
            if (phpClass == null) {
                return null;
            }

            MultiMap<String, AssignmentExpression> accessMap = PhpClassImpl.getPhpUnitSetUpAssignmentsPerField(phpClass);
            if (!accessMap.containsKey(((FieldReference)element).getName())) {
                return null;
            }

            Collection<AssignmentExpression> expressions = accessMap.get(((FieldReference)element).getName());
            for (AssignmentExpression expression : expressions) {
                PhpPsiElement variable = expression.getVariable();
                if (variable instanceof FieldReference && variableName.equals(variable.getName())) {
                    return new PhpType().add(
                        "#" + this.getKey() + StringUtils.stripStart(phpClass.getFQN(), "\\") + TRIM_KEY + variableName
                    );
                }
            }
        }

        return null;
    }

    @Nullable
    @Override
    public PhpType complete(String s, Project project) {
        return null;
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String expression, Set<String> visited, int depth, Project project) {
        // split: CLASS|FIELD_NAME
        String[] split = expression.split(String.valueOf(TRIM_KEY));
        if(split.length != 2) {
            return null;
        }

        Collection<PhpNamedElement> phpNamedElements = new ArrayList<>();

        PhpIndex phpIndex = PhpIndex.getInstance(project);
        for (PhpClass phpClass : phpIndex.getAnyByFQN(split[0])) {
            if (!PhpUnitUtil.isTestClass(phpClass)) {
                continue;
            }

            MultiMap<String, AssignmentExpression> accessMap = PhpClassImpl.getPhpUnitSetUpAssignmentsPerField(phpClass);
            if (!accessMap.containsKey(split[1])) {
                continue;
            }

            Collection<AssignmentExpression> expressions = accessMap.get(split[1]);
            for (AssignmentExpression assignmentExpression : expressions) {
                PhpPsiElement variable = assignmentExpression.getVariable();
                if (variable instanceof FieldReference && split[1].equals(variable.getName())) {
                    Set<String> types = phpIndex.completeType(project, assignmentExpression.getType(), visited).getTypes();
                    for (String s : types) {
                        phpNamedElements.addAll(PhpType.isUnresolved(s) ? phpIndex.getBySignature(s, visited, depth) : phpIndex.getAnyByFQN(s));
                    }
                }
            }
        }

        return phpNamedElements;
    }
}
