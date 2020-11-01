package de.espend.idea.php.phpunit.tests.references;

import com.intellij.patterns.PlatformPatterns;
import com.jetbrains.php.lang.psi.elements.Method;
import de.espend.idea.php.phpunit.tests.PhpUnitLightCodeInsightFixtureTestCase;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 */
public class PhpUnitReferenceContributorTest extends PhpUnitLightCodeInsightFixtureTestCase {
    public void setUp() throws Exception {
        super.setUp();
        myFixture.copyFileToProject("PhpUnitReferenceContributor.php");
    }

    public String getTestDataPath() {
        return "src/test/java/de/espend/idea/php/phpunit/tests/references/fixtures";
    }

    public void testThatReferencesForClassMethodAreProvided() {
        configureByText(
            "<?php\n" +
            "class Foo extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "" +
            "   public function setUp()\n" +
            "   {\n" +
            "       $this->foo = $this->createMock('Foo\\Bar');\n" +
            "   }\n" +
            "" +
            "   public function foobar()\n" +
            "   {\n" +
            "       $this->foo->method('getFoo<caret>bar');\n" +
            "   }\n" +
            "}"
        );
        assertReferencesMatch(PlatformPatterns.psiElement(Method.class).withName("getFoobar"));
    }
}