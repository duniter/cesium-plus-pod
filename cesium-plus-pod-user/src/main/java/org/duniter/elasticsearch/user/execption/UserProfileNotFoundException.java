package org.duniter.elasticsearch.user.execption;

import org.duniter.elasticsearch.exception.NotFoundException;
import org.elasticsearch.rest.RestStatus;

public class UserProfileNotFoundException extends NotFoundException {
    public UserProfileNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserProfileNotFoundException(String msg, Object... args) {
        super(msg, args);
    }

    public UserProfileNotFoundException(String msg, Throwable cause, Object... args) {
        super(msg, args, cause);
    }

    @Override
    public RestStatus status() {
        return RestStatus.FORBIDDEN;
    }
}
