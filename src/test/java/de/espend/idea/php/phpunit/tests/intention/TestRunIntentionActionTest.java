package de.espend.idea.php.phpunit.tests.intention;

import de.espend.idea.php.phpunit.tests.PhpUnitLightCodeInsightFixtureTestCase;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 * @see de.espend.idea.php.phpunit.intention.TestRunIntentionAction
 */
public class TestRunIntentionActionTest extends PhpUnitLightCodeInsightFixtureTestCase {
    public void setUp() throws Exception {
        super.setUp();
        myFixture.copyFileToProject("classes.php");
    }

    public String getTestDataPath() {
        return "src/test/java/de/espend/idea/php/phpunit/tests/intention/fixtures";
    }

    public void testThatIntentionIsAvailableForClass() {
        configureByText(
            "<?php\n class Foo extends \\PHPUnit\\Framework\\TestCase {<caret>}"
        );
        assertIntentionIsAvailable("PHPUnit: Run Test");
    }

    public void testThatIntentionIsAvailableForMethod() {
        configureByText(
            "<?php\n class Foo extends \\PHPUnit\\Framework\\TestCase { function testFoo() { <caret>} }"
        );
        assertIntentionIsAvailable("PHPUnit: Run Test");
    }
}