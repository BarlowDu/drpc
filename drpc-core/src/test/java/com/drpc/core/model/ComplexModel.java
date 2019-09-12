package com.drpc.core.model;

public class ComplexModel {

    public static class A{
        public static int get(){return 12;}
    }

    public int get(){
        return A.get();
    }
    private int requestId;

    private Object data;

    public ComplexModel(int requestId, Object data) {
        this.requestId = requestId;
        this.data = data;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("requestId:%d;%s",requestId,data);
    }
}
