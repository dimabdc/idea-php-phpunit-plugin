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
        /**
         * Matches any single value.
         *
         * @return Prophecy\Argument\Token\AnyValueToken
         */
        public static function any()
        {
        }

        /**
         * Checks that argument is identical value.
         *
         * @param mixed $value
         *
         * @return Prophecy\Argument\Token\IdenticalValueToken
         */
        public static function is()
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
