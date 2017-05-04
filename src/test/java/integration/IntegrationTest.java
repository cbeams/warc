package integration;

import org.iokit.warc.read.WarcReader;

import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.IOException;

import java.util.concurrent.atomic.AtomicInteger;

@Ignore
public class IntegrationTest {

    @Test
    public void test() throws IOException {
        long begin = System.currentTimeMillis();
        AtomicInteger n = new AtomicInteger(0);
        Files
            .find(
                Paths.get("/Users/cbeams/Work/webgraph/data/commoncrawl/crawl-data/CC-MAIN-2017-13/segments/1490218186353.38/wat"),
                1,
                (path, basicFileAttributes) -> path.toFile().exists() && path.toFile().getName().endsWith("warc.wat.gz"))
            .forEach(path -> {
                System.out.println("reading " + path);
                long start = System.currentTimeMillis();
                WarcReader reader = new WarcReader(path);
                int count = 0;
                while (reader.read() != null) {
                    count++;
                }
                long finish = System.currentTimeMillis();
                System.out.println("read " + count + " records in " + (finish - start) + " ms");
                long end = System.currentTimeMillis();
                System.out.println("read " + n.incrementAndGet() + " warc files in " + (end - begin) + " ms");
            });
    }
}
