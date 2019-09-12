package com.drpc.core.complier;

import com.drpc.core.channel.Channel;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class JavassistProxy {

    public static Class<?> getProxy(Class<?> cInterface,Channel channel) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cls = pool.makeClass(cInterface.getSimpleName() + "$" + cInterface.getSimpleName() + "$Proxy");
        CtClass clsHolder=cls.makeNestedClass("Holder",true);

        clsHolder.addField(CtField.make("public static "+ Channel.class.getName()+" CHANNEL;",clsHolder));

        cls.addInterface(pool.get(cInterface.getName()));
        for (Method m : cInterface.getMethods()) {
            StringBuilder code = new StringBuilder();
            Parameter[] ps = m.getParameters();
            Class<?>[] exceptions=m.getExceptionTypes();
            code.append("public ");
            if (m.getReturnType() != Void.class) {
                code.append(m.getReturnType().getName()).append(' ');
            }
            code.append(m.getName()).append("(");
            for (int i = 0; i < ps.length; i++) {
                Parameter p = ps[i];
                if (p.getType().isArray()) {
                    code.append(p.getType().getComponentType().getName()).append("[]");
                } else {
                    code.append(p.getType().getName());
                }
                code.append(" ").append(p.getName());
                if (i < ps.length - 1) {
                    code.append(",");
                }
            }
            code.append("){\n");
            if (m.getReturnType() != Void.class) {
                code.append("return ");
            }
            code.append(clsHolder.getName()).append(".CHANNEL.invoke(\"").append(cInterface.getName()).append("\",\"")
                    .append(m.getName()).append("\",new Object[]{$args})")
                    .append(";\n}");
            System.out.println(code);
            CtMethod ctm = CtMethod.make(code.toString(), cls);
            CtClass[] exceptionClasses=new CtClass[exceptions.length];
            for(int i=0;i<exceptions.length;i++) {
                exceptionClasses[i]=pool.getCtClass(exceptions[i].getName());
            }
            ctm.setExceptionTypes(exceptionClasses);

            cls.addMethod(ctm);
        }
        Class<?> holderClazz=clsHolder.toClass();
        Class<?> clazz= cls.toClass();
        Field channelField=holderClazz.getField("CHANNEL");
        channelField.set(null,channel);


        return clazz;
    }

    public void Holder(){}
    static class Holder{
        private int a;
    }
    private String getDefaultValue(Class<?> clz) {
        if (clz == int.class||clz==long.class||clz==double.class||clz==float.class) {
            return "0";
        }
        return "null";
    }

}

