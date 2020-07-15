package de.espend.idea.php.phpunit.tests.intention;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import de.espend.idea.php.phpunit.intention.AddMockMethodIntention;
import de.espend.idea.php.phpunit.tests.PhpUnitLightCodeInsightFixtureTestCase;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 * @see de.espend.idea.php.phpunit.intention.AddMockMethodIntention
 */
public class AddMockMethodIntentionTest extends PhpUnitLightCodeInsightFixtureTestCase {
    public void setUp() throws Exception {
        super.setUp();
        myFixture.copyFileToProject("classes.php");
    }

    public String getTestDataPath() {
        return "src/test/java/de/espend/idea/php/phpunit/tests/intention/fixtures";
    }

    public void testThatIntentionForChainingIsAvailableWithTopMostParent() {
        configureByText(
            "<?php\n" +
            "/** @var $x \\PHPUnit\\Framework\\TestCase */\n" +
            "$x->createMock(\\Foo\\Bar::class)->exp<caret>ects();"
        );
        assertIntentionIsAvailable("PHPUnit: Add mock method");

        configureByText(
            "<?php\n" +
            "/** @var $x \\PHPUnit\\Framework\\TestCase */\n" +
            "$x->creat<caret>eMock(\\Foo\\Bar::class);"
        );
        assertIntentionIsAvailable("PHPUnit: Add mock method");

        configureByText(
            "<?php\n" +
            "function testFoo()" +
            "{\n" +
            "  /** @var $t \\PHPUnit\\Framework\\TestCase */\n" +
            "  $x = $t->getMoc<caret>kBuilder(\\Foo\\Bar::class)->getMock()" +
            "}"
        );
        assertIntentionIsAvailable("PHPUnit: Add mock method");

        configureByText(
            "<?php\n" +
            "function testFoo()" +
            "{\n" +
            "  /** @var $t \\PHPUnit\\Framework\\TestCase */\n" +
            "  $<caret>x = $t->getMockBuilder(\\Foo\\Bar::class)->getMock()" +
            "}"
        );
        assertIntentionIsAvailable("PHPUnit: Add mock method");
    }

    public void testThatInspectionIsInvokedForCreateMockWithInlined() {
        configureByText(
            "<?php\n" +
            "/** @var $x \\PHPUnit\\Framework\\TestCase */\n" +
            "$x->createMock(\\Foo\\Bar::class)->expec<caret>ts();"
        );

        String text = invokeAndGetText();
        assertTrue(text.contains("$x->method('getFooBar')->willReturn();"));

        PsiElement target = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        assertEquals("willReturn", ((MethodReference) target.getParent()).getName());
    }

    public void testThatInspectionIsInvokedForCreateMockWithPropertyAccess() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "        public function setUp()\n" +
            "        {\n" +
            "            $this->foo = $this->createMock(\\Foo\\Bar::class);\n" +
            "        }\n" +
            "        public function testFoobar()\n" +
            "        {\n" +
            "            $this->foo->fo<caret>ar();\n" +
            "        }\n" +
            "    }"
        );

        String text = invokeAndGetText();
        assertTrue(text.contains("$this->foo->method('getFooBar')->willReturn();"));

        PsiElement target = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        assertEquals("willReturn", ((MethodReference) target.getParent()).getName());
    }

    public void testThatIntentionForChainingIsAvailableForCreateMock() {
        configureByText(
            "<?php\n" +
            "/** @var $x \\PHPUnit\\Framework\\TestCase */\n" +
            "$x->createMock(\\Foo\\Bar::class)->expec<caret>ts();"
        );
        assertIntentionIsAvailable("PHPUnit: Add mock method");

        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "        public function setUp()\n" +
            "        {\n" +
            "            $this->foo = $this->createMock(\\Foo\\Bar::class);\n" +
            "        }\n" +
            "        public function testFoobar()\n" +
            "        {\n" +
            "            $this->foo->fo<caret>ar();\n" +
            "        }\n" +
            "    }"
        );
        assertIntentionIsAvailable("PHPUnit: Add mock method");
    }

    public void testThatIntentionForChainingIsAvailableForMockBuilder() {
        configureByText(
            "<?php\n" +
            "function testFoo()" +
            "{\n" +
            "  /** @var $t \\PHPUnit\\Framework\\TestCase */\n" +
            "  $x = $t->getMockBuilder(\\Foo\\Bar::class)->getMock()" +
            "  $x->bar(<caret>);\n" +
            "}"
        );
        assertIntentionIsAvailable("PHPUnit: Add mock method");

        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "        public function setUp()\n" +
            "        {\n" +
            "            $this->foo = $this->getMockBuilder(\\Foo\\Bar::class);\n" +
            "        }\n" +
            "        public function testFoobar()\n" +
            "        {\n" +
            "            $this->foo->getMock()->b<caret>ar();\n" +
            "        }\n" +
            "    }"
        );
        assertIntentionIsAvailable("PHPUnit: Add mock method");
    }

    public void testThatInspectionIsInvokedForMockBuilderWithPropertyAccess() {
        configureByText(
            "<?php\n" +
            "class FooTest extends \\PHPUnit\\Framework\\TestCase\n" +
            "    {\n" +
            "        public function setUp()\n" +
            "        {\n" +
            "            $this->foo = $this->getMockBuilder(\\Foo\\Bar::class)->getMock();\n" +
            "        }\n" +
            "        public function testFoobar()\n" +
            "        {\n" +
            "            $this->foo->b<caret>ar();\n" +
            "        }\n" +
            "    }"
        );

        String text = invokeAndGetText();
        assertTrue(text.contains("$this->foo->method('getFooBar')->willReturn();"));

        PsiElement target = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        assertEquals("willReturn", ((MethodReference) target.getParent()).getName());
    }

    public void testThatInspectionIsInvokedForMockBuilderInlined() {
        configureByText(
            "<?php\n" +
            "function testFoo()" +
            "{\n" +
            "  /** @var $t \\PHPUnit\\Framework\\TestCase */\n" +
            "  $x = $t->getMockBuilder(\\Foo\\Bar::class)->getMock()" +
            "  $x->bar(<caret>);\n" +
            "}"
        );

        String text = invokeAndGetText();

        assertTrue(text.contains("$x->method('getFooBar')->willReturn();"));

        PsiElement target = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        assertEquals("willReturn", ((MethodReference) target.getParent()).getName());
    }

    private String invokeAndGetText() {
        PsiElement psiElement = myFixture.getFile().findElementAt(myFixture.getCaretOffset());

        new AddMockMethodIntention().invoke(getProject(), getEditor(), psiElement);

        return psiElement.getContainingFile().getText();
    }
}
