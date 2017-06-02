package io.webgraph.warc;

import org.iokit.general.EndOfInputException;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static io.webgraph.warc.WarcType.warcinfo;
import static io.webgraph.warc.WarcVersion.WARC_1_0;
import static org.assertj.core.api.Assertions.*;

public class WarcRecordReaderSpec {

    /**
     * C.1: Example of 'warcinfo' record
     */
    @Test
    public void readExampleWarcinfoRecord() {
        String input = "" +
            "WARC/1.0\r\n" +
            "WARC-Type: warcinfo\r\n" +
            "WARC-Date: 2006-09-19T17:20:14Z\r\n" +
            "WARC-Record-ID: <urn:uuid:d7ae5c10-e6b3-4d27-967d-34780c58ba39>\r\n" +
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

        WarcRecord.Reader reader =
            new WarcRecord.Reader(new ByteArrayInputStream(input.getBytes()));

        WarcRecord record = reader.read();
        WarcHeader header = record.getHeader();
        byte[] body = record.getBody().getData();

        assertThat(header.getVersion()).hasToString(WARC_1_0);
        assertThat(header.getType()).isEqualTo(warcinfo);
        assertThat(header.getDate()).isEqualTo("2006-09-19T17:20:14Z");
        assertThat(header.getRecordId()).isEqualTo("<urn:uuid:d7ae5c10-e6b3-4d27-967d-34780c58ba39>");
        assertThat(header.getContentLength()).isEqualTo(398);
        assertThat(header.getContentType()).isPresent().hasValue(WarcField.MIME_TYPE);

        assertThat(body[0]).isEqualTo((byte) 's'); // the 's' in "software:" on the first line
        assertThat(body[body.length - 1]).isEqualTo((byte) 'l'); // the 'l' in ".html" on the last line
        assertThat(body.length).isEqualTo(header.getContentLength());

        assertThatThrownBy(reader::read).isInstanceOf(EndOfInputException.class);
    }
}
