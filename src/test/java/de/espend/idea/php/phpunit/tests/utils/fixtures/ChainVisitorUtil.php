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
