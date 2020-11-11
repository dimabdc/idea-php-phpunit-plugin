package de.espend.idea.php.phpunit.tests.type;

import de.espend.idea.php.phpunit.tests.PhpUnitLightCodeInsightFixtureTestCase;
import de.espend.idea.php.phpunit.type.ProphecyArgumentTypeProvider;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 * @see ProphecyArgumentTypeProvider
 */
public class ProphecyArgumentTypeProviderTest extends PhpUnitLightCodeInsightFixtureTestCase {
    public void setUp() throws Exception {
        super.setUp();
        myFixture.copyFileToProject("ProphecyArgumentTypeProvider.php");
    }

    public String getTestDataPath() {
        return "src/test/java/de/espend/idea/php/phpunit/tests/type/fixtures";
    }

    public void testThatProphecyArgumentsProvideTypesForPrimitives() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "        public function testFoobar()\n" +
            "        {\n" +
            "            $foo = $this->prophesize(Foo::class);\n" +
            "            $foo->getBar(\\Prophecy\\Argument::a<caret>ny());\n" +
            "        }\n" +
            "    }"
        );
        assertMethodContainsTypes("\\array");

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
            "            $this->foo->getBar(\\Prophecy\\Argument::i<caret>s());\n" +
            "        }\n" +
            "    }"
        );
        assertMethodContainsTypes("\\array");

        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "        public function testFoobar()\n" +
            "        {\n" +
            "            $foo = $this->prophesize(Foo::class);\n" +
            "            $foo->getBar(\\Prophecy\\Argument::unk<caret>nown());\n" +
            "        }\n" +
            "    }"
        );
        assertMethodContainsTypes("\\array");
    }

    public void testThatProphecyArgumentsProvideTypesForClasses() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "        public function testFoobar()\n" +
            "        {\n" +
            "            $foo = $this->prophesize(Foo::class);\n" +
            "            $foo->getBar(\\Prophecy\\Argument::any(), \\Prophecy\\Argument::a<caret>ny());\n" +
            "        }\n" +
            "    }"
        );
        assertMethodContainsTypes("\\Foo");
    }
}
