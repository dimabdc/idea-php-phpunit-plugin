package de.espend.idea.php.phpunit.intention;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.phpunit.PhpUnitUtil;
import de.espend.idea.php.phpunit.utils.PhpUnitPluginUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 */
public class TestRunIntentionAction extends PsiElementBaseIntentionAction {
    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
        PsiElement context = getTestContextElement(psiElement);
        if (context != null) {
            PhpUnitPluginUtil.executeDebugRunner(psiElement);
        }
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
        return getTestContextElement(psiElement) != null;
    }

    @Nullable
    private PsiElement getTestContextElement(@NotNull PsiElement psiElement) {
        Method method = PhpPsiUtil.getParentByCondition(psiElement, Method.INSTANCEOF);
        if (method != null && PhpUnitUtil.isTestMethod(method)) {
            return method;
        }

        PhpClass phpClass = PhpPsiUtil.getParentByCondition(psiElement, PhpClass.INSTANCEOF);
        if (phpClass != null && PhpUnitUtil.isTestClass(phpClass)) {
            return phpClass;
        }

        return null;
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return "PHPUnit";
    }

    @NotNull
    @Override
    public String getText() {
        return "PHPUnit: Run Test";
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }
}
