package com.phpuaca.tests.completion;

import de.espend.idea.php.phpunit.tests.PhpUnitLightCodeInsightFixtureTestCase;

public class StringCompletionContributorTest extends PhpUnitLightCodeInsightFixtureTestCase {
    public void setUp() throws Exception {
        super.setUp();
        myFixture.copyFileToProject("StringCompletionContributor.php");
    }

    public String getTestDataPath() {
        return "src/test/java/com/phpuaca/tests/completion/fixtures";
    }

    public void testCompleteSetMethods() {
        configureByText(
            "<?php\n" +
            "class Foo extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "   public function foobar()\n" +
            "   {\n" +
            "       $foo = $this->getMockBuilder(\\Foo\\Bar::class)\n" +
            "           ->setMethods(['<caret>'])\n" +
            "   }\n" +
            "}"
        );
        assertCompletionContains("getFoobar", "getFoobaz", "getFoobazbar");

        configureByText(
            "<?php\n" +
            "class Foo extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "   public function foobar()\n" +
            "   {\n" +
            "       $foo = $this->getMockBuilder(\\Foo\\Bar::class)\n" +
            "           ->setMethods([\n" +
            "               'getFoobar'\n" +
            "               'getFoobaz'\n" +
            "               'getFoobazbar'\n" +
            "               '<caret>'\n" +
            "           ])\n" +
            "   }\n" +
            "}"
        );
        assertCompletionNotContains("getFoobar", "getFoobaz", "getFoobazbar");
    }

    public void testCompleteOnlyMethods() {
        configureByText(
            "<?php\n" +
            "class Foo extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "   public function foobar()\n" +
            "   {\n" +
            "       $foo = $this->getMockBuilder(\\Foo\\Bar::class)\n" +
            "           ->onlyMethods(['<caret>'])\n" +
            "   }\n" +
            "}"
        );
        assertCompletionContains("getFoobar", "getFoobaz");
        assertCompletionNotContains("getFoobazbar");

        configureByText(
            "<?php\n" +
            "class Foo extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "   public function foobar()\n" +
            "   {\n" +
            "       $foo = $this->getMockBuilder(\\Foo\\Bar::class)\n" +
            "           ->setMethods([\n" +
            "               'getFoobar'\n" +
            "               'getFoobaz'\n" +
            "               '<caret>'\n" +
            "           ])\n" +
            "   }\n" +
            "}"
        );
        assertCompletionNotContains("getFoobar", "getFoobaz", "getFoobazbar");
    }

    public void testCompleteAddMethods() {
        configureByText(
            "<?php\n" +
            "class Foo extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "   public function foobar()\n" +
            "   {\n" +
            "       $foo = $this->getMockBuilder(\\Foo\\Bar::class)\n" +
            "           ->addMethods(['<caret>'])\n" +
            "   }\n" +
            "}"
        );
        assertCompletionContains("getFoobazbar");
        assertCompletionNotContains("getFoobar", "getFoobaz");

        configureByText(
            "<?php\n" +
            "class Foo extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "   public function foobar()\n" +
            "   {\n" +
            "       $foo = $this->getMockBuilder(\\Foo\\Bar::class)\n" +
            "           ->addMethods([\n" +
            "               'getFoobazbar'\n" +
            "               '<caret>'\n" +
            "           ])\n" +
            "   }\n" +
            "}"
        );
        assertCompletionNotContains("getFoobar", "getFoobaz", "getFoobazbar");
    }
}
