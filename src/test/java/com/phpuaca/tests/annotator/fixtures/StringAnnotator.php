<?php

namespace
{
    trait TD
    {
        public function getTD()
        {
        }
    }

    trait TA
    {
        public function getTA()
        {
        }
    }

    /**
     * @method getTC()
     * @mixin TD
     */
    trait TB
    {
        use TA;

        public function getTB()
        {
        }
    }

    class A
    {
        public function getA()
        {
        }
    }

    class B extends A
    {
        use TB;

        public function getB()
        {
        }
    }

    class C extends B
    {
        public function getC()
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