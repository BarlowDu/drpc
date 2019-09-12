package com.drpc.core.register;

import com.drpc.core.complier.Invoker;
import com.drpc.core.complier.Wrapper;
import com.drpc.core.complier.WrapperFactory;
import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegister {

    static ConcurrentHashMap<String,Invoker> INVOKERS=new ConcurrentHashMap<>();
    public static <T> void Add(Class<T> clazz,T instance) throws Exception {
        WrapperFactory factory=new WrapperFactory();
        Invoker invoker=factory.getInvoker(clazz,instance);
        INVOKERS.put(clazz.getName(),invoker);
    }

    public static Invoker getInvoker(String name){
        return INVOKERS.get(name);
    }
}
