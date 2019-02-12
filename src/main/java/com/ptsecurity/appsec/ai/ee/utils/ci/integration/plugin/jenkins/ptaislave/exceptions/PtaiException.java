package com.ptsecurity.appsec.ai.ee.utils.ci.integration.plugin.jenkins.ptaislave.exceptions;

import java.io.IOException;

public class PtaiException extends RuntimeException {
    protected Throwable inner;

    public PtaiException(final String message, Throwable innerException) {
        super(message);
        this.inner = innerException;
    }

    public PtaiException(final String message) {
        super(message);
    }
}
