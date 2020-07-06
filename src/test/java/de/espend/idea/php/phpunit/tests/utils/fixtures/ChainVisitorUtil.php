<?php

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
    }
}

namespace PHPUnit\Framework\MockObject\Builder
{
    class InvocationMocker
    {
    }
}

namespace
{
    abstract class PHPUnit_Framework_TestCase
    {
        /**
         * @param string $originalClassName
         *
         * @return PHPUnit_Framework_MockObject_MockObject
         */
        protected function createMock($originalClassName)
        {
            return new PHPUnit_Framework_MockObject_MockObject();
        }
    }

    class PHPUnit_Framework_MockObject_MockObject
    {
        /**
         * @return PHPUnit_Framework_MockObject_Builder_InvocationMocker
         */
        public function expects()
        {
            return new PHPUnit_Framework_MockObject_Builder_InvocationMocker();
        }
    }

    class PHPUnit_Framework_MockObject_MockBuilder
    {

    }

    class PHPUnit_Framework_MockObject_Builder_InvocationMocker
    {
    }
}
