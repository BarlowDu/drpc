package com.drpc.core.channel;

import com.drpc.core.future.DefaultFuture;

public interface Channel {
    DefaultFuture invoke(String serviceName, String methodName, Object[] args);
}
