package io.pinger.plus.plugin.loader;

public class LoadingException extends RuntimeException {

    public LoadingException(String message) {
        super(message);
    }

    public LoadingException(String message, Throwable cause) {
        super(message, cause);
    }

}