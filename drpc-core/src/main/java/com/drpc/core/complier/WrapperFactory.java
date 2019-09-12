package com.drpc.core.complier;

import javassist.CannotCompileException;
import javassist.NotFoundException;

public class WrapperFactory {
    public Invoker getInvoker(Class<?> type,final  Object proxy)
            throws CannotCompileException, InstantiationException, NotFoundException, IllegalAccessException {
        final  Wrapper wrapper=Wrapper.create(type.getName()+"$Proxy",type);
        return new Invoker() {
            @Override
            public Object invoke(String methodName, Object[] parameters)throws Exception {
                return wrapper.invokeMethod(proxy,methodName,parameters);
            }
        };
    }
}
