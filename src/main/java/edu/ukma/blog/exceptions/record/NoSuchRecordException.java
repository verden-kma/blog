package edu.ukma.blog.exceptions.record;

public class NoSuchRecordException extends RuntimeException {
    final int missingRecordId;

    public NoSuchRecordException(int missingRecordId) {
        this.missingRecordId = missingRecordId;
    }

    @Override
    public String getMessage() {
        return "no record with id " + missingRecordId + " found";
    }
}
