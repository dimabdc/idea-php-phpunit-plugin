<?php

namespace Bar
{
    class Foo
    {
    }
    class Car
    {
    }
}

namespace Foo
{

    use Bar\Car;
    use Bar\Foo;

    class Bar
    {
        public function __construct(Foo $foo, Car $car)
        {
        }
    }

    class BarNext
    {
        public function __construct(Foo $foo, Car $car, Car $car2, Car $car3)
        {
        }
    }

    class FooExtends extends Bar
    {
    }

    class BarPrimitives
    {
        public function __construct(string $foo1, int $foo2, bool $foo4, Car $car)
        {
        }
    }
}

namespace App\Models
{
    class ChatAIVertex
    {
        public function __construct(array $attributes = [])
        {
        }
    }
}

namespace App\Services\Vertex\AI
{
    use App\Models\ChatAIVertex;

    class AIRepository
    {
        public function __construct(ChatAIVertex $model)
        {
        }
    }
}

namespace PHPUnit\Framework
{
    class TestCase
    {
    }
}
