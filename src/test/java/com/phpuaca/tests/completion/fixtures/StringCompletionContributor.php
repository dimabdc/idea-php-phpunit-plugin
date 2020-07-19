<?php

namespace Foo
{
    /**
     * @method getFoo()
     * @mixin BazBar
     */
    class Bar extends Baz
    {
        public function getFoobar()
        {
        }
    }

    class Baz
    {
        public function getFoobaz()
        {
        }
    }

    class BazBar
    {
        public function getFoobazbar()
        {
        }
    }
}

namespace PHPUnit\Framework
{
    use PHPUnit\Framework\MockObject\MockBuilder;

    abstract class TestCase
    {
        /**
         * @param $className
         * @return MockBuilder
         */
        public function getMockBuilder($className) {
            return new MockBuilder();
        }
    }
}

namespace PHPUnit\Framework\MockObject
{
    class MockBuilder
    {
        public function setMethods(?array $methods = null): self
        {
            return new self();
        }

        public function addMethods(?array $methods = null): self
        {
            return new self();
        }

        public function onlyMethods(?array $methods = null): self
        {
            return new self();
        }

        public function getMock(): MockObject
        {

        }
    }
}
