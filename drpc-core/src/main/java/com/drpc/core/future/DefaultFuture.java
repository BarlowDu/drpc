package com.drpc.core.future;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*
 *
 *如果网络中断导致不调用setValue方法,那么FUTURES中残留的Futrue怎么处理?
 *
 *
 *
 */
public class DefaultFuture implements Future {

  private static ConcurrentHashMap<Long,DefaultFuture> FUTURES=new ConcurrentHashMap<>();
    private static AtomicLong REQUESTID=new AtomicLong(0);

    private long requestId;
    private volatile  Boolean canceled=false;
    private volatile  Boolean done=false;
    private volatile  Object data;
    private ReentrantLock lock=new ReentrantLock();
    private Condition getCondition=lock.newCondition();
    public DefaultFuture(long requestId){
        this.requestId=requestId;
    }


    public static DefaultFuture Add(long requestId){
        DefaultFuture future=new DefaultFuture(requestId);
        FUTURES.put(requestId,future);
        return future;
    }


    public void setValue(Object value){
        lock.lock();
        try {
            data = value;
            done = true;
            getCondition.signal();
            FUTURES.remove(this.requestId);
        }finally {
            lock.unlock();
        }
    }

    public static long newRequestId(){
        return REQUESTID.getAndDecrement();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {

        FUTURES.remove(this.requestId);
        canceled=true;
        return true;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        lock.lock();
        try
        {
            while(!done){//是否还加上cancel的判断
                getCondition.await();
            }
            return data;

        }finally {
            lock.unlock();
        }


    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        lock.lock();
        try
        {
            while(!done){//是否还加上cancel的判断
                if(getCondition.await(timeout,unit)==false){

                    FUTURES.remove(this.requestId);
                    return null;
                }
            }
            return data;

        }finally {
            lock.unlock();
        }
    }
}
