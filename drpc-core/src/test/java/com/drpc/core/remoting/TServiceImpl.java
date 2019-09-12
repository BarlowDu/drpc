package com.drpc.core.remoting;

public class TServiceImpl implements TService {

    @Override
    public void add(int  a,int b) {
        System.out.println("receive request");
    }
}
