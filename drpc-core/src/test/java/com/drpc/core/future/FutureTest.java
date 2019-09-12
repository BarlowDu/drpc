package com.drpc.core.future;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FutureTest {

    @Test
    public void testSetValue() throws InterruptedException {
        int len=100;
        List<DefaultFuture> fetures=new ArrayList<>(len);
        for(int i=0;i<len;i++){
            fetures.add(new DefaultFuture(DefaultFuture.newRequestId()));
        }
        Thread th1=new Thread(()->{
            for(int i=0;i<len;i++){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fetures.get(i).setValue(i);
            }
        });
        Thread th2=new Thread(()->{
            for(int i=0;i<len;i++){
                try {
                    System.out.println(fetures.get(i).get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        th1.start();
        th2.start();
        th1.join();
        th2.join();
    }
}
