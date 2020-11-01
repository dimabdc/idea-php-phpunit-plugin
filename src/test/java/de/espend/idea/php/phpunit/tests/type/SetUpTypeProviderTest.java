package de.espend.idea.php.phpunit.tests.type;

import com.intellij.patterns.PlatformPatterns;
import com.jetbrains.php.lang.psi.elements.Method;
import de.espend.idea.php.phpunit.tests.PhpUnitLightCodeInsightFixtureTestCase;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 * @see de.espend.idea.php.phpunit.type.SetUpTypeProvider
 */
public class SetUpTypeProviderTest extends PhpUnitLightCodeInsightFixtureTestCase {
    public void setUp() throws Exception {
        super.setUp();
        myFixture.copyFileToProject("classes.php");
        myFixture.copyFileToProject("ProphecyTypeProvider.php");
        myFixture.copyFileToProject("SetUpTypeProvider.php");
    }

    public String getTestDataPath() {
        return "src/test/java/de/espend/idea/php/phpunit/tests/type/fixtures";
    }

    public void testThatSetUpTypesForFieldReferencesAreProvided() {
        configureByText(
            "<?php" +
            "    class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "\n" +
            "        private $fake;\n" +
            "\n" +
            "        public function setUp()\n" +
            "        {\n" +
            "            $this->fake = $this->prophesize(\\Bar::class);\n" +
            "        }\n" +
            "\n" +
            "        public function itShouldDoFoobar()\n" +
            "        {\n" +
            "            $this->fake->getFo<caret>obar();\n" +
            "        }\n" +
            "    }"
        );
        assertPhpReferenceResolveTo(PlatformPatterns.psiElement(Method.class).withName("getFoobar"));
    }

    public void _testThatSetUpTypesForFieldReferencesAreProvidedForCreateMock() {
        // @TODO: index access problems prevents a stable test?

        configureByText(
            "<?php" +
            "    class FooBarTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "\n" +
            "        private $fake;\n" +
            "\n" +
            "        public function setUp()\n" +
            "        {\n" +
            "            $this->fake = $this->createMock(\\Bar::class);\n" +
            "        }\n" +
            "\n" +
            "        public function itShouldDoFoobar()\n" +
            "        {\n" +
            "            $this->fake->getFo<caret>obar();\n" +
            "        }\n" +
            "    }"
        );
        assertPhpReferenceResolveTo(PlatformPatterns.psiElement(Method.class).withName("getFoobar"));
    }

    public void testThatSetUpTypesForFieldReferencesWithMultipleAssignmentsAreProvided() {
        configureByText(
            "<?php" +
            "    class FooBarBarTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "\n" +
            "        private $fake;\n" +
            "\n" +
            "        public function setUp()\n" +
            "        {\n" +
            "            $this->fake = $this->prophesize(\\Bar::class);\n" +
            "        }\n" +
            "\n" +
            "        public function itShouldDoFoobar()\n" +
            "        {\n" +
            "            $this->fake->getFo<caret>obar();\n" +
            "        }\n" +
            "    }"
        );
        assertPhpReferenceResolveTo(PlatformPatterns.psiElement(Method.class).withName("getFoobar"));
    }
}
