package com.drpc.core.proxy;

import com.drpc.core.channel.Channel;
import com.drpc.core.future.DefaultFuture;
import com.drpc.core.utils.ReflectUtils;
import javassist.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ProxyFactory {

    public static <T> T getProxy(Class<?> clazz, Channel channel) {

        try {
            Class<?> clz = makeClass(clazz, channel);
            return (T) clz.newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;

        }

    }

    private static Class<?> makeClass(Class<?> clazz, Channel channel)
            throws CannotCompileException, NoSuchFieldException, IllegalAccessException, NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass(clazz.getName() + "$Proxy");
        ctClass.addInterface(pool.getCtClass(clazz.getName()));
        CtClass innerCtClass = ctClass.makeNestedClass("Holder", true);
        String innerClassName = innerCtClass.getName();
        CtField channelField = CtField.make("public static " + Channel.class.getName() + " channel;", innerCtClass);
        innerCtClass.addField(channelField);

        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            StringBuilder code = new StringBuilder();
            code.append("public ");
            if (m.getReturnType() == Void.TYPE) {
                code.append("void ");
            } else {
                code.append(ReflectUtils.getName(m.getReturnType())).append(" ");
            }
            code.append(m.getName()).append("(");
            Class<?>[] ps = m.getParameterTypes();
            for (int i = 0; i < ps.length; i++) {
                code.append(ReflectUtils.getName(ps[i])).append(" arg").append(i);
                if (i < ps.length - 1) {
                    code.append(",");
                }

            }
            code.append("){\n");
            code.append("try{\n");
            code.append(DefaultFuture.class.getName()).append(" future=")
                    .append(innerClassName).append(".channel.invoke(")
                    .append("\"").append(clazz.getName()).append("\",")
                    .append("\"").append(m.getName()).append("\",new Object[]{");
            for (int i = 0; i < ps.length; i++) {
                code.append("arg").append(i);
                if (i < ps.length - 1) {
                    code.append(",");
                }

            }
            code.append("});\n");

            if (m.getReturnType() != Void.TYPE) {
                code.append("System.out.println(future.get());\n");
                code.append("return (").append(m.getReturnType().getName()).append(")future.get();");
                code.append("\n}catch(Exception e){\n");
                if(m.getReturnType()!=Void.TYPE){
                    code.append("return null;");
                }
                code.append("\n}");
            }
            code.append("\n}");
            System.out.println(code);
            CtMethod ctMethod = CtMethod.make(code.toString(), ctClass);
            ctMethod.setExceptionTypes(new CtClass[]{pool.get(Exception.class.getName())});
            ctClass.addMethod(ctMethod);

        }
        Class<?> clz = ctClass.toClass();
        Class<?> innerClass = innerCtClass.toClass();
        Field fChannel = innerClass.getField("channel");
        fChannel.set(null, channel);
        return clz;
    }
}
