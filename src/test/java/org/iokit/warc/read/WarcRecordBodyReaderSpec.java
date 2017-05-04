package org.iokit.warc.read;

import org.iokit.warc.WarcRecordBody;

import org.iokit.core.parse.ParsingException;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class WarcRecordBodyReaderSpec {

    @Test
    public void readWellFormedBody() throws IOException, ParsingException {
        String content = "0123ü56789";
        String input = content + "\r\n\r\n";

        int contentLength = content.getBytes().length;

        WarcRecordBodyReader reader = new WarcRecordBodyReader(input);
        WarcRecordBody body = reader.read(contentLength);

        byte[] data = body.getData();

        assertThat(data.length).isEqualTo(contentLength);
        assertThat(stringified(data)).isEqualTo(content);
    }

    private String stringified(byte[] data) {
        StringBuilder builder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data), UTF_8))) {
            int c;
            while ((c = reader.read()) != -1)
                builder.append((char) c);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return builder.toString();
    }
}
