package com.drpc.core.complier;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.junit.Assert;
import org.junit.Test;

public class WrapperTest {
    public interface  Calc{
        int add(int a, int b);
    }

    Object proxy=new Calc() {
        @Override
        public int add(int a, int b) {
            return a+b;
        }
    };
    @Test
    public void testCalc(){
        try {
            Object a=1;
            int b=(int)a;
            Wrapper wrapper=Wrapper.create("TestCalc",Calc.class);
           Object result= wrapper.invokeMethod(proxy,"add",new Object[]{1,2});
            Assert.assertEquals(result,3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testWrapperTest(){
        try {
            WrapperFactory factory=new WrapperFactory();
            Invoker invoker=factory.getInvoker(Calc.class,proxy);
            Object result= invoker.invoke("add",new Object[]{1,2});
            Assert.assertEquals(result,3);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
