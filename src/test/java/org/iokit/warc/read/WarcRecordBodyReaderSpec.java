package org.iokit.warc.read;

import org.iokit.warc.WarcBody;
import org.iokit.warc.WarcHeader;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class WarcRecordBodyReaderSpec {

    @Test
    public void readWellFormedBody() {
        String content = "0123ü56789";
        String input = content + "\r\n\r\n";

        int contentLength = content.getBytes().length;

        WarcHeader header = new WarcHeader(null, null) {
            @Override
            public int getContentLength() {
                return contentLength;
            }
        };

        WarcBody.Reader reader = new WarcBody.Reader(new ByteArrayInputStream(input.getBytes()));
        WarcBody body = reader.read(header);

        byte[] data = body.getData();

        assertThat(data.length).isEqualTo(contentLength);
        assertThat(new String(data, UTF_8)).isEqualTo(content);
    }
}
