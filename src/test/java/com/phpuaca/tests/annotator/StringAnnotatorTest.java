package com.phpuaca.tests.annotator;

import de.espend.idea.php.phpunit.tests.PhpUnitLightCodeInsightFixtureTestCase;

public class StringAnnotatorTest extends PhpUnitLightCodeInsightFixtureTestCase {
    public void setUp() throws Exception {
        super.setUp();
        myFixture.copyFileToProject("StringAnnotator.php");
    }

    public String getTestDataPath() {
        return "src/test/java/com/phpuaca/tests/annotator/fixtures";
    }

    public void testSetMethodsAnnotator() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "" +
            "   public function foobar()\n" +
            "   {\n" +
            "       $this->getMockBuilder(\\C::class)\n" +
            "           ->setMethods([\n" +
            "               'getC',\n" +
            "               'getB',\n" +
            "               'getA',\n" +
            "               'getTA',\n" +
            "               'getTB',\n" +
            "               <warning descr=\"Method 'getTC' not found in class C\">'getTC'</warning>,\n" +
            "               <warning descr=\"Method 'getTD' not found in class C\">'getTD'</warning>,\n" +
            "           ])\n" +
            "           ->getMock();\n" +
            "   }\n" +
            "}"
        );
        myFixture.checkHighlighting();
    }

    public void testOnlyMethodsAnnotator() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "" +
            "   public function foobar()\n" +
            "   {\n" +
            "       $this->getMockBuilder(\\C::class)\n" +
            "           ->onlyMethods([\n" +
            "               'getC',\n" +
            "               'getB',\n" +
            "               'getA',\n" +
            "               'getTA',\n" +
            "               'getTB',\n" +
            "               <warning descr=\"Method 'getTC' not found in class C\">'getTC'</warning>,\n" +
            "               <warning descr=\"Method 'getTD' not found in class C\">'getTD'</warning>,\n" +
            "           ])\n" +
            "           ->getMock();\n" +
            "   }\n" +
            "}"
        );
        myFixture.checkHighlighting();
    }

    public void testAddMethodsAnnotator() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "{\n" +
            "" +
            "   public function foobar()\n" +
            "   {\n" +
            "       $this->getMockBuilder(\\C::class)\n" +
            "           ->addMethods([\n" +
            "               <warning descr=\"Method 'getC' is not allowed to use here\">'getC'</warning>,\n" +
            "               <warning descr=\"Method 'getB' is not allowed to use here\">'getB'</warning>,\n" +
            "               <warning descr=\"Method 'getA' is not allowed to use here\">'getA'</warning>,\n" +
            "               <warning descr=\"Method 'getTA' is not allowed to use here\">'getTA'</warning>,\n" +
            "               <warning descr=\"Method 'getTB' is not allowed to use here\">'getTB'</warning>,\n" +
            "               'getTC',\n" +
            "               'getTD',\n" +
            "           ])\n" +
            "           ->getMock();\n" +
            "   }\n" +
            "}"
        );
        myFixture.checkHighlighting();
    }
}
