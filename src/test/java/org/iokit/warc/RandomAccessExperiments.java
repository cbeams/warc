package org.iokit.warc;

import org.iokit.core.IOKitInputStream;

import org.junit.Ignore;
import org.junit.Test;

import java.util.zip.GZIPInputStream;

import it.unimi.dsi.fastutil.io.FastBufferedInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;




import com.google.common.io.ByteStreams;

public class RandomAccessExperiments {


    @Test
    public void test() throws IOException {
        RandomAccessFile file = new RandomAccessFile(getClass().getResource("/org/iokit/warc/multi.warc").getFile(), "r");

        file.seek(0);
    }

    @Test
    public void test2() throws IOException {
        FastBufferedInputStream in = new FastBufferedInputStream(new FileInputStream(new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.wat")));

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
    @Ignore
    public void test3() throws IOException {
        FastBufferedInputStream in = new FastBufferedInputStream(new GZIPInputStream(new FileInputStream(new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.wat.gz"))));

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
    @Ignore
    public void test4() throws IOException {
        FastBufferedInputStream in = new FastBufferedInputStream(new GZIPInputStream(new FileInputStream(new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.wat.gz"))));
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

    @Test
    @Ignore
    public void test5() throws IOException {
        Warc.Reader reader = new Warc.Reader("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/CC-MAIN-20170322212946-00003-ip-10-233-31-227.ec2.internal.warc.wat");

        reader.in.seek(1_523_285);

        WarcRecord record = reader.read();
        System.out.println(reader.in);

        new WarcRecord.Writer(System.out).write(record);

        assertThat(record.getHeader().getRecordId()).isEqualTo("<urn:uuid:9e7ade15-65c9-472e-93ad-84ec16030cf0>");
        assertThat(record.getHeader().getContentLength()).isEqualTo(30686);
    }

    @Test
    public void test6() throws IOException {
        Warc.Reader reader = new Warc.Reader("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/CC-MAIN-20170322212946-00003-ip-10-233-31-227.ec2.internal.warc.wat.gz");

        ByteStreams.skipFully(IOKitInputStream.Adapter.ADAPTED_STREAM.get(), 0x145c1f7b);

        WarcRecord record = reader.read();

        new WarcRecord.Writer(System.out).write(record);

        assertThat(record.getHeader().getRecordId()).isEqualTo("<urn:uuid:9e7ade15-65c9-472e-93ad-84ec16030cf0>");
        assertThat(record.getHeader().getContentLength()).isEqualTo(30686);
    }
}
