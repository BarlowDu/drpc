package com.drpc.core.proxy;

import com.drpc.core.channel.Channel;
import com.drpc.core.channel.NettyChannel;
import com.drpc.core.future.DefaultFuture;
import com.drpc.core.model.ComplexModel;
import com.drpc.core.model.ComplierArray;
import com.drpc.core.model.SimpleModel;
import com.drpc.core.testinterface.IComplexModel;
import org.junit.Assert;
import org.junit.Test;

public class ProxyFactoryTest {

    @Test
    public void testProxy() throws Exception {
        Object[] s=new Object[]{};
        int i = (int) getInt();

        Channel channel = new Channel() {
            @Override
            public DefaultFuture invoke(String serviceName, String methodName, Object[] args) {
                DefaultFuture f = DefaultFuture.Add(1);
                ComplexModel m=new ComplexModel(1,new SimpleModel(1,"a"));
                f.setValue(m);
                return f;
            }
        };

        IComplexModel c = ProxyFactory.getProxy(IComplexModel.class, channel);
        ComplexModel model=c.get(new int[]{1, 2});
        Assert.assertEquals(model.getRequestId(),1);
    }



    @Test
    public void testTypeChange() {
        int i = (int) getInt();
        Assert.assertEquals(i,1);
    }

    private Object getInt() {
        return 1;
    }

}
