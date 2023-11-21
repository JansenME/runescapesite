package com.maulsinc.runescape.model.exception;

public class ExecutionException extends RuntimeException {
    public ExecutionException(String m, Throwable cause) {
        super(m, cause);
    }
}
