# jphp-cglib-ext

> Code Generation Library, used to extend **java** classes and implements interfaces at runtime

## Example (Swing UI)

```php
<?php

use java\cglib\Enhancer;
use java\reflection\ReflectionClass;
use java\reflection\ReflectionTypes;

$frame_class = ReflectionClass::forName("javax.swing.JFrame"); // get ReflectionClass for javax.swing.JFrame
$frame = $frame_class->getConstructor([ ReflectionClass::forName("java.lang.String") ])->newInstance([ "Test!" ]); // get new java class instance from construcor
// getting setDefaultCloseOperation method & invoke it
$frame_class->getMethod("setDefaultCloseOperation", [ ReflectionTypes::typeInt() ])->invoke($frame, [ $frame_class->getField("EXIT_ON_CLOSE")->get($frame) ]);

// ...
$frame_class->getMethod("setSize", [
	ReflectionTypes::typeInt(), // getting ReflectionClass for int (int.class)
	ReflectionTypes::typeInt()
])->invoke($frame, [
	ReflectionTypes::toInt(300), // cast jPHP float to ReflectionObject(int)
	ReflectionTypes::toInt(300)
]);

$pane = $frame_class->getMethod("getContentPane", [])->invoke($frame, []); // getting ReflectionObject(java.awt.Container)
$pane_class = ReflectionClass::forName("java.awt.Container"); // ReflectionClass for java.awt.Container

$button_class = ReflectionClass::forName("javax.swing.JButton");
$button = $button_class->getConstructor([ ReflectionClass::forName("java.lang.String") ])->newInstance([ "Test swing button!" ]);

// create callback
$callback_class = ReflectionClass::forName("java.awt.event.ActionListener");
$callback = Enhancer::createWithInterfaces(ReflectionClass::forName("java.lang.Object"), [ $callback_class ], function ($obj, $method, $args) {
	if ($method->getName() == "actionPerformed") {
		echo "Cool!\n";

		return null;
	}

	return $method->invoke($obj, $args);
});

$button_class->getMethod("addActionListener", [ $callback_class ])->invoke($button, [ $callback ]);
$pane_class->getMethod("add", [ ReflectionClass::forName("java.awt.Component") ])->invoke($pane, [ $button ]); // ReflectionObject casts to java.lang.Object
$frame_class->getMethod("setVisible", [ ReflectionTypes::typeBool() ])->invoke($frame, [ true ]);
```
