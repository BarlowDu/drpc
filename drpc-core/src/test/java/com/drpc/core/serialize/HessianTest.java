package com.drpc.core.serialize;

import com.drpc.core.model.ComplexModel;
import com.drpc.core.model.SimpleModel;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class HessianTest {

    @Test
    public void testSerialize() throws SerializeException {
        HessianSerialize codec=new HessianSerialize();
        ComplexModel  model=new ComplexModel(112,new SimpleModel(1,"d"));
        byte[] buff=codec.serialize(model);
        ComplexModel smodel=codec.deserialize(buff);

        Assert.assertEquals(model.getRequestId(),smodel.getRequestId());
        Assert.assertEquals(model.getData().getClass(),smodel.getData().getClass());

    }
}
