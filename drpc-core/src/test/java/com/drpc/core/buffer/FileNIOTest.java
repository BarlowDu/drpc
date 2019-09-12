package com.drpc.core.buffer;

import io.netty.buffer.ByteBuf;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileNIOTest {

    @Test
    public void testFileChannel() throws IOException {
        FileChannel fc=null;
        ByteBuffer buffer=ByteBuffer.wrap("abc".getBytes());
        fc.write(buffer);
        buffer.flip();

    }
}
