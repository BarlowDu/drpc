package com.drpc.core.complier;

import javassist.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public abstract class Wrapper {

    public abstract Object invokeMethod(Object proxy, String methodName, Object[] parameters);

    public static Wrapper create(String name, Class<?> type)
            throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass(name);
        ctClass.setSuperclass(classPool.get(Wrapper.class.getName()));
        StringBuilder code = new StringBuilder();
        code.append("public Object invokeMethod(Object proxy, String methodName, Object[] parameters){\n");
        code.append(type.getName()).append(" w;");
        code.append("w=(").append(type.getName()).append(")proxy;");
        //code.append("\nSystem.out.println(proxy);\n");
        Method[] methods = type.getMethods();
        for (Method m : methods) {
            Class<?>[] ps=m.getParameterTypes();
            code.append("if(\"").append(m.getName()).append("\".equals($2)){\n");
            if (m.getReturnType() != Void.TYPE) {
                code.append("return ($w)");
            }
            code.append("w.").append(m.getName()).append("(").append(args(ps,"$3")).append(");");


            if (m.getReturnType() == Void.TYPE) {
                code.append("return null;");
            }
            code.append("\n} throw new Exception(\"not found\");");
        }
        code.append("\n}");
        System.out.println(code);
        CtMethod ctMethod=CtMethod.make(code.toString(),ctClass);
        ctMethod.setExceptionTypes(new CtClass[]{classPool.getCtClass(Exception.class.getName())});
        ctClass.addMethod(ctMethod);
        Class<?> clazz=ctClass.toClass();
        return (Wrapper)clazz.newInstance();
    }
    private static String args(Class<?>[] cs,String name)
    {
        int len = cs.length;
        if( len == 0 ) return "";
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<len;i++)
        {
            if( i > 0 )
                sb.append(',');
            sb.append(arg(cs[i],name+"["+i+"]"));
        }
        return sb.toString();
    }

    private static String arg(Class<?> cl, String name)
    {
        if( cl.isPrimitive() )
        {
            if( cl == Boolean.TYPE )
                return "((Boolean)" + name + ").booleanValue()";
            if( cl == Byte.TYPE )
                return "((Byte)" + name + ").byteValue()";
            if( cl == Character.TYPE )
                return "((Character)" + name + ").charValue()";
            if( cl == Double.TYPE )
                return "((Number)" + name + ").doubleValue()";
            if( cl == Float.TYPE )
                return "((Number)" + name + ").floatValue()";
            if( cl == Integer.TYPE )
                return "((Number)" + name + ").intValue()";
            if( cl == Long.TYPE )
                return "((Number)" + name + ").longValue()";
            if( cl == Short.TYPE )
                return "((Number)" + name + ").shortValue()";
            throw new RuntimeException("Unknown primitive type: " + cl.getName());
        }
        return "(" +cl.getName() + ")" + name;
    }
}

