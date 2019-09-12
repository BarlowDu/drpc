package com.drpc.core.remoting;

import com.drpc.core.protocol.DefaultRequest;
import com.drpc.core.protocol.HEAD;
import com.drpc.core.serialize.FstSerialize;
import com.drpc.core.serialize.Serialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import javax.xml.ws.Response;
import java.util.List;

public class DecodeHandler extends ByteToMessageCodec {
    Serialize serializer=new FstSerialize();
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if(msg==null){
            return;
        }
        if(msg instanceof DefaultRequest){
            out.writeByte(0);

        }else{
            out.writeByte(1);
        }
        byte[] bytes=serializer.serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {
        int maxLen=in.readableBytes();
        if(maxLen< HEAD.LENGTH){
            return;
        }
        byte[] buffer=new byte[HEAD.LENGTH];
        in.getBytes(0,buffer);
        byte type=buffer[0];
        int contentLength=byte4ToInt(buffer,1);
        if(maxLen<contentLength+HEAD.LENGTH){
            return;
        }
        in.skipBytes(HEAD.LENGTH);
        buffer=new byte[contentLength];
        in.readBytes(buffer);
        Object obj=serializer.deserialize(buffer);
        out.add(obj);
    }

    public  int byte4ToInt(byte[] bytes, int off) {
            int b0 = bytes[off] & 0xFF;
            int b1 = bytes[off + 1] & 0xFF;
            int b2 = bytes[off + 2] & 0xFF;
            int b3 = bytes[off + 3] & 0xFF;
            return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
        }

}
