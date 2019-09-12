package com.drpc.core;

import com.drpc.core.complier.JavassistComplier;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        JavassistComplier complier = new JavassistComplier();
        Class<?> clazz = complier.complie("Test001");
        if (clazz == null) {
            System.out.println("class is null");
            return;
        }
        try {
            System.out.println(clazz.getFields().length);
            Field f = clazz.getField("id");
            Method m = clazz.getMethod("run");
            Object obj = clazz.newInstance();
            System.out.println(f.get(obj));
            m.invoke(obj, null);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    static public class A {
        public int ID;
        static class ID{}

        public void Run(){}
        static class Run{}

    }
}
