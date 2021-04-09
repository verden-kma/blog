package edu.ukma.blog.models.user.security;

import org.springframework.security.core.GrantedAuthority;

public enum UserPermission implements GrantedAuthority {
    POST_RECORDS,
    POST_COMMENTS,
    EVALUATE,
    FOLLOW,
    DELETE_OTHER_RECORD,
    DELETE_OTHER_COMMENT,
    BAN_OTHERS;

    @Override
    public String getAuthority() {
        return name();
    }
}
