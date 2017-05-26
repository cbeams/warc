package org.iokit.warc;

import org.iokit.message.DefinedField;
import org.iokit.message.Field;
import org.iokit.message.FieldName;

import java.util.function.Predicate;

import static org.iokit.warc.WarcType.*;

public enum WarcDefinedField implements DefinedField { // TODO: support field-type specific validation

    WARC_Type(mandatory()),
    WARC_Record_ID(mandatory()),
    WARC_Date(mandatory()),
    Content_Length(mandatory()),
    Content_Type,
    WARC_Concurrent_To(optional(), forbiddenIn(warcinfo, conversion, continuation)),
    WARC_Block_Digest,
    WARC_Payload_Digest,
    WARC_IP_Address(optional(), permittedIn(response, resource, request, metadata, revisit)),
    WARC_Refers_To(optional(), forbiddenIn(warcinfo, response, resource, request, continuation)),
    WARC_Target_URI(mandatoryIn(response, resource, request, revisit, conversion, continuation), forbiddenIn(warcinfo)),
    WARC_Truncated,
    WARC_Warcinfo_ID(optional(), forbiddenIn(warcinfo)),
    WARC_Filename(optional(), permittedIn(warcinfo)),
    WARC_Profile(mandatoryIn(revisit)),
    WARC_Identified_Payload_Type,
    WARC_Segment_Origin_ID(mandatoryIn(continuation), permittedIn(continuation)),
    WARC_Segment_Number(mandatoryIn(continuation)),
    WARC_Segment_Total_Length(optional(), permittedIn(continuation)),
    Extension_Field;

    private final String displayName;
    private final FieldName fieldName;
    private final Predicate<WarcType> mandatory;
    private final Predicate<WarcType> permitted;

    WarcDefinedField() {
        this(optional());
    }

    WarcDefinedField(Predicate<WarcType> mandatory) {
        this(mandatory, permitted());
    }

    WarcDefinedField(Predicate<WarcType> mandatory, Predicate<WarcType> permitted) {
        this.displayName = name().replace('_', '-');
        this.fieldName = new FieldName(displayName);
        this.mandatory = mandatory;
        this.permitted = permitted;
    }

    public String displayName() {
        return displayName;
    }

    @Override
    public FieldName fieldName() {
        return fieldName;
    }

    @Override
    public String toString() {
        return displayName();
    }

    public boolean isMandatoryIn(WarcType type) {
        return mandatory.test(type);
    }

    public boolean isPermittedIn(WarcType type) {
        return permitted.test(type);
    }

    private static Predicate<WarcType> mandatory() {
        return t -> true;
    }

    private static Predicate<WarcType> optional() {
        return t -> false;
    }

    private static Predicate<WarcType> mandatoryIn(WarcType... types) {
        return t -> {
            for (WarcType type : types) if (t == type) return true;
            return false;
        };
    }

    private static Predicate<WarcType> permitted() {
        return t -> true;
    }

    private static Predicate<WarcType> permittedIn(WarcType... types) {
        return t -> {
            for (WarcType type : types) if (t == type) return true;
            return false;
        };
    }

    private static Predicate<WarcType> forbiddenIn(WarcType... types) {
        return t -> {
            for (WarcType type : types) if (t == type) return false;
            return true;
        };
    }

    public static WarcDefinedField typeOf(Field field) {
        try {
            return WarcDefinedField.valueOf(field.getName().toString().replace('-', '_'));
        } catch (IllegalArgumentException ex) {
            return WarcDefinedField.Extension_Field;
        }
    }
}
