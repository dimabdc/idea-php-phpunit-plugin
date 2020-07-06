<?php

namespace PHPUnit\Framework
{
    use Prophecy\Prophecy\ObjectProphecy;

    class TestCase
    {
        /**
         * @param null $classOrInterface
         * @return \Prophecy\Prophecy\ObjectProphecy
         */
        protected function prophesize($classOrInterface = null)
        {
            return new ObjectProphecy();
        }
    };
}

namespace Prophecy\Prophecy
{
    class ObjectProphecy
    {
        public function reveal()
        {
        }
    }
}

namespace
{
    class Foo
    {
        public function getBar()
        {
        }
    }
}