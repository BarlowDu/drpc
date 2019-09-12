package com.drpc.core.complier;

import javassist.*;

public class JavassistComplier {
    protected ClassPool pool;

    public JavassistComplier(){
        configure();

    }
    protected void configure() {
        pool = ClassPool.getDefault();
        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        try {
            contextLoader.loadClass(JavassistComplier.class.getName());
        } catch (ClassNotFoundException e) { // 如果线程上下文的ClassLoader不能加载当前httl.jar包中的类，则切换回httl.jar所在的ClassLoader
            contextLoader = JavassistComplier.class.getClassLoader();
        }
        pool.appendClassPath(new LoaderClassPath(contextLoader));
    }

    public Class<?>  complie(String name){
        CtClass cls=pool.makeClass(name);
        try {
            //CtField f=new CtField(CtClass.intType,"id",cls);
            CtField f=CtField.make("public int id;",cls);
            cls.addField(f);
            CtMethod m=CtMethod.make("public void run(){System.out.println(\"run\");}",cls);
            cls.addMethod(m);
            return cls.toClass();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return null;
    }
}

