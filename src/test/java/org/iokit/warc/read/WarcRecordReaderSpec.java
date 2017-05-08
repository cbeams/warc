package org.iokit.warc.read;

import org.iokit.warc.WarcField;
import org.iokit.warc.WarcRecord;
import org.iokit.warc.WarcRecordHeader;
import org.iokit.warc.WarcRecordVersion;

import org.iokit.core.read.ReaderException;

import org.junit.Test;

import java.io.EOFException;

import static org.assertj.core.api.Assertions.*;
import static org.iokit.warc.WarcRecord.Type.warcinfo;

public class WarcRecordReaderSpec {

    /**
     * C.1: Example of 'warcinfo' record
     */
    @Test
    public void readExampleWarcinfoRecord() throws ReaderException, EOFException {
        String input = "" +
            "WARC/1.0\r\n" +
            "WARC-Type: warcinfo\r\n" +
            "WARC-Date: 2006-09-19T17:20:14Z\r\n" +
            "WARC-WarcRecord-ID: <urn:uuid:d7ae5c10-e6b3-4d27-967d-34780c58ba39>\r\n" +
            "Content-Type: application/warc-fields\r\n" +
            "Content-Length: 398\r\n" +
            "\r\n" +
            "software: Heritrix 1.12.0 http://crawler.archive.org\r\n" +
            "hostname: crawling017.archive.org\r\n" +
            "ip: 207.241.227.234\r\n" +
            "isPartOf: testcrawl-20050708\r\n" +
            "description: testcrawl with WARC output\r\n" +
            "operator: IA_Admin\r\n" +
            "http-header-user-agent:\r\n" +
            " Mozilla/5.0 (compatible; heritrix/1.4.0 +http://crawler.archive.org)\r\n" +
            "format: WARC file version 1.0\r\n" +
            "conformsTo:\r\n" +
            " http://www.archive.org/documents/WarcFileFormat-1.0.html";

        WarcRecordReader reader = new WarcRecordReader(input);

        WarcRecord record = reader.read();
        WarcRecordHeader header = record.getHeader();
        byte[] body = record.getBody().getData();

        assertThat(header.getVersion()).hasToString(WarcRecordVersion.WARC_1_0);
        assertThat(header.getRecordType()).isEqualTo(warcinfo);
        assertThat(header.getDate()).isEqualTo("2006-09-19T17:20:14Z");
        assertThat(header.getRecordId()).isEqualTo("<urn:uuid:d7ae5c10-e6b3-4d27-967d-34780c58ba39>");
        assertThat(header.getContentType()).isEqualTo(WarcField.MIME_TYPE);
        assertThat(header.getContentLength()).isEqualTo(398);

        assertThat(body[0]).isEqualTo((byte) 's'); // the 's' in "software:" on the first line
        assertThat(body[body.length - 1]).isEqualTo((byte) 'l'); // the 'l' in ".html" on the last line
        assertThat(body.length).isEqualTo(header.getContentLength());

        assertThatThrownBy(reader::read).isInstanceOf(EOFException.class);
    }


    /*
    @Test
    public void howFast() throws IOException {

        long start = System.currentTimeMillis();

        //InputStream input = new GZIPInputStream(new FileInputStream(new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/CC-MAIN-20170322212946-00006-ip-10-233-31-227.ec2.internal.warc.wat.gz")));

        //InputStream input = new FileInputStream(new File("/tmp/from-gzipped.warc")); // 1323 ms
        //InputStream input = new GZIPInputStream(new FileInputStream(new File("/tmp/from-gzipped.warc-for-re-gzipping.gz"))); // 4282 ms

        InputStream input = new WarcInputStream(new GZIPInputStream(new FileInputStream(new File("/tmp/from-gzipped.warc-for-re-gzipping.gz")))); // 4393 ms

        byte[] buf = new byte[1024 * 10];
        int bytesRead = input.read(buf);
        while (bytesRead != -1) {
            bytesRead = input.read(buf);
        }

        long finish = System.currentTimeMillis();

        System.out.println(finish - start);
    }

    @Test
    public void howFastFastReadArray() throws IOException {

        long start = System.currentTimeMillis();

        FastBufferedInputStream input = new FastBufferedInputStream(new GZIPInputStream(new FileInputStream(new File("/tmp/from-gzipped.warc-for-re-gzipping.gz"))));
        //FastBufferedInputStream input = new FastBufferedInputStream(new FileInputStream(new File("/tmp/from-gzipped.warc")));

        byte[] array = new byte[1024];
        while (input.read(array, 0, array.length) != -1) {
        }

        long finish = System.currentTimeMillis();

        System.out.println(finish - start);
    }

    @Test
    public void howFastFastReadLine() throws IOException {

        long start = System.currentTimeMillis();

        FastBufferedInputStream input = new FastBufferedInputStream(new GZIPInputStream(new FileInputStream(new File("/tmp/from-gzipped.warc-for-re-gzipping.gz")))); // 5.6s
        //FastBufferedInputStream input = new FastBufferedInputStream(new FileInputStream(new File("/tmp/from-gzipped.warc")));
        //WarcInputStream input = new WarcInputStream(new FileInputStream(new File("/tmp/from-gzipped.warc")));

        int i = 0;
        byte[] array = new byte[1024];
        while (input.readLine(array, 0, array.length, EnumSet.of(FastBufferedInputStream.LineTerminator.CR_LF)) != -1) {
            i++;
        }

        long finish = System.currentTimeMillis();

        System.out.printf("read %d lines in %d ms\n", i, (finish - start));
    }

    @Test
    public void howFastWarcReadLine() throws IOException {

        long start = System.currentTimeMillis();

        WarcInputStream input = new WarcInputStream(new GZIPInputStream(new FileInputStream(new File("/tmp/from-gzipped.warc-for-re-gzipping.gz")))); // 15.1, 15.2, 15.0 s
        //WarcInputStream input = new WarcInputStream(new FileInputStream(new File("/tmp/from-gzipped.warc"))); // 10.3, 11.0 s

        try {
            do {
                input.readLine();
            } while (true);
        } catch (EOFException ignore) {
        }

        long finish = System.currentTimeMillis(); // 15.1, 15.2

        System.out.println(finish - start);
    }

    @Test
    public void howFastReadRecord() throws IOException {

        long start = System.currentTimeMillis();

        WarcRecordReader reader = new WarcRecordReader(new File("/tmp/from-gzipped.warc-for-re-gzipping.gz"));

        int i = 0;
        while (reader.read() != null) {
            i++;
        }

        long finish = System.currentTimeMillis(); // 16.8, 17.3, 16.3

        System.out.println(finish - start);
    }

    @Test
    public void testGzip() throws IOException {
        GZIPInputStream input = new GZIPInputStream(new FileInputStream(new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/CC-MAIN-20170322212946-00006-ip-10-233-31-227.ec2.internal.warc.wat.gz")));

        WarcRecordReader reader = new WarcRecordReader(input);

        int i = 0;
        while (reader.read() != null) {
            i++;
        }
        System.out.println(i);
    }

    @Test
    public void trySomething() throws IOException {
        //GZIPInputStream gzInput = new GZIPInputStream(new FileInputStream(new File("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat/CC-MAIN-20170322212946-00006-ip-10-233-31-227.ec2.internal.warc.wat.gz")));

        //Files.copy(gzInput, new File("/tmp/from-gzipped.warc").toPath());
        //gzInput.close();

        //InputStream input = new FileInputStream(new File("/Users/cbeams/Work/webgraph/bisect.warc")); // good (was cut at line 35)
        //InputStream input = new FileInputStream(new File("/Users/cbeams/Work/webgraph/bisect-68.warc")); // good
        //InputStream input = new FileInputStream(new File("/Users/cbeams/Work/webgraph/bisect-68-79.warc")); // bad - the problem is "Página inicial" at position 3600
        //InputStream input = new FileInputStream(new File("/Users/cbeams/Work/webgraph/bisect-79.warc")); // bad line 79 - expected content length 19376, but read only 19367 chars
        //InputStream input = new FileInputStream(new File("/Users/cbeams/Work/webgraph/bisect-145.warc")); // bad at line 81
        //InputStream input = new FileInputStream(new File("/Users/cbeams/Work/webgraph/bisect-343.warc")); // bad at line 81
        //InputStream input = new FileInputStream(new File("/Users/cbeams/Work/webgraph/bisect-343.warc")); // bad at line 81

        //InputStream input = new FileInputStream(new File("/tmp/from-gzipped.warc")); // GOOD!
        //InputStream input = new GZIPInputStream(new FileInputStream(new File("/tmp/from-gzipped.warc-for-re-gzipping.gz"))); // BAD on 4th record with content-length: 48649
        //InputStream input = new FileInputStream(new File("/tmp/first-46-from-plain")); // GOOD
        //InputStream input = new FileInputStream(new File("/tmp/first-46-from-gzip")); // GOOD

        //InputStream input = new GZIPInputStream(new FileInputStream(new File("/tmp/first-46-from-plain-re-gzipped.gz"))); // GOOD
        //InputStream input = new FileInputStream(new File("/tmp/first-46-from-plain")); // GOOD

        //again... hopefully good this time!
        InputStream input = new GZIPInputStream(new FileInputStream(new File("/tmp/from-gzipped.warc-for-re-gzipping.gz")));

        WarcRecordReader reader = new WarcRecordReader(input);

        int i = 0;
        try {
            while (reader.read() != null) {
                i++;
            }
        } catch (MalformedWarcRecordException ex) {
            System.out.println(i);
            throw ex;
        }

        System.out.println(i);
    }

    @Test
    public void trySomething2() throws IOException {
        InputStream input = new FileInputStream(new File("/Users/cbeams/Work/webgraph/bisect-68-79-two-tokens.txt")); // ?
        InputStreamReader reader = new InputStreamReader(input, "UTF-8");
        char[] bytes = new char[1];
        int bytesRead = reader.read(bytes);
        System.out.println();
        System.out.println("[[[" + String.valueOf(bytes) + "]]]");
        System.out.println();
        assertThat(bytesRead).isEqualTo(1);
    }
    */
}
