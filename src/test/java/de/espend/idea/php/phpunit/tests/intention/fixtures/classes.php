<?php

namespace Foo
{
    class Bar
    {
       public function getFooBar()
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

        /**
         * @return \PHPUnit\Framework\MockObject\MockBuilder
         */
        public function getMockBuilder() {
            return new \PHPUnit\Framework\MockObject\MockBuilder();
        }
    }
}

namespace PHPUnit\Framework\MockObject
{
    use PHPUnit\Framework\MockObject\Builder\InvocationMocker;

    class MockBuilder
    {
        /**
         * @return $this
         */
        public function disableOriginalConstructor()
        {
            return $this;
        }

        /**
         * @return $this
         */
        public function getMock()
        {
            return $this;
        }

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
