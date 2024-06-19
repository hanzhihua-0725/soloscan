package org.soloquest.soloscan.exception;

public class ExpressionRuntimeException extends RuntimeException {
    public ExpressionRuntimeException() {
    }

    public ExpressionRuntimeException(String message) {
        super(message);
    }

    public ExpressionRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpressionRuntimeException(Throwable cause) {
        super(cause);
    }

    public ExpressionRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
