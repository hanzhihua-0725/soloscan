package org.soloquest.soloscan.compiler.codegen;

public class SoloscanClassloader extends ClassLoader {

    public SoloscanClassloader(ClassLoader parent) {
        super(parent);
    }


    public Class<?> defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}
