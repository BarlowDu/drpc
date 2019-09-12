package com.drpc.core.serialize;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerialize implements Serialize {

    @Override
    public byte[] serialize(Object obj) throws SerializeException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Hessian2Output output=new Hessian2Output(outputStream);
        byte[] result;
        try {
            output.writeObject(obj);
            result = outputStream.toByteArray();
            outputStream.close();
        }catch (IOException e){
            throw new SerializeException(e);
        }
        return result;
    }

    @Override
    public <T> T deserialize(byte[] data) throws SerializeException {
        ByteArrayInputStream inputStream=new ByteArrayInputStream(data);
        Hessian2Input input=new Hessian2Input(inputStream);
        Object result;
        try {
            result = input.readObject();
            inputStream.close();
        } catch (IOException e) {
            throw new SerializeException(e);
        }
        if(result==null){
            return null;
        }
        return (T) result;
    }
}
