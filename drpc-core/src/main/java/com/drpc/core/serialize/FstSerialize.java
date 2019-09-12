package com.drpc.core.serialize;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FstSerialize implements Serialize {
    @Override
    public byte[] serialize(Object obj) throws SerializeException {
        FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
        conf.setForceSerializable(true);
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        FSTObjectOutput output= conf.getObjectOutput(outputStream);
        byte[] result;
        try {
            output.writeObject(obj);
            output.flush();
            result = outputStream.toByteArray();
            outputStream.close();
        }catch (IOException e){
            throw new SerializeException(e);
        }
        return result;

    }

    @Override
    public <T> T deserialize(byte[] data) throws SerializeException {
        FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
        conf.setForceSerializable(true);
        ByteArrayInputStream inputStream=new ByteArrayInputStream(data);
        FSTObjectInput input= conf.getObjectInput(inputStream);
        Object result;
        try {
            result = input.readObject();
            inputStream.close();
        } catch (IOException e) {
            throw new SerializeException(e);
        } catch (ClassNotFoundException e) {
            throw new SerializeException(e);
        }
        if(result==null){
            return null;
        }
        return (T) result;
    }
}
