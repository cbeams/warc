package integration;

import org.iokit.warc.read.WarcReader;

import org.iokit.core.read.ReaderException;

import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.IOException;

import java.util.concurrent.atomic.AtomicInteger;

public class IntegrationTest {

    @Test
    @Ignore
    public void test() throws IOException {
        long begin = System.currentTimeMillis();
        AtomicInteger n = new AtomicInteger(0);
        Files
            .find(
                Paths.get("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat"),
                1,
                (path, basicFileAttributes) -> path.toFile().exists() && path.toFile().getName().endsWith("CC-MAIN-20170322212946-00658-ip-10-233-31-227.ec2.internal.warc.wat.gz"))
            .forEach(path -> {
                System.out.println("reading " + path);
                long start = System.currentTimeMillis();
                WarcReader reader;
                try {
                    reader = new WarcReader(path.toFile());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                int count = 0;
                try {
                    while (reader.read() != null) {
                        count++;
                    }
                } catch (ReaderException e) {
                    throw new RuntimeException(e);
                }
                long finish = System.currentTimeMillis();
                System.out.println("read " + count + " records in " + (finish - start) + " ms");
                long end = System.currentTimeMillis();
                System.out.println("read " + n.incrementAndGet() + " warc files in " + (end - begin) + " ms");
            });
    }
}
