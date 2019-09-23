package org.develnext.jphp.cglib;

import net.sf.cglib.proxy.Enhancer;
import org.develnext.jphp.cglib.classes.PEnhancer;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;

public class CGLibExtExtension extends Extension {
    public static final String NS = "java\\cglib";
    
    @Override
    public Status getStatus() {
        return Status.EXPERIMENTAL;
    }
    
    @Override
    public void onRegister(CompileScope scope) {
        registerWrapperClass(scope, Enhancer.class, PEnhancer.class);
    }
}