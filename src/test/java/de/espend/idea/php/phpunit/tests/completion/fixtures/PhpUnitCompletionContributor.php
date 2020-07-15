<?php

namespace Foo
{
    /**
     * @mixin Baz
     */
    class Bar
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
}

namespace PHPUnit\Framework
{
    use PHPUnit\Framework\MockObject\MockObject;

    abstract class TestCase
    {
        /**
         * @param string $originalClassName
         *
         * @return MockObject
         */
        protected function createMock($originalClassName)
        {
            return new MockObject();
        }
    }
}

namespace PHPUnit\Framework\MockObject
{
    use PHPUnit\Framework\MockObject\Builder\InvocationMocker;

    class MockObject
    {
        /**
         * @return InvocationMocker
         */
        public function expects()
        {
            return new InvocationMocker();
        }

        /**
         * @return InvocationMocker
         */
        public function method($constraint)
        {
            return new InvocationMocker();
        }
    }
}

namespace PHPUnit\Framework\MockObject\Builder
{
    class InvocationMocker
    {
        /**
         * @return InvocationMocker
         */
        public function method($constraint)
        {
            return new self();
        }
    }
}
