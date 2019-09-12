package com.drpc.core.complier;

import com.drpc.core.model.ComplierArray;
import javassist.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class JavassistTest {


    @Test
    public void testArray() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        Class cif = ComplierArray.class;
        CtClass cls = pool.makeClass(cif.getSimpleName() + "$Impl");
        cls.addInterface(pool.get(cif.getName()));
        for (Method m : cif.getMethods()) {
            StringBuilder code=new StringBuilder();
            Parameter[] ps=m.getParameters();
            code.append("public ");
            if(m.getReturnType()!=Void.class){
                code.append(m.getReturnType().getName()).append(' ');
            }
            code.append(m.getName()).append("(");
            for(int i=0;i<ps.length;i++){
                Parameter p=ps[i];
                if(p.getType().isArray()){
                    code.append(p.getType().getComponentType().getName()).append("[]");
                }else{
                    code.append(p.getType().getName());
                }
                code.append(" ").append(p.getName());
                if(i<ps.length-1){
                    code.append(",");
                }
            }
            code.append("){\n");
            if(m.getReturnType()!=Void.class){
                code.append("return ").append(getDefault(m.getReturnType())).append(";\n}");
            }
            System.out.println(code);
            CtMethod ctm=CtMethod.make(code.toString(),cls);
            cls.addMethod(ctm);
        }
        Class clazz=cls.toClass();
        ComplierArray obj=(ComplierArray)clazz.newInstance();
        Assert.assertEquals(0,obj.sum(new int[1]));
    }

    @Test
    public void testInnerClass() throws Exception{
        ClassPool pool=ClassPool.getDefault();
        CtClass outerClass=pool.makeClass("OuterClass");
        CtClass innerClass=outerClass.makeNestedClass("InnerClass",true);
        innerClass.addField(CtField.make("public static int ID=12;",innerClass));
        innerClass.addMethod(CtMethod.make("public static int get(){return ID;}",innerClass));
        outerClass.addMethod(CtMethod.make("public int get(){return "+innerClass.getName()+".get();}",outerClass));
        Class<?> innerClazz=innerClass.toClass();
        Class<?> clazz=outerClass.toClass();
        Object obj=clazz.newInstance();
        Method m=clazz.getMethod("get",null);


        Assert.assertEquals(m.invoke(obj),12);

       Field idField= innerClazz.getField("ID");
       idField.set(null,102);


        Assert.assertEquals(m.invoke(obj),102);
    }




    private String getDefault(Class<?> clz) {
        if (clz == int.class) {
            return "0";
        }
        return "null";
    }

}
