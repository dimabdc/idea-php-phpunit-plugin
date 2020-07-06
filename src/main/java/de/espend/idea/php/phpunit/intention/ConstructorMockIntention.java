package de.espend.idea.php.phpunit.intention;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import de.espend.idea.php.phpunit.utils.PhpElementsUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 */
public class ConstructorMockIntention extends PsiElementBaseIntentionAction {
    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
        NewExpression newExpression = getScopeForOperation(psiElement);
        if(newExpression != null) {
            ClassReference classReference = newExpression.getClassReference();
            if (classReference != null) {
                String fqn = classReference.getFQN();

                for (PhpClass phpClass : PhpIndex.getInstance(project).getAnyByFQN(fqn)) {
                    Method constructor = phpClass.getConstructor();

                    // first constructor wins on non unique class names
                    if(constructor == null) {
                        continue;
                    }

                    // execute string insertions and stop iteration
                    new MyConstructorCommandActionArgument(
                        psiElement,
                        PsiTreeUtil.getChildOfType(newExpression, ParameterList.class),
                        constructor,
                        newExpression,
                        editor
                    ).execute();

                    return;
                }
            }
        }
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
        NewExpression newExpression = getScopeForOperation(psiElement);
        if(newExpression == null) {
            return false;
        }

        PhpClass phpClass = PsiTreeUtil.getParentOfType(psiElement, PhpClass.class);
        if(phpClass == null) {
            return false;
        }

        return PhpElementsUtil.isInstanceOf(phpClass, "\\PHPUnit\\Framework\\TestCase");
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
        return "PHPUnit: Add constructor mocks";
    }

    @Nullable
    private NewExpression getScopeForOperation(@NotNull PsiElement psiElement) {
        // $foo = new Foo<caret>bar();
        NewExpression newExpression = PsiTreeUtil.getParentOfType(psiElement, NewExpression.class);

        if(newExpression == null) {
            // scope outside method reference chaining
            // $f<caret>oo = new Foobar();
            PsiElement variable = psiElement.getParent();
            if(variable instanceof Variable) {
                PsiElement assignmentExpression = variable.getParent();
                if(assignmentExpression instanceof AssignmentExpression) {
                    newExpression = PsiTreeUtil.getChildOfAnyType(assignmentExpression, NewExpression.class);
                }
            }
        }

        return newExpression;
    }

    /**
     * new Foobar($this->createMock(Foobar::class))
     */
    private static class MyConstructorCommandActionArgument extends WriteCommandAction.Simple {
        @NotNull
        private final PsiElement scope;

        @Nullable
        private final ParameterList parameterList;

        @NotNull
        private final Method method;

        @NotNull
        private final NewExpression newExpression;

        @NotNull
        private final Editor editor;

        private MyConstructorCommandActionArgument(@NotNull PsiElement scope, @Nullable ParameterList parameterList, @NotNull Method method, @NotNull NewExpression newExpression, @NotNull Editor editor) {
            super(scope.getProject(), scope.getContainingFile());
            this.scope = scope;
            this.parameterList = parameterList;
            this.method = method;
            this.newExpression = newExpression;
            this.editor = editor;
        }

        @Override
        protected void run() throws Throwable {

            // current parameter state
            PsiElement[] parameters = parameterList != null ? parameterList.getParameters() : new PsiElement[0];
            int length = parameters.length;

            // pre insert "use imports"
            int pos = 0;
            List<String> classes = new ArrayList<>();
            for (Parameter parameter : method.getParameters()) {
                String className = parameter.getDeclaredType().toString();

                if(pos++ < length) {
                    continue;
                }

                boolean primitiveType = PhpType.isPrimitiveType(className);
                if(primitiveType) {
                    classes.add("\\" + className);
                } else {
                    // try import and get class name result; result can also be an alias
                    classes.add(PhpElementsUtil.insertUseIfNecessary(newExpression, className));
                }
            }

            PsiDocumentManager.getInstance(scope.getProject())
                .doPostponedOperationsAndUnblockDocument(editor.getDocument());

            PsiDocumentManager.getInstance(scope.getProject())
                .commitDocument(editor.getDocument());

            List<String> collect = classes
                .stream()
                .map(type -> {
                    // PrimitiveType
                    if(PhpType.isPrimitiveType(type)) {
                        String s1 = "\\" + StringUtils.stripStart(type, "\\");

                        if(s1.equalsIgnoreCase("\\int")) {
                            return "-1";
                        } else if(s1.equalsIgnoreCase("\\bool") || s1.equalsIgnoreCase("\\boolean")) {
                            return "true";
                        }

                        // fallback
                        return "'?'";
                    }

                    return String.format("$this->createMock(%s::class)", type);
                })
                .collect(Collectors.toList());

            String insert = StringUtils.join(collect, ", ");

            // condition for new Foobar() and new Foobar
            int startOffset;
            if(parameterList != null) {
                startOffset = parameterList.getTextRange().getStartOffset();

                // we already have a parameter so append string
                if(length > 0) {
                    PsiElement parameter = parameters[parameters.length - 1];
                    startOffset = parameter.getTextRange().getEndOffset();

                    insert = ", " + insert;
                }
            } else {
                // new Foobar we need wrap parameter with "()"
                startOffset = newExpression.getTextRange().getEndOffset();
                insert = "(" + insert + ")";
            }

            editor.getDocument().insertString(startOffset, insert);

            PsiElement statement = newExpression.getParent();

            CodeStyleManager.getInstance(scope.getProject()).reformatText(
                newExpression.getContainingFile(),
                statement.getTextRange().getStartOffset(),
                statement.getTextRange().getEndOffset() + insert.length()
            );
        }
    }
}
