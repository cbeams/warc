package org.iokit.warc.read;

import org.iokit.core.read.LineReader;

import org.iokit.core.validate.Validator;

import org.iokit.core.input.LineInputStream;

import org.iokit.core.token.LineTerminator;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;

import java.util.zip.GZIPInputStream;

import it.unimi.dsi.fastutil.bytes.ByteArrays;
import it.unimi.dsi.fastutil.io.FastBufferedInputStream;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static java.lang.System.currentTimeMillis;
import static org.assertj.core.api.Assertions.assertThat;




import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCReader;
import org.archive.io.warc.WARCReaderFactory;
import org.iokit.core.config.Reflector;

@Ignore
public class WarcReaderPerformanceTests {

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
    };


    // with (1): Read 416415 records in 43687 ms
    // with (2): Read 416415 records in 36528 ms
    // with (3): Read 416415 records in 43416 ms

    // sans (1): Read 416415 records in 42711 ms
    // sans (2): Read 416415 records in 43300 ms
    @Test
    public void test() throws Exception {
        File dir = new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/");
        AtomicLong total = new AtomicLong(0);
        Stream.of(dir.listFiles(pathname -> pathname.getName().endsWith("warc.wat.gz")))
            .limit(3)
            .forEach(warcFile -> {
                System.out.println("reading: " + warcFile);
                WarcReader reader = new WarcReader(warcFile);
                try {
                    new Reflector().configure(reader, Validator.class, v -> v.setEnabled(false));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                total.set(total.get() + reader.stream().count());
            });

        long time = stopwatch.runtime(TimeUnit.MILLISECONDS);
        System.out.printf("Read %d records in %d ms\n", total.get(), time);
    }

    @Test
    public void test2() throws Exception {
        File dir = new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/");
        AtomicLong total = new AtomicLong(0);
        Stream.of(dir.listFiles(pathname -> pathname.getName().endsWith("warc.wat.gz")))
            .limit(3)
            .forEach(warcFile -> {
                System.out.println("reading: " + warcFile);
                try {
                    byte[] chunk = new byte[1024];
                    FastBufferedInputStream input = new FastBufferedInputStream(new FileInputStream(warcFile));

                    int start = 0, length;
                    do {
                        while ((length = input.readLine(chunk, start, chunk.length - start, EnumSet.of(FastBufferedInputStream.LineTerminator.CR_LF))) == chunk.length - start) {
                            start += length;
                            chunk = ByteArrays.grow(chunk, chunk.length + 1);
                        }
                        total.incrementAndGet();
                    } while (length != -1);
                    System.out.println("length = " + length);
                    System.out.println("chunk  = " + chunk.length);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        long time = stopwatch.runtime(TimeUnit.MILLISECONDS);
        System.out.printf("Read %d lines records in %d ms\n", total.get(), time);
    }

    @Test
    public void test3() throws Exception {
        File dir = new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/");
        AtomicLong total = new AtomicLong(0);
        Stream.of(dir.listFiles(pathname -> pathname.getName().endsWith("warc.wat.gz")))
            .limit(3)
            .forEach(warcFile -> {
                System.out.println("reading: " + warcFile);
                try {
                    byte[] chunk = new byte[1024];
                    WarcInputStream input = new WarcInputStream(new FileInputStream(warcFile));
                    //FastBufferedInputStream input = new FastBufferedInputStream(new FileInputStream(warcFile));

                    int start = 0, length;
                    do {
                        while ((length = input.readLine(chunk, start, chunk.length - start)) == chunk.length - start) {
                            start += length;
                            chunk = ByteArrays.grow(chunk, chunk.length + 1);
                        }
                        total.incrementAndGet();
                    } while (length != -1);
                    System.out.println("length = " + length);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        long time = stopwatch.runtime(TimeUnit.MILLISECONDS);
        System.out.printf("Read %d lines records in %d ms\n", total.get(), time);
    }

    @Test
    public void test4() throws Exception {
        File dir = new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/");
        AtomicLong total = new AtomicLong(0);
        Stream.of(dir.listFiles(pathname -> pathname.getName().endsWith("warc.wat.gz")))
            .limit(3)
            .forEach(warcFile -> {
                System.out.println("reading: " + warcFile);
                try {
                    LineReader reader = new LineReader(new WarcInputStream(new FileInputStream(warcFile)));
                    while (true) {
                        reader.read();
                        total.incrementAndGet();
                    }
                } catch (EOFException ignored) {
                    System.out.println("EOF");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        long time = stopwatch.runtime(TimeUnit.MILLISECONDS);
        System.out.printf("Read %d lines records in %d ms\n", total.get(), time);
    }

    @Test
    public void x() throws Exception {
        LineReader reader = new LineReader(new LineInputStream(new FileInputStream("/tmp/entire.warc")));
        byte[] line;
        int lines = 0;
        while ((line = reader.fastRead()).length != 0)
            lines++;

        System.out.println(lines);
    }

    @Test
    public void b() throws Exception {
        // 1*1024: 6.5 6.5 6.6
        // 2*1024: 6.9 6.5 6.6
        // 3*1024: 7.1 6.8 6.7
        // 4*1024: 6.8 6.9 6.8
        // 8*1024: 7.3 7.1 6.7
        //16*1024: 6.9 7.2 7.0

        // 1*1024: 6.7 6.1 6.0
        //    512: 6.1 6.2 6.3 *
        //    256: 6.5 6.5 6.4
        //     64: 7.8 7.4 7.5

        // 4092*1024: 6.5 6.5 6.2 (compare with fastutil numbers in d() at same value!)

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/tmp/entire.warc")), 4092*1024);
        int lines = 0;
        while (reader.readLine() != null)
            lines++;

        System.out.printf("read %d lines in %d ms\n", lines, stopwatch.runtime(TimeUnit.MILLISECONDS));
        assertThat(lines).isEqualTo(1527528);
    }

    @Test
    public void c() throws Exception {
        //      512: 2.8 2.7 2.8
        //     1024: 1.8 1.8 1.9
        //   4*1024: 1.1 1.0 1.0
        //   8*1024: 0.7 0.7 1.0
        //  16*1024: 0.8 0.7 0.6
        //  32*1024: 0.6 0.6 0.6 **
        //  64*1024: 0.6 0.6 0.5 *
        // 128*1024: 0.6 0.4 0.6 *
        // 256*1024: 1.2 0.7 0.6
        // 512*1024: 0.8 0.5 0.6

        InputStream input = new FileInputStream("/tmp/entire.warc");
        int count = 0;
        int size = 32*1024;
        byte[] chunk = new byte[size];
        while (input.read(chunk) != -1)
            count++;

        System.out.printf("read %d %d byte chunks in %d ms\n", count, size, stopwatch.runtime(TimeUnit.MILLISECONDS));
    }

    @Test
    public void c_for_normal_warc() throws Exception {
        // baseline
        // $ time cat /Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc > /dev/null
        // 0.7 0.7 0.7

        // 32K read
        // 1.4 1.7 1.5
        // 1.8 1.5 1.8

        // 64K read     *
        // 1.3 1.3 1.4
        // 1.7 1.4 1.4

        // 128K read
        // 1.6 1.3 1.4

        // 1024K read
        // 1.7 1.3 1.5

        InputStream input = new FileInputStream("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc");
        int count = 0;
        int size = 64*1024;
        byte[] chunk = new byte[size];
        while (input.read(chunk) != -1)
            count++;

        System.out.printf("read %d %d byte chunks in %d ms\n", count, size, stopwatch.runtime(TimeUnit.MILLISECONDS));
    }

    @Test
    public void c_for_normal_warc_gz() throws Exception {
        // baseline
        // $ time gunzip -c /Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.gz > /dev/null
        // 10 10 10

        // 512B gzip / 32K read
        // 16 16 16

        // actuals from below with 8K GZIP chunk size and 32K read size     *
        // 12 12 12

        // actuals from below with 8K GZIP chunk size and 8K read size
        // 13 13 13

        // actuals from below with 32K GZIP chunk size and 32K read size    **
        // 12.3 11.9 12.0

        // actuals from below with 1024K GZIP chunk size and 32K read size  ***
        // 11.5 11.7 11.8

        // actuals from below with 1024K GZIP chunk size and 64K read size  ****
        // 11.6 11.3 11.7
        // 11.2 11.4 11.3

        // actuals from below with 1024K GZIP chunk size and 128K read size
        // 11.5 11.7 11.4

        InputStream input = new GZIPInputStream(new FileInputStream("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.gz"), 1024*1024);
        int count = 0;
        int size = 64*1024;
        byte[] chunk = new byte[size];
        while (input.read(chunk) != -1)
            count++;

        System.out.printf("read %d %d byte chunks in %d ms\n", count, size, stopwatch.runtime(TimeUnit.MILLISECONDS));
    }

    @Test
    public void c_for_normal_warc_gz_with_parallel_gzip() throws Exception {
        // baseline
        // $ time gunzip -c /Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.gz > /dev/null
        // 10 10 10

        // 512B gzip / 32K read
        // 16 16 16

        // actuals from below with 8K GZIP chunk size and 32K read size     *
        // 12 12 12

        // actuals from below with 8K GZIP chunk size and 8K read size
        // 13 13 13

        // actuals from below with 32K GZIP chunk size and 32K read size    **
        // 12.3 11.9 12.0

        // actuals from below with 1024K GZIP chunk size and 32K read size  ***
        // 11.5 11.7 11.8

        // actuals from below with 1024K GZIP chunk size and 64K read size  ****
        // 11.6 11.3 11.7
        // 11.2 11.4 11.3

        // actuals from below with 1024K GZIP chunk size and 128K read size
        // 11.5 11.7 11.4

        InputStream input = new GZIPInputStream(new FileInputStream("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.gz"), 1024*1024);
        int count = 0;
        int size = 64*1024;
        byte[] chunk = new byte[size];
        while (input.read(chunk) != -1)
            count++;

        System.out.printf("read %d %d byte chunks in %d ms\n", count, size, stopwatch.runtime(TimeUnit.MILLISECONDS));
    }

    @Test
    public void c_for_normal_warc_gz_loop() throws Exception {
        // baseline
        // $ time gunzip -c /Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.gz > /dev/null
        // 10 10 10

        // 1024K GZIP chunk size and 64K read size (as tested above)
        // 10.9

        readOptimal(); // throw away first run

        int n = 3;
        long start = currentTimeMillis();
        for (int i=0; i<n; i++)
            readOptimal();
        long elapsed = currentTimeMillis() - start;

        System.out.printf("read %d times in %d ms for an average of %d ms\n", n, elapsed, elapsed/n);
    }

    private void readOptimal() throws IOException {
        InputStream input = new GZIPInputStream(new FileInputStream("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.gz"), 1024*1024);
        int count = 0;
        int size = 64*1024;
        byte[] chunk = new byte[size];
        while (input.read(chunk) != -1)
            count++;
    }

    @Test
    public void d() throws Exception {
        // baseline is wc -l
        // wc -l:    1.4 1.3 1.3

        //smaller values are faster but cannot get the exact line count
        //2048*1024: 1.7 1.9 1.7 *
        //4096*1024: 1.9 1.9 1.6

        // not bad, wow


        FastBufferedInputStream input = new FastBufferedInputStream(new FileInputStream("/tmp/entire.warc"));
        int count = 0;
        int size = 2048*1024;
        byte[] chunk = new byte[size];
        while (input.readLine(chunk, EnumSet.of(FastBufferedInputStream.LineTerminator.CR_LF)) != -1)
            count++;

        System.out.printf("read %d %d byte chunks in %d ms\n", count, size, stopwatch.runtime(TimeUnit.MILLISECONDS));
        assertThat(count).isEqualTo(1527528);
    }

    @Test
    public void e() throws Exception {

        //32*1024: 1.7 1.7 1.7 (buffer grows to 2048, same as what we had to manually size in d() above)

        FastBufferedInputStream input = new FastBufferedInputStream(new FileInputStream("/tmp/entire.warc"));
        int count = 0;
        int size = 32*1024;
        byte[] chunk = new byte[size];

        while (true) {
            int start = 0, length;
            while ((length = input.readLine(chunk, start, chunk.length - start, EnumSet.of(FastBufferedInputStream.LineTerminator.CR_LF))) == chunk.length - start) {
                start += length;
                chunk = ByteArrays.grow(chunk, chunk.length + 1);
            }

            if (length == -1)
                break;

            count++;
        }

        System.out.printf("read %d %d byte lines in %d ms\n", count, size, stopwatch.runtime(TimeUnit.MILLISECONDS));
        System.out.printf("final chunk size: %d\n", chunk.length/1024);
        assertThat(count).isEqualTo(1527528);
    }

    @Test
    public void e_for_normal_warc() throws Exception {

        // baseline
        // $ time wc -l CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc
        // 3.8 3.5 3.9

        // 6.4 5.9 6.3 (checking for all line terminators)


        // 7.4 7.5 7.4 (checking for only CRLF terminator)

        FastBufferedInputStream input = new FastBufferedInputStream(new FileInputStream("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc"));
        int count = 0;
        int size = 32*1024;
        byte[] chunk = new byte[size];

        while (true) {
            int start = 0, length;
            while ((length = input.readLine(chunk, start, chunk.length - start, EnumSet.of(FastBufferedInputStream.LineTerminator.CR_LF))) == chunk.length - start) {
                start += length;
                chunk = ByteArrays.grow(chunk, chunk.length + 1);
            }

            if (length == -1)
                break;

            count++;
        }

        System.out.printf("read %d %d byte lines in %d ms\n", count, size, stopwatch.runtime(TimeUnit.MILLISECONDS));
        System.out.printf("final chunk size: %d\n", chunk.length/1024);
        assertThat(count).isEqualTo(67_474_403);
    }

    @Test
    public void e2() throws Exception {

        // 1.8 1.9 1.7 (unchecked)

        // 1.7 1.7 1.9 (checked)

        LineInputStream input = new LineInputStream(new FileInputStream("/tmp/entire.warc"), LineTerminator.CR_LF);

        int count = 0;
        int size = 32*1024;
        byte[] chunk = new byte[size];

        while (true) {
            int start = 0, length;
            while ((length = input.readLine(chunk, start, chunk.length - start)) == chunk.length - start) {
                start += length;
                chunk = ByteArrays.grow(chunk, chunk.length + 1);
            }

            if (length == -1)
                break;

            count++;
        }

        System.out.printf("read %d %d byte lines in %d ms\n", count, size, stopwatch.runtime(TimeUnit.MILLISECONDS));
        System.out.printf("final chunk size: %d\n", chunk.length/1024);
        assertThat(count).isEqualTo(1527528);
    }

    @Test
    public void linesToExclude() throws Exception {

        LineInputStream input = new LineInputStream(new FileInputStream("/tmp/entire.warc"), LineTerminator.CR_LF);

        int bodyLines = 0;
        int count = 0;
        int size = 32*1024;
        byte[] chunk = new byte[size];

        while (true) {
            int start = 0, length;
            while ((length = input.readLine(chunk, start, chunk.length - start)) == chunk.length - start) {
                start += length;
                chunk = ByteArrays.grow(chunk, chunk.length + 1);
            }

            if (length == -1)
                break;

            if (length > 700) // approx
                bodyLines++;

            count++;
        }

        System.out.printf("read %d %d byte lines in %d ms\n", count, size, stopwatch.runtime(TimeUnit.MILLISECONDS));
        System.out.printf("final chunk size: %d\n", chunk.length/1024);
        assertThat(count).isEqualTo(1527528);
        //assertThat(bodyLines).isEqualTo(138_866);
    }

    @Test
    public void e3() throws Exception {

        // 2.6 2.8 2.7

        // now, after sharing the internal byte[] used in LineReader.fastRead:

        // 1.8 1.7 1.9

        LineReader reader = new LineReader(new LineInputStream(new FileInputStream("/tmp/entire.warc"), LineTerminator.CR_LF));

        int count = 0;
        while (reader.fastRead() != null)
            count++;

        System.out.printf("read %d byte[] lines in %d ms\n", count, stopwatch.runtime(TimeUnit.MILLISECONDS));
        assertThat(count).isEqualTo(1527528);
    }

    @Test
    public void e3_for_normal_warc() throws Exception {

        // 7.5 6.5 7.2

        LineReader reader = new LineReader(new LineInputStream(new FileInputStream("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc"), LineTerminator.CR_LF));

        int count = 0;
        while (reader.fastRead() != null)
            count++;

        System.out.printf("read %d byte[] lines in %d ms\n", count, stopwatch.runtime(TimeUnit.MILLISECONDS));
        assertThat(count).isEqualTo(20_377_635);
    }

    @Test
    public void e4() throws Exception {

        // 4.0 4.4 4.1

        // now, after ignoring body lines (those > 1024 bytes in length, based on analysis of Content-Length headers)

        // 1.9 1.8 1.8

        LineReader reader = new LineReader(new LineInputStream(new FileInputStream("/tmp/entire.warc"), LineTerminator.CR_LF));

        int count = 0;
        while (reader.fastStringRead() != null)
            count++;

        System.out.printf("read %d String lines in %d ms\n", count, stopwatch.runtime(TimeUnit.MILLISECONDS));
        assertThat(count).isEqualTo(1527528);
    }

    @Test
    public void e4_for_normal_warc() throws Exception {

        // 9.2 9.4 9.7

        // again, a few mins later...

        // 8.8 9.9 9.0

        LineReader reader = new LineReader(new LineInputStream(new FileInputStream("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc"), LineTerminator.CR_LF));

        int count = 0;
        while (reader.fastStringRead() != null)
            count++;

        System.out.printf("read %d String lines in %d ms\n", count, stopwatch.runtime(TimeUnit.MILLISECONDS));
        //assertThat(count).isEqualTo(1527528);
    }

    @Test
    public void f() throws Exception {

        // baseline
        // $ time gunzip -c /tmp/entire.warc.gz | wc -l
        // 2.9 2.9 2.9

        // sizes here are GZIPInputStream sizes. We know the 32*1024 chunk size works out pretty optimally for FBIS
        //     512: 5.1 5.1 5.4 (default)
        //     512: 5.0 5.0 5.0
        //  2*1024: 4.8 4.5 4.6
        //  4*1024: 4.7 4.4 4.7
        //  8*1024: 4.4 4.5 4.4 *
        // 16*1024: 4.9 4.6 4.7
        // 32*1024: 4.8 4.7 4.7

        int gzSize = 8*1024;
        FastBufferedInputStream input = new FastBufferedInputStream(new GZIPInputStream(new FileInputStream("/tmp/entire.warc.gz"), gzSize));
        int count = 0;
        int size = 32*1024;
        byte[] chunk = new byte[size];

        while (true) {
            int start = 0, length;
            while ((length = input.readLine(chunk, start, chunk.length - start, EnumSet.of(FastBufferedInputStream.LineTerminator.CR_LF))) == chunk.length - start) {
                start += length;
                chunk = ByteArrays.grow(chunk, chunk.length + 1);
            }

            if (length == -1)
                break;

            count++;
        }

        System.out.printf("read %d %d byte chunks in %d ms\n", count, size, stopwatch.runtime(TimeUnit.MILLISECONDS));
        System.out.printf("final chunk size: %d\n", chunk.length/1024);
        assertThat(count).isEqualTo(1527528);
    }

    @Test
    public void g() throws Exception {

        // the following produces
        //     it.unimi.di.law.warc.io.WarcFormatException: Unrecognized record type (metadata)
        /*
        UncompressedWarcReader reader = new UncompressedWarcReader(new FileInputStream("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc"));
        int count = 0;
        while (reader.read() != null)
            count++;

        System.out.printf("read %d records in %d ms\n", count, stopwatch.runtime(TimeUnit.MILLISECONDS));
        */

        // the following produces
        //     it.unimi.di.law.warc.io.gzarc.GZIPArchive$FormatException: Missing SL extra field
        /*
        CompressedWarcReader reader = new CompressedWarcReader(new FileInputStream("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.gz"));
        int count = 0;
        while (reader.read() != null)
            count++;

        System.out.printf("read %d records in %d ms\n", count, stopwatch.runtime(TimeUnit.MILLISECONDS));
        */
    }

    @Test
    public void h() throws Exception {

        // baseline: wc -l
        // 5.1 4.1 3.6 3.6 3.7 3.7

        FastBufferedInputStream input = new FastBufferedInputStream(new FileInputStream("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc"));
        int count = 0;
        int size = 32*1024;
        byte[] chunk = new byte[size];

        while (true) {
            int start = 0, length;
            while ((length = input.readLine(chunk, start, chunk.length - start/*, EnumSet.of(FastBufferedInputStream.LineTerminator.CR_LF)*/)) == chunk.length - start) {
                start += length;
                chunk = ByteArrays.grow(chunk, chunk.length + 1);
            }

            if (length == -1)
                break;

            count++;
        }

        System.out.printf("read %d %d byte chunks in %d ms\n", count, size, stopwatch.runtime(TimeUnit.MILLISECONDS));
        System.out.printf("final chunk size: %d\n", chunk.length/1024);
        assertThat(count).isEqualTo(67474403);
    }

    @Test
    public void i() throws Exception {

        // baseline
        // $ time gunzip -c /tmp/entire.warc.gz | wc -l
        // 2.9 2.9 2.9

        // sizes here are GZIPInputStream sizes. We know the 32*1024 chunk size works out pretty optimally for FBIS
        //     512: 5.1 5.1 5.4 (default)
        //     512: 5.0 5.0 5.0
        //  2*1024: 4.8 4.5 4.6
        //  4*1024: 4.7 4.4 4.7
        //  8*1024: 4.4 4.5 4.4 *
        // 16*1024: 4.9 4.6 4.7
        // 32*1024: 4.8 4.7 4.7

        int gzSize = 8*1024;
        FastBufferedInputStream input = new FastBufferedInputStream(new GZIPInputStream(new FileInputStream("/tmp/entire.warc.gz"), gzSize));
        int count = 0;
        int size = 32*1024;
        byte[] chunk = new byte[size];

        while (true) {
            int start = 0, length;
            while ((length = input.readLine(chunk, start, chunk.length - start, EnumSet.of(FastBufferedInputStream.LineTerminator.CR_LF))) == chunk.length - start) {
                start += length;
                chunk = ByteArrays.grow(chunk, chunk.length + 1);
            }

            if (length == -1)
                break;

            count++;
        }

        System.out.printf("read %d %d byte chunks in %d ms\n", count, size, stopwatch.runtime(TimeUnit.MILLISECONDS));
        System.out.printf("final chunk size: %d\n", chunk.length/1024);
        assertThat(count).isEqualTo(1527528);
    }

    @Test
    public void i_for_normal_warc_gz_without_resizing() throws Exception {

        // baseline
        // $ time gunzip -c $PATH_TO_GZ | wc -l
        // 11.0 11.9 11.4

        // fb: 2M / gz: 2M / ck 2M
        //
        // 15.7 15.8 16.0

        // fb: 4M / gz: 4M / ck 2M
        //
        // 15.6 15.6 15.8

        int fbSize = 4*1024*1024;
        int gzSize = 4*1024*1024;

        FastBufferedInputStream input =
            new FastBufferedInputStream(
                new GZIPInputStream(
                    new FileInputStream("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.gz"),
                    gzSize),
                fbSize);

        int count = 0;
        int ckSize = 2*1024*1024;
        byte[] chunk = new byte[ckSize];
        EnumSet<FastBufferedInputStream.LineTerminator> terminators = EnumSet.of(FastBufferedInputStream.LineTerminator.CR_LF);

        while (input.readLine(chunk, 0, chunk.length, terminators) != -1)
            count++;

        System.out.printf("read %d lines in %d ms\n", count, stopwatch.runtime(TimeUnit.MILLISECONDS));
        assertThat(count).isEqualTo(20_377_635);
    }

    @Test
    public void i_for_normal_warc_gz_with_resizing() throws Exception {

        // baseline
        // $ time gunzip -c $PATH_TO_GZ | wc -l
        // 11.0 11.9 11.4

        // fb: 4M / gz: 4M / ck 1K
        //
        // 15.9 17.7 15.7

        int fbSize = 4*1024*1024;
        int gzSize = 4*1024*1024;

        FastBufferedInputStream input =
            new FastBufferedInputStream(
                new GZIPInputStream(
                    new FileInputStream("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.gz"),
                    gzSize),
                fbSize);

        int count = 0;
        int ckSize = 1024;
        byte[] chunk = new byte[ckSize];
        EnumSet<FastBufferedInputStream.LineTerminator> terminators = EnumSet.of(FastBufferedInputStream.LineTerminator.CR_LF);

        int length;
        do {
            int start = 0;
            while ((length = input.readLine(chunk, start, chunk.length - start, EnumSet.of(FastBufferedInputStream.LineTerminator.CR_LF))) == chunk.length - start) {
                start += length;
                chunk = ByteArrays.grow(chunk, chunk.length + 1);
            }

            if (length != -1) count++;
        } while (length != -1);

        System.out.printf("read %d lines in %d ms\n", count, stopwatch.runtime(TimeUnit.MILLISECONDS));
        assertThat(count).isEqualTo(20_377_635);
    }

    @Test
    public void i_for_normal_warc_gz_with_resizing_3_times() throws Exception {

        // 15.8 ms per read

        i_for_normal_warc_gz_with_resizing(); // throw away warmup
        System.out.println("0");
        long start = currentTimeMillis();
        int n=3;
        for (int i=0; i<n; i++) {
            i_for_normal_warc_gz_with_resizing();
            System.out.println(i);
        }
        long elapsed = currentTimeMillis() - start;
        System.out.printf("read %d times in %d ms for an average of %d ms per read\n", n, elapsed, elapsed/3);
    }

    @Test
    public void j() throws Exception {
        org.jwat.warc.WarcReader reader = org.jwat.warc.WarcReaderFactory.getReaderUncompressed(new FileInputStream("/tmp/entire.warc"));


        int count = 0;
        while (reader.getNextRecord() != null)
            count++;

        // read 138866 records in 46328 ms
        //                     in 37636 ms
        //                     in 36770 ms
        System.out.printf("read %d records in %d ms\n", count, stopwatch.runtime(TimeUnit.MILLISECONDS));
        assertThat(count).isEqualTo(138_866);
    }

    @Test
    public void k() throws Exception {
        org.iokit.warc.read.WarcReader reader = new org.iokit.warc.read.WarcReader(new FileInputStream("/tmp/entire.warc"));


        int count = 0;
        while (reader.read() != null)
            count++;

        // read 138866 records in 11021 ms
        //                     in 11430 ms
        //                     in  9419 ms
        //                     in 10409 ms
        //                     in 10696 ms
        System.out.printf("read %d records in %d ms\n", count, stopwatch.runtime(TimeUnit.MILLISECONDS));
        assertThat(count).isEqualTo(138_866);
    }

    @Test
    public void k_for_normal_warc() throws Exception {
        org.iokit.warc.read.WarcReader reader = new org.iokit.warc.read.WarcReader(new FileInputStream("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc"));


        int count = 0;
        while (reader.read() != null)
            count++;

        // read 138865 records in 24040 (about 3s slower than l_for_normal_warc below)
        //
        //
        //
        //
        System.out.printf("read %d records in %d ms\n", count, stopwatch.runtime(TimeUnit.MILLISECONDS));
        assertThat(count).isEqualTo(138_865);
    }

    @Test
    public void k_for_normal_gz_warc() throws Exception {
        int fbSize = 4*1024*1024;
        int gzSize = 4*1024*1024;

        FastBufferedInputStream input =
            new FastBufferedInputStream(
                new GZIPInputStream(
                    new FileInputStream("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.gz"),
                    gzSize),
                fbSize);

        org.iokit.warc.read.WarcReader reader = new org.iokit.warc.read.WarcReader(new WarcInputStream(input));

        int count = 0;
        while (reader.read() != null)
            count++;

        // read 138865 records in 33125 ms ----> 18+ seconds faster than webarchive-commons in l_for_normal_gz_warc below!!
        //                     in 34281 ms
        //
        // and after merging master, which had removal of regexes...
        //                     in 34432 ms
        //
        // and after removing bytesAsString call...
        //                     in 34942 ms
        //                     in 33663 ms
        //
        // and after further cleaning up LineReader#read
        //                     in 31400 ms
        //                     in 34381 ms
        //
        // and after moving new byte[] out of read method
        //                     in 29574 ms
        //                     in 32636 ms
        //                     in 23430 ms (!!!)
        //
        // before killing validators
        // read 3 times in 66482 ms for an average of 22160 ms per read
        //
        // after
        // read 3 times in 72903 ms for an average of 24301 ms per read (hmm...)
        //
        //
        System.out.printf("read %d records in %d ms\n", count, stopwatch.runtime(TimeUnit.MILLISECONDS));
        assertThat(count).isEqualTo(138_865);
    }

    @Test
    public void k_for_normal_gz_warc_3_times() throws Exception {

        // 24078 ms per read (!)

        k_for_normal_gz_warc(); // throw away warmup
        System.out.println("throwaway complete");
        long start = currentTimeMillis();
        int n=3;
        for (int i=0; i<n; i++) {
            k_for_normal_gz_warc();
            System.out.println(i);
        }
        long elapsed = currentTimeMillis() - start;
        System.out.printf("read %d times in %d ms for an average of %d ms per read\n", n, elapsed, elapsed/3);
    }

    @Test
    public void l() throws Exception {
        WARCReader reader = WARCReaderFactory.get(new File("/tmp/entire.warc"));

        int count = 0;
        for (ArchiveRecord record : reader)
            count++;

        // read 138866 records in 11812 ms
        //                     in  9370 ms
        //                     in  9393 ms
        //                     in  9139 ms
        System.out.printf("read %d records in %d ms\n", count, stopwatch.runtime(TimeUnit.MILLISECONDS));
        assertThat(count).isEqualTo(138_866);
    }

    @Test
    public void l_for_normal_gz_warc() throws Exception {
        org.archive.io.warc.WARCReader reader = WARCReaderFactory.get(new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.gz"));

        int count = 0;
        for (ArchiveRecord record : reader)
            count++;

        // read 138865 records in 52826 ms
        //
        //
        //
        System.out.printf("read %d records in %d ms\n", count, stopwatch.runtime(TimeUnit.MILLISECONDS));
        assertThat(count).isEqualTo(138_865);
    }

    @Test
    public void l_for_normal_warc() throws Exception {
        WARCReader reader = WARCReaderFactory.get(new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/warc/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc"));

        int count = 0;
        for (ArchiveRecord record : reader)
            count++;

        // read 138866 records in 21929 ms
        //
        //
        //
        System.out.printf("read %d records in %d ms\n", count, stopwatch.runtime(TimeUnit.MILLISECONDS));
        assertThat(count).isEqualTo(138_865);
    }

    @Test
    public void A_withValidation() throws Exception {
        WarcReader reader = new WarcReader(new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.wat.gz"));
        new Reflector().configure(reader, Validator.class, v -> v.setEnabled(true));
        long count = reader.stream().limit(10000).count();
        long time = stopwatch.runtime(TimeUnit.MILLISECONDS);
        System.out.printf("A (with) read %d records in %d ms\n", count, time);
    }

    @Test
    public void B_withoutValidation() throws Exception {
        WarcReader reader = new WarcReader(new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.wat.gz"));
        new Reflector().configure(reader, Validator.class, v -> v.setEnabled(false));
        long count = reader.stream().limit(10000).count();
        long time = stopwatch.runtime(TimeUnit.MILLISECONDS);
        System.out.printf("B (sans) read %d records in %d ms\n", count, time);
    }

    @Test
    public void Y_withoutValidation() throws Exception {
        WarcReader reader = new WarcReader(new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.wat.gz"));
        new Reflector().configure(reader, Validator.class, v -> v.setEnabled(false));
        long count = reader.stream().limit(10000).count();
        long time = stopwatch.runtime(TimeUnit.MILLISECONDS);
        System.out.printf("Y (sans) read %d records in %d ms\n", count, time);
    }

    @Test
    public void Z_withValidation() throws Exception {
        WarcReader reader = new WarcReader(new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/CC-MAIN-20170322212946-00000-ip-10-233-31-227.ec2.internal.warc.wat.gz"));
        new Reflector().configure(reader, Validator.class, v -> v.setEnabled(true));
        long count = reader.stream().limit(10000).count();
        long time = stopwatch.runtime(TimeUnit.MILLISECONDS);
        System.out.printf("Z (with) read %d records in %d ms\n", count, time);
    }
}
