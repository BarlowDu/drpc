package com.drpc.core.serialize;

import com.drpc.core.model.ComplexModel;
import com.drpc.core.model.SimpleModel;
import com.drpc.core.protocol.DefaultRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.Request;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class FstTest {

    @Test
    public void testSerialize() throws SerializeException {
        FstSerialize codec=new FstSerialize();
        ComplexModel model=new ComplexModel(112,1);
        byte[] buff=codec.serialize(model);
        System.out.println(Arrays.toString(buff));
        ComplexModel smodel=codec.deserialize(buff);

        Assert.assertEquals(model.getRequestId(),smodel.getRequestId());
        Assert.assertEquals(model.getData().getClass(),smodel.getData().getClass());


    }

    @Test
    public void TestRequestResponse()throws SerializeException{
        FstSerialize codec=new FstSerialize();
        DefaultRequest request=new DefaultRequest();
        request.setRequestId(1);
        request.setServiceName("calc");
        request.setMethodName("add");
        request.setMethodName("add");
        request.setArguments(new Object[]{1,2});
        byte[] buffRequest=codec.serialize(request);
        DefaultRequest sRequest=codec.deserialize(buffRequest);
        Assert.assertEquals(request.getRequestId(),sRequest.getRequestId());
        Assert.assertEquals(request.getServiceName(),sRequest.getServiceName());
        Assert.assertEquals(request.getMethodName(),sRequest.getMethodName());
    }


    @Test
    public void testTypeSerialize() throws SerializeException {
        FstSerialize codec=new FstSerialize();
        Class<?> clazz=ComplexModel.class;
        byte[] buff=codec.serialize(clazz);
        Class<?> sclazz=codec.deserialize(buff);

        Assert.assertEquals(clazz,sclazz);


    }

    @Test
    public void testMap(){
        HashMap<String,Integer> map=new HashMap<>();
        map.put("a",1);
        map.put("b",2);
        map.put("c",3);
        Assert.assertNull(map.get("d"));
    }
}
