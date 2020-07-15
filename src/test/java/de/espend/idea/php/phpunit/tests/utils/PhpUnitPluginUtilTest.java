package de.espend.idea.php.phpunit.tests.utils;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.Function;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import de.espend.idea.php.phpunit.tests.PhpUnitLightCodeInsightFixtureTestCase;
import de.espend.idea.php.phpunit.utils.PhpUnitPluginUtil;

import java.util.Objects;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 */
public class PhpUnitPluginUtilTest extends PhpUnitLightCodeInsightFixtureTestCase {

    public void testIsTestClassWithoutIndexAccess() {
        assertTrue(PhpUnitPluginUtil.isTestClassWithoutIndexAccess(
            Objects.requireNonNull(PhpPsiElementFactory.createFromText(getProject(), PhpClass.class, "<?php class FooTest {}"))
        ));

        assertTrue(PhpUnitPluginUtil.isTestClassWithoutIndexAccess(
            Objects.requireNonNull(PhpPsiElementFactory.createFromText(getProject(), PhpClass.class, "<?php class FooTest extends \\PHPUnit\\Framework\\TestCase {}"))
        ));

        assertTrue(PhpUnitPluginUtil.isTestClassWithoutIndexAccess(
            Objects.requireNonNull(PhpPsiElementFactory.createFromText(getProject(), PhpClass.class, "<?php class FooTest extends PHPUnit_Framework_TestCase {}"))
        ));

        assertTrue(PhpUnitPluginUtil.isTestClassWithoutIndexAccess(
            Objects.requireNonNull(PhpPsiElementFactory.createFromText(getProject(), PhpClass.class, "<?php class FooTest extends \\Symfony\\Bundle\\FrameworkBundle\\Test\\WebTestCase {}"))
        ));
    }

    public void testThatInsertExpectedExceptionForNonExistingDocBlock() {
        configureByText("<?php\n" +
            " function test()" +
            " {" +
            " }"
        );
        PsiFile psiFile = myFixture.getFile();

        Document document = PsiDocumentManager.getInstance(getProject()).getDocument(psiFile);
        Function function = PsiTreeUtil.findChildOfType(psiFile, Function.class);

        WriteCommandAction.writeCommandAction(getProject()).withName("PHPUnit: ExpectedException Insert").run(() -> {
            PhpUnitPluginUtil.insertExpectedException(document, function, "Foobar\\Foobar");
        });

        assertTrue(psiFile.getText().contains("@expectedException \\Foobar\\Foobar"));
    }

    public void testInsertExpectedExceptionForDocBlockUpdate() {
        configureByText("<?php\n" +
            "/**\n" +
            " * @Foo\n" +
            " * @return Foo\n" +
            " */\n" +
            "function test()\n" +
            "{\n" +
            "}\n"
        );
        PsiFile psiFile = myFixture.getFile();

        Document document = PsiDocumentManager.getInstance(getProject()).getDocument(psiFile);
        Function function = PsiTreeUtil.findChildOfType(psiFile, Function.class);

        WriteCommandAction.writeCommandAction(getProject()).withName("PHPUnit: ExpectedException Insert").run(() -> {
            PhpUnitPluginUtil.insertExpectedException(document, function, "Foobar\\Foobar");
        });

        assertTrue(psiFile.getText().contains("@expectedException \\Foobar\\Foobar"));
    }
}
