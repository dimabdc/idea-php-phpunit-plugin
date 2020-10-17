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
        assertCompletionContains("getFoobar", "getFoobaz", "getFoobazbar", "getFoo");

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
            "               'getFoo'\n" +
            "               '<caret>'\n" +
            "           ])\n" +
            "   }\n" +
            "}"
        );
        assertCompletionNotContains("getFoobar", "getFoobaz", "getFoobazbar", "getFoo");
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
        assertCompletionNotContains("getFoobazbar", "getFoo");

        configureByText(
            "<?php\n" +
            "class Foo extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "   public function foobar()\n" +
            "   {\n" +
            "       $foo = $this->getMockBuilder(\\Foo\\Bar::class)\n" +
            "           ->onlyMethods([\n" +
            "               'getFoobar'\n" +
            "               'getFoobaz'\n" +
            "               '<caret>'\n" +
            "           ])\n" +
            "   }\n" +
            "}"
        );
        assertCompletionNotContains("getFoobar", "getFoobaz", "getFoobazbar", "getFoo");
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
        assertCompletionContains("getFoobazbar", "getFoo");
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
            "               'getFoo'\n" +
            "               '<caret>'\n" +
            "           ])\n" +
            "   }\n" +
            "}"
        );
        assertCompletionNotContains("getFoobar", "getFoobaz", "getFoobazbar", "getFoo");
    }

    public void testCompleteSetMethodsInterface() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "   public function foobarTest()\n" +
            "   {\n" +
            "       $foo = $this->getMockBuilder(\\Bar\\Foo::class)\n" +
            "           ->setMethods(['<caret>'])\n" +
            "   }\n" +
            "}"
        );
        assertCompletionContains("getFoo", "getBar", "getBarFoo", "getBaz", "getBazBar", "getFooBar");

        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "   public function foobarTest()\n" +
            "   {\n" +
            "       $foo = $this->getMockBuilder(\\Bar\\Foo::class)\n" +
            "           ->setMethods([\n" +
            "               'getFoo'\n" +
            "               'getBar'\n" +
            "               'getBarFoo'\n" +
            "               'getBaz'\n" +
            "               'getBazBar'\n" +
            "               'getFooBar'\n" +
            "               '<caret>'\n" +
            "           ])\n" +
            "   }\n" +
            "}"
        );
        assertCompletionNotContains("getFoo", "getBar", "getBarFoo", "getBaz", "getBazBar", "getFooBar");
    }

    public void testCompleteOnlyMethodsInterface() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "   public function foobarTest()\n" +
            "   {\n" +
            "       $foo = $this->getMockBuilder(\\Bar\\Foo::class)\n" +
            "           ->onlyMethods(['<caret>'])\n" +
            "   }\n" +
            "}"
        );
        assertCompletionContains("getFoo", "getBar", "getBarFoo", "getBaz");
        assertCompletionNotContains("getBazBar", "getFooBar");

        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "   public function foobarTest()\n" +
            "   {\n" +
            "       $foo = $this->getMockBuilder(\\Bar\\Foo::class)\n" +
            "           ->setMethods([\n" +
            "               'getFoo'\n" +
            "               'getBar'\n" +
            "               'getBarFoo'\n" +
            "               'getBaz'\n" +
            "               '<caret>'\n" +
            "           ])\n" +
            "   }\n" +
            "}"
        );
        assertCompletionNotContains("getFoo", "getBar", "getBarFoo", "getBaz", "getBazBar", "getFooBar");
    }

    public void testCompleteAddMethodsInterface() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "   public function foobarTest()\n" +
            "   {\n" +
            "       $foo = $this->getMockBuilder(\\Bar\\Foo::class)\n" +
            "           ->addMethods(['<caret>'])\n" +
            "   }\n" +
            "}"
        );
        assertCompletionContains("getBazBar", "getFooBar");
        assertCompletionNotContains("getFoo", "getBar", "getBarFoo", "getBaz");

        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "   public function foobarTest()\n" +
            "   {\n" +
            "       $foo = $this->getMockBuilder(\\Bar\\Foo::class)\n" +
            "           ->addMethods([\n" +
            "               'getBazBar'\n" +
            "               'getFooBar'\n" +
            "               '<caret>'\n" +
            "           ])\n" +
            "   }\n" +
            "}"
        );
        assertCompletionNotContains("getBazBar", "getFooBar", "getFoo", "getBar", "getBarFoo", "getBaz");
    }
}
