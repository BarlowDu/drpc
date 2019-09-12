package com.drpc.core.protocol;

public interface Invocation {
     Class<?> getServiceType();
     String getMethodName();
     Object[] getArguments();
}
