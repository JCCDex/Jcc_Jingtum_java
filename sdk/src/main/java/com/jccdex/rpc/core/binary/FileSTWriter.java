package com.jccdex.rpc.core.binary;

import com.jccdex.rpc.core.serialized.StreamSink;

import java.io.*;

public class FileSTWriter extends STWriter implements Closeable {
    BufferedOutputStream out;

    private FileSTWriter(StreamSink sink, BufferedOutputStream out) {
        super(sink);
        this.out = out;
    }

    public static FileSTWriter fromFile(String path) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedOutputStream out = new BufferedOutputStream(fos);
        StreamSink sink = new StreamSink(out);

        return new FileSTWriter(sink, out);
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
