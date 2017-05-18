package org.iokit.warc.read;

import org.iokit.warc.WarcRecord;
import org.iokit.warc.write.WarcRecordWriter;

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
    public void test5() throws IOException {
        WarcReader reader = new WarcReader("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.wat");

        reader.seek(1_549_727_457);

        WarcRecord record = reader.read();

        new WarcRecordWriter(System.out).write(record);

        assertThat(record.getHeader().getRecordId()).isEqualTo("<urn:uuid:71124c20-52f4-4451-9de2-d41609631374>");
        assertThat(record.getHeader().getContentLength()).isEqualTo(1068);
    }

    @Test
    @Ignore("need to make this faster")
    public void test6() throws IOException {
        WarcReader reader = new WarcReader("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.wat.gz");

        reader.seek(1_549_727_457);

        WarcRecord record = reader.read();

        new WarcRecordWriter(System.out).write(record);

        assertThat(record.getHeader().getRecordId()).isEqualTo("<urn:uuid:71124c20-52f4-4451-9de2-d41609631374>");
        assertThat(record.getHeader().getContentLength()).isEqualTo(1068);
    }
}
