package org.iokit.warc;

public enum WarcType {

    /**
     * A 'warcinfo' record describes the records that follow it, up through end of file,
     * end of input, or until next 'warcinfo' record. Typically, this appears once and at
     * the beginning of a WARC file. For a web archive, it often contains information
     * about the web crawl which generated the following records.
     * <p>
     * The format of this descriptive record block may vary, though the use of the
     * "application/warc-fields" content-type is recommended. Allowable fields include,
     * but are not limited to, all <a href="http://dublincore.org/documents/dcmi-terms/">
     * DCMI Metadata Terms</a> plus the following field definitions. All fields are
     * optional.
     */
    warcinfo,
    response,
    resource,
    request,
    metadata,
    revisit,
    conversion,
    continuation,
    unrecognized;

    public static WarcType typeOf(String name) {
        try {
            return valueOf(WarcType.class, name);
        } catch (IllegalArgumentException ex) {
            return WarcType.unrecognized;
        }
    }
}
