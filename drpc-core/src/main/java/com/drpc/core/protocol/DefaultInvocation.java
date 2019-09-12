package com.drpc.core.protocol;

public class DefaultInvocation implements  Invocation{

    private Class<?> serviceType;
    private String methodName;
    private Object[] arguments;

    public DefaultInvocation(Class<?> serviceType, String methodName, Object[] arguments) {
        this.serviceType = serviceType;
        this.methodName = methodName;
        this.arguments = arguments;
    }

    @Override
    public Class<?> getServiceType() {
        return serviceType;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }
}
