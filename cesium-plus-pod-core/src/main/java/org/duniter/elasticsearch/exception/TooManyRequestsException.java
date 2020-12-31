package org.duniter.elasticsearch.exception;

import org.elasticsearch.rest.RestStatus;

public class TooManyRequestsException extends DuniterElasticsearchException {
    public TooManyRequestsException(Throwable cause) {
        super(cause);
    }

    public TooManyRequestsException(String msg, Object... args) {
        super(msg, args);
    }

    public TooManyRequestsException(String msg, Throwable cause, Object... args) {
        super(msg, args, cause);
    }

    @Override
    public RestStatus status() {
        return RestStatus.TOO_MANY_REQUESTS;
    }
}
