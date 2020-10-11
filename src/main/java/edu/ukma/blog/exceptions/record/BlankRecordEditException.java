package edu.ukma.blog.exceptions.record;

import ch.qos.logback.core.pattern.color.BlackCompositeConverter;

public class BlankRecordEditException extends IllegalStateException {
    public BlankRecordEditException(String message) {
        super(message);
    }
}
