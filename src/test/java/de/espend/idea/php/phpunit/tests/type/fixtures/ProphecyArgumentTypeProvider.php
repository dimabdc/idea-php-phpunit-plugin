<?php

namespace PHPUnit\Framework
{
    use Prophecy\Prophecy\ObjectProphecy;
    {
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
        }
    }
}

namespace Prophecy
{
    class Argument
    {
        public static function any()
        {
        }
    }
}

namespace {
    class Foo
    {
        public function getBar(array $test, Foo $foo)
        {
        }
    }
}
