package io.webgraph.warc;

import org.iokit.core.IOKitInputStream;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class WarcBodyReaderSpec {

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

        WarcBody.Reader reader = new WarcBody.Reader(new IOKitInputStream(new ByteArrayInputStream(input.getBytes())));
        WarcBody body = reader.read(header);

        byte[] data = body.getData();

        assertThat(data.length).isEqualTo(contentLength);
        assertThat(new String(data, UTF_8)).isEqualTo(content);
    }
}
