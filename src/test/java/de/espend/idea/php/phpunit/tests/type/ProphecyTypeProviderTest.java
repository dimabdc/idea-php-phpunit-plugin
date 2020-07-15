package de.espend.idea.php.phpunit.tests.type;

import com.intellij.patterns.PlatformPatterns;
import com.jetbrains.php.lang.psi.elements.Method;
import de.espend.idea.php.phpunit.tests.PhpUnitLightCodeInsightFixtureTestCase;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 * @see de.espend.idea.php.phpunit.type.ProphecyTypeProvider
 */
public class ProphecyTypeProviderTest extends PhpUnitLightCodeInsightFixtureTestCase {
    public void setUp() throws Exception {
        super.setUp();
        myFixture.copyFileToProject("ProphecyTypeProvider.php");
    }

    public String getTestDataPath() {
        return "src/test/java/de/espend/idea/php/phpunit/tests/type/fixtures";
    }

    public void testThatProphesizeForVariableIsResolved() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "        public function testFoobar()\n" +
            "        {\n" +
            "            $foo = $this->prophesize(Foo::class);\n" +
            "            $foo->getBar()->will<caret>Return();\n" +
            "        }\n" +
            "    }"
        );
        assertPhpReferenceResolveTo(PlatformPatterns.psiElement(Method.class).withName("willReturn"));
    }

    public void testThatProphesizeForVariableIsResolvedForClosure() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "        public function testFoobar()\n" +
            "        {\n" +
            "            $foo = $this->prophesize(Foo::class);\n" +
            "            $closure = function() use ($class) {\n" +
            "               $foo->getBar()->will<caret>Return();\n" +
            "            };" +
            "        }\n" +
            "    }"
        );
        assertPhpReferenceResolveTo(PlatformPatterns.psiElement(Method.class).withName("willReturn"));
    }

    public void testThatProphesizeForVariableInPropertyIsResolved() {
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
            "            $this->foo->getBar()->will<caret>Return();\n" +
            "        }\n" +
            "    }"
        );
        assertPhpReferenceResolveTo(PlatformPatterns.psiElement(Method.class).withName("willReturn"));
    }

    public void testThatProphesizeForVariableWithStringClassIsResolved() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "        public function testFoobar()\n" +
            "        {\n" +
            "            $foo = $this->prophesize('Foo');\n" +
            "            $foo->getBar()->will<caret>Return();\n" +
            "        }\n" +
            "    }"
        );
        assertPhpReferenceResolveTo(PlatformPatterns.psiElement(Method.class).withName("willReturn"));
    }

    public void testThatProphesizeForVariableIsNotResolvedForUnknownMethods() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "        public function testFoobar()\n" +
            "        {\n" +
            "            $foo = $this->prophesize(Foo::class);\n" +
            "            $foo->unknown()->will<caret>Return();\n" +
            "        }\n" +
            "    }"
        );
        assertPhpReferenceNotResolveTo(PlatformPatterns.psiElement());
    }

    public void testThatProphesizeForMethodReferenceIsResolved() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "        public function testFoobar()\n" +
            "        {\n" +
            "            $foo = $this->prophesize(Foo::class)->getBar()->will<caret>Return();\n" +
            "        }\n" +
            "    }"
        );
        assertPhpReferenceResolveTo(PlatformPatterns.psiElement(Method.class).withName("willReturn"));
    }
}
