<?php

namespace java\cglib;

use java\reflection\ReflectionClass;
use java\reflection\ReflectionObject;

abstract class Enhancer
{
    /**
     * @param ReflectionClass $baseClass
     * @param callable $callback (ReflectionObject obj, ReflectionMethod method, ReflectionObject[] args)
     * @return ReflectionObject
     */
    abstract public static function create(ReflectionClass $baseClass, callable $callback): ReflectionObject;

    /**
     * @param ReflectionClass $baseClass
     * @param ReflectionClass $interfaces
     * @param callable $callback (ReflectionObject obj, ReflectionMethod method, ReflectionObject[] args)
     * @return ReflectionObject
     */
    abstract public static function createWithInterfaces(ReflectionClass $baseClass, ReflectionClass $interfaces, callable $callback): ReflectionObject;
}