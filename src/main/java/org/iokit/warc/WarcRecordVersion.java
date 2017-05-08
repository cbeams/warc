package org.iokit.warc;

import java.util.Objects;

public class WarcRecordVersion {

    public static final String WARC_1_0 = "WARC/1.0";
    public static final String WARC_1_1 = "WARC/1.1";

    private final String value;

    public WarcRecordVersion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarcRecordVersion version = (WarcRecordVersion) o;
        return Objects.equals(value, version.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
