package de.espend.idea.php.phpunit.tests.type;

import com.intellij.patterns.PlatformPatterns;
import com.jetbrains.php.lang.psi.elements.Method;
import de.espend.idea.php.phpunit.tests.PhpUnitLightCodeInsightFixtureTestCase;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 * @see de.espend.idea.php.phpunit.type.RevealProphecyTypeProvider
 */
public class RevealProphecyTypeProviderTest extends PhpUnitLightCodeInsightFixtureTestCase {
    public void setUp() throws Exception {
        super.setUp();
        myFixture.copyFileToProject("RevealProphecyTypeProvider.php");
    }

    public String getTestDataPath() {
        return "src/test/java/de/espend/idea/php/phpunit/tests/type/fixtures";
    }

    public void testThatRevealIsResolved() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "        public function testFoobar()\n" +
            "        {\n" +
            "            $foo = $this->prophesize(Foo::class);\n" +
            "            $foo->reveal()->getB<caret>ar();\n" +
            "        }\n" +
            "    }"
        );
        assertPhpReferenceResolveTo(PlatformPatterns.psiElement(Method.class).withName("getBar"));
    }

    public void testThatRevealIsResolvedForStringClass() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "        public function testFoobar()\n" +
            "        {\n" +
            "            $foo = $this->prophesize('Foo');\n" +
            "            $foo->reveal()->getB<caret>ar();\n" +
            "        }\n" +
            "    }"
        );
        assertPhpReferenceResolveTo(PlatformPatterns.psiElement(Method.class).withName("getBar"));
    }

    public void testThatRevealForPropertyIsResolved() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "        public function setUp()\n" +
            "        {\n" +
            "            $this->foo = $this->prophesize(Foo::class);\n" +
            "        }\n" +
            "        public function testFoobar()\n" +
            "        {\n" +
            "            $this->foo->reveal()->getB<caret>ar();\n" +
            "        }\n" +
            "    }"
        );
        assertPhpReferenceResolveTo(PlatformPatterns.psiElement(Method.class).withName("getBar"));
    }
}
