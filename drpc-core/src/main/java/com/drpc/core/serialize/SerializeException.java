package com.drpc.core.serialize;

public class SerializeException extends Exception {
    public SerializeException(Exception cause){
        super(cause);
    }
    public SerializeException(String msg){
        super(msg);
    }

    public SerializeException(String msg,Exception cause){
        super(msg,cause);
    }
}
