package org.develnext.jphp.cglib.classes;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.develnext.jphp.cglib.CGLibExtExtension;
import org.venity.javareflection.classes.ReflectionClass;
import org.venity.javareflection.classes.ReflectionMethod;
import org.venity.javareflection.classes.ReflectionObject;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Reflection.Abstract
@Reflection.Name("Enhancer")
@Reflection.Namespace(CGLibExtExtension.NS)
public class PEnhancer extends BaseWrapper<Enhancer> {
    public PEnhancer(Environment env, Enhancer wrappedObject) {
        super(env, wrappedObject);
    }

    public PEnhancer(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Reflection.Signature
    public static Memory create(Environment environment, ReflectionClass reflectionClass, Invoker invoker) {
        return ObjectMemory.valueOf(new ReflectionObject(environment,
                Enhancer.create(reflectionClass.getWrappedObject(),
                        (InvocationHandler) (proxy, method, args) -> invokeInvoker(proxy, method, args, environment, invoker))));
    }

    @Reflection.Signature
    public static Memory createWithInterfaces(Environment environment, ReflectionClass reflectionClass, List<ReflectionClass> interfaces, Invoker invoker) {
        Class[] classes = new Class[interfaces.size()];

        for (int i = 0; i < interfaces.size(); i++) {
            classes[i] = interfaces.get(i).getWrappedObject();
        }

        return ObjectMemory.valueOf(new ReflectionObject(environment,
                Enhancer.create(reflectionClass.getWrappedObject(), classes,
                        (InvocationHandler) (proxy, method, args) -> invokeInvoker(proxy, method, args, environment, invoker))));
    }

    private static Object invokeInvoker(Object proxy, Method method, Object[] args, Environment environment, Invoker invoker) {
        ArrayMemory arrayMemory = ArrayMemory.createHashed();

        for (Object obj: args)
            arrayMemory.add(new ReflectionObject(environment, obj));

        return Memory.unwrap(environment,
                invoker.callNoThrow(
                        ObjectMemory.valueOf(new ReflectionObject(environment, proxy)),
                        ObjectMemory.valueOf(new ReflectionMethod(environment, method)),
                        arrayMemory));
    }
}
