package org.iokit.warc.read;

import org.junit.Test;

import java.util.zip.GZIPInputStream;

import it.unimi.dsi.fastutil.io.FastBufferedInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RandomAccessExperiments {


    @Test
    public void test() throws IOException {
        RandomAccessFile file = new RandomAccessFile(getClass().getResource("/org/iokit/warc/multi.warc").getFile(), "r");

        file.seek(0);
    }

    @Test
    public void test2() throws IOException {
        FastBufferedInputStream in = new FastBufferedInputStream(new FileInputStream(new File("/tmp/entire.warc")));

        int available = in.available();
        System.out.println(available);

        long lastRecordPosition = available - 1386;
        System.out.println("lastRecordPosition = " + lastRecordPosition);

        {
            in.position(lastRecordPosition);

            byte[] line = new byte[1024];
            int length = in.readLine(line);
            System.out.println("last = " + new String(line, 0, length, UTF_8));
        }

        {
            in.position(0);

            byte[] line = new byte[1024];
            int length = in.readLine(line);
            System.out.println("first = " + new String(line, 0, length, UTF_8));
        }
    }

    @Test
    public void test3() throws IOException {
        FastBufferedInputStream in = new FastBufferedInputStream(new GZIPInputStream(new FileInputStream(new File("/tmp/entire.warc.gz"))));

        int available = in.available();
        System.out.println(available);

        long lastRecordPosition = available - 1386;
        System.out.println("lastRecordPosition = " + lastRecordPosition);

        {
            in.position(lastRecordPosition);

            byte[] line = new byte[1024];
            int length = in.readLine(line);
            System.out.println("last = " + new String(line, 0, length, UTF_8));
        }

        {
            in.position(0);

            byte[] line = new byte[1024];
            int length = in.readLine(line);
            System.out.println("first = " + new String(line, 0, length, UTF_8));
        }
    }

    @Test
    public void test4() throws IOException {
        FastBufferedInputStream in = new FastBufferedInputStream(new GZIPInputStream(new FileInputStream(new File("/tmp/entire.warc.gz"))));
        //FastBufferedInputStream in = new FastBufferedInputStream(new FileInputStream(new File("/tmp/entire.warc")));

        // position of last record is 1549727457

        //in.skip(386);           // works for both
        //in.skip(100_997);       // works for both
        //in.skip(151_102_146);   // works for both

        //in.skip(1_549_727_457); // works only for plain
        //in.skip(1_549_726_433); // works only for gz

        //System.out.println(1_549_727_457 - 1_549_726_433); // = 1024 (!)

        // after fixing bug in LineReader#read...
        //
        in.skip(1_549_727_457); // works for both!

        for (int i=0; i<11; i++) {
            byte[] line = new byte[1024];
            int length = in.readLine(line);
            System.out.println(new String(line, 0, length, UTF_8));
        }
    }
}
