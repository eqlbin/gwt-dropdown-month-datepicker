package ru.eqlbin.gwt.datepicker.client;

public class DateOutOfRangeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DateOutOfRangeException() {}

    public DateOutOfRangeException(String message) {
        super(message);
    }

    public DateOutOfRangeException(Throwable cause) {
        super(cause);
    }

    public DateOutOfRangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateOutOfRangeException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
