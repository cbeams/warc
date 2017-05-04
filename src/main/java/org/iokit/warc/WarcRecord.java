package org.iokit.warc;

import org.iokit.imf.Message;

/**
 * Per http://bibnum.bnf.fr/WARC/ and
 * http://bibnum.bnf.fr/WARC/WARC_ISO_28500_version1_latestdraft.pdf.
 */
public class WarcRecord extends Message<WarcRecordHeader, WarcRecordBody> {

    public WarcRecord(WarcRecordHeader header, WarcRecordBody body) {
        super(header, body);
    }

    public enum Type {

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
        metadata,
        unknown
    }
}
