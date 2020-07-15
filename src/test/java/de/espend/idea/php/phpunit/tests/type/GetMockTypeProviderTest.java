package de.espend.idea.php.phpunit.tests.type;

import com.intellij.patterns.PlatformPatterns;
import com.jetbrains.php.lang.psi.elements.Method;
import de.espend.idea.php.phpunit.tests.PhpUnitLightCodeInsightFixtureTestCase;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 * @see de.espend.idea.php.phpunit.type.GetMockTypeProvider
 */
public class GetMockTypeProviderTest extends PhpUnitLightCodeInsightFixtureTestCase {
    public void setUp() throws Exception {
        super.setUp();
        myFixture.copyFileToProject("GetMockBuilderTypeProvider.php");
    }

    public String getTestDataPath() {
        return "src/test/java/de/espend/idea/php/phpunit/tests/type/fixtures";
    }

    public void testGetMockGeneratorProvidesNavigation() {
        configureByText(
            "<?php\n" +
            "/** @var $t \\PHPUnit\\Framework\\TestCase */\n" +
            "$x = $t->getMockBuilder('Foo')->getMock()" +
            "$x->b<caret>ar();\n"
        );
        assertPhpReferenceResolveTo(PlatformPatterns.psiElement(Method.class).withName("bar"));
    }

    public void testChainMockGeneratorProvidesNavigation() {
        configureByText(
            "<?php\n" +
            "/** @var $t \\PHPUnit\\Framework\\TestCase */\n" +
            "$x = $t->getMockBuilder('Foo')" +
            "->disableOriginalConstructor()" +
            "->disableOriginalConstructor()" +
            "->getMock()" +
            "$x->b<caret>ar();\n"
        );
        assertPhpReferenceResolveTo(PlatformPatterns.psiElement(Method.class).withName("bar"));
    }

    public void testThatProphesizeForVariableWithStringClassIsResolved() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "        public function setUp()\n" +
            "        {\n" +
            "            $this->foo = $this->getMockBuilder(\\Foo::class);\n" +
            "        }\n" +
            "        public function testFoobar()\n" +
            "        {\n" +
            "            $this->foo->getMock()->b<caret>ar();\n" +
            "        }\n" +
            "    }"
        );
        assertPhpReferenceResolveTo(PlatformPatterns.psiElement(Method.class).withName("bar"));
    }
}
