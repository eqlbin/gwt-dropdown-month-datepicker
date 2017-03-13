package ru.eqlbin.gwt.datepicker.client;

public class MonthOutOfRangeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MonthOutOfRangeException() {}

    public MonthOutOfRangeException(String message) {
        super(message);
    }

    public MonthOutOfRangeException(Throwable cause) {
        super(cause);
    }

    public MonthOutOfRangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MonthOutOfRangeException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
