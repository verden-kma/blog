package edu.ukma.blog.exceptions.comment;

import java.util.NoSuchElementException;

public class NoSuchCommentException extends NoSuchElementException {
    public NoSuchCommentException(String publisher, int recordId, int commentId) {
        super("no comment found with publisher=" + publisher + ", recordId=" + recordId + ", commentId=" + commentId);
    }
}
