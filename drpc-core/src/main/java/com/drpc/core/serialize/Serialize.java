package com.drpc.core.serialize;

import java.io.IOException;

public interface Serialize {
    byte[] serialize(Object obj) throws SerializeException;
    <T> T deserialize(byte[] data) throws SerializeException;
}


