package com.github.loki.afro.metallum;

import java.util.concurrent.ExecutionException;

public class MetallumException extends RuntimeException {

    public MetallumException(final String message) {
        super(message);
    }

    public MetallumException(final String message, Exception e) {
        super(message, e);
    }

    public MetallumException(final ExecutionException e) {
        super(e);
    }
}
