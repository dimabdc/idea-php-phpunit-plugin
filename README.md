# IntelliJ IDEA / PhpStorm PHPUnit Helper

[![Build Status](https://travis-ci.org/dimabdc/idea-php-phpunit-plugin.svg?branch=master)](https://travis-ci.org/dimabdc/idea-php-phpunit-plugin)
![IntelliJ Plugin Compatibility](https://github.com/dimabdc/idea-php-phpunit-plugin/workflows/IntelliJ%20Plugin%20Compatibility/badge.svg)

PhpStorm plugin to provide smart autocomplete, code navigation and refactoring features for mocked class methods. Supported all versions of PhpStorm since 2020.1

Key         | Value
----------- | -----------
Plugin Url  | 
ID          | com.dimabdc.idea.php.phpunit
Changelog   | [CHANGELOG](CHANGELOG.md)
Build and Deployment | [MAINTENANCE](MAINTENANCE.md)
Origin Fork | [Haehnchen/idea-php-phpunit-plugin](https://github.com/Haehnchen/idea-php-phpunit-plugin/)

## Installation

Stable version, JetBrains repository:
* Go to `PhpStorm -> Preferences... -> Plugins -> Install Plugin from Disk ...` and select PHPUnit Helper plugin
* Restart PhpStorm

## Feature list

* method autocomplete for class, abstract class and trait mock objects;
  * type providers: `getMock`, `getMockForAbstractClass`, etc. will return mock object with methods of mocking class and `PHPUnit\Framework\MockObject\MockObject`;
  * supported PHPUnit methods:
    * `PHPUnit\Framework\MockObject\MockBuilder::setMethods`
    * `PHPUnit\Framework\MockObject\MockBuilder::onlyMethods`
    * `PHPUnit\Framework\MockObject\MockBuilder::addMethods`
    * `PHPUnit\Framework\MockObject\TestCase::getMock`
    * `PHPUnit\Framework\MockObject\TestCase::getMockClass`
    * `PHPUnit\Framework\MockObject\TestCase::getMockForAbstractClass` 
    * `PHPUnit\Framework\MockObject\TestCase::getMockForTrait` 
    * `PHPUnit\Framework\MockObject\Builder\InvocationMocker::method` 
    * `PHPUnit\Framework\MockObject\MockObject::method` 
* code navigation (go to declaration, find usages, etc.) and refactoring (rename methods);
* highlighting of incorrect method usages;
* Prophecy support.

### Mocks

```php
/** @var $x \PHPUnit\Framework\TestCase */
$x->createMock(Foo::class)->bar();
```

```php
/** @var $x \PHPUnit\Framework\TestCase */
$x->prophesize(Foo::class)->bar();
```

```php
class Foo extends \PHPUnit\Framework\TestCase
{
   public function foobar()
   {
       $foo = $this->createMock(Foo::class);
       $foo->method('<caret>')
   }
}
```

```php
class Foo extends \PHPUnit\Framework\TestCase
{
   public function setUp()
   {
       $this->foo = $this->createMock('Foo\Bar');
   }
   public function foobar()
   {
       $this->foo->method('<caret>');
   }
}
```

```php
class FooTest extends \PHPUnit\Framework\TestCase
    {
        public function setUp()
        {
            $this->foo = $this->prophesize(Foo::class);
        }
        public function testFoobar()
        {
            $this->foo->getBar()->willReturn();
        }
    }
```

```php
class FooTest extends \PHPUnit\Framework\TestCase
    {
        public function setUp()
        {
            $this->foo = $this->getMockBuilder(\Foo::class);
        }
        public function testFoobar()
        {
            $this->foo->getMock()->bar();
        }
    }
```

### Prophecy

```php
class FooTest extends \PHPUnit\Framework\TestCase
{
    public function testFoobar()
    {
        $foo = $this->prophesize(Foo::class);
        $foo->getBar()->willReturn();
    }
}
```

```php
class FooTest extends \PHPUnit\Framework\TestCase
{
    public function setUp()
    {
        $this->foo = $this->prophesize(Foo::class);
    }
    
    public function testFoobar()
    {
        $this->foo->getBar()->willReturn();
    }
}

```

```php
class FooTest extends \PHPUnit\Framework\TestCase
    {
        public function testFoobar()
        {
            $foo = $this->prophesize(Foo::class);
            $foo->reveal()->getBar();
        }
    }
```

### Intention / Generator

Use intention / generator to add new method mocks. Every caret position inside a mock object is detected

```php
$foo = $this->getMockBuilder(Foobar::class)->getMock();
$foo->method('getFoobar')->willReturn();

$foo = $this->createMock(Foobar::class);
$foo->method('getFoobar')->willReturn();
```

```php
$this->foobar = $this->getMockBuilder(Foobar::class)->getMock();
// ...
$this->foobar->method('getFoobar')->willReturn();

$this->foobar = $this->createMock(Foobar::class);
// ...
$this->foobar->method('getFoobar')->willReturn();
```

```php
new Foobar();
// ...
new Foobar(
    $this->createMock(Foo::class),
    $this->createMock(FooBar::class)
);
```

```php
/**
 * @expectedException \Foo\FooException
 */
public function testExpectedException()
{
    $foo = new FooBar();
    $foo->throwFooException();
}
```

Examples
--------

![PHPUnit_Framework_MockObject_MockBuilder::setMethods](https://jetbrains-plugins.s3.amazonaws.com/9674/screenshot_16946.png)

![PHPUnit_Framework_MockObject_Builder_InvocationMocker::method](https://jetbrains-plugins.s3.amazonaws.com/9674/screenshot_16945.png)

![PHPUnit_Framework_MockObject_Builder_InvocationMocker::method](https://jetbrains-plugins.s3.amazonaws.com/9674/screenshot_16944.png)

![PHPUnit Runner LineMarker](https://jetbrains-plugins.s3.amazonaws.com/9674/screenshot_16951.png)

![PHPUnit Prophecy](https://jetbrains-plugins.s3.amazonaws.com/9674/screenshot_16953.png)

![PHPUnit Expected exception](https://download.plugins.jetbrains.com/9674/screenshot_17449.png)