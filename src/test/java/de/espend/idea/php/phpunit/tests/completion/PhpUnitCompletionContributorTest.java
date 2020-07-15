package de.espend.idea.php.phpunit.tests.completion;

import de.espend.idea.php.phpunit.tests.PhpUnitLightCodeInsightFixtureTestCase;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 * @see de.espend.idea.php.phpunit.completion.PhpUnitCompletionContributor
 */
public class PhpUnitCompletionContributorTest extends PhpUnitLightCodeInsightFixtureTestCase {
    public void setUp() throws Exception {
        super.setUp();
        myFixture.copyFileToProject("PhpUnitCompletionContributor.php");
    }

    public String getTestDataPath() {
        return "src/test/java/de/espend/idea/php/phpunit/tests/completion/fixtures";
    }

    public void testThatChainingCreateMockProvidesMethodCompletion() {
        configureByText(
            "<?php\n" +
            "class Foo extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "   public function foobar()\n" +
            "   {\n" +
            "       $foo = $this->createMock(\\Foo\\Bar::class);\n" +
            "       $foo->method('<caret>')\n" +
            "   }\n" +
            "}"
        );
        assertCompletionContains("getFoobar");

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
            "       $this->foo->method('<caret>');\n" +
            "   }\n" +
            "}"
        );
        assertCompletionContains("getFoobar");

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
            "       $this->foo->method(null)->method('<caret>');\n" +
            "   }\n" +
            "}"
        );
        assertCompletionContains("getFoobar");
    }
}
