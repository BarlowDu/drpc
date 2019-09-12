package com.drpc.core.complier;

public interface Invoker {
    public Object invoke(String methodName, Object[] parameters) throws Exception;
}
