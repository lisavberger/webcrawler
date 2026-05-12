package at.aau.lisafe.crawler;

public class CrawlerError {

    private final String message;
    private final String exceptionType;

    public CrawlerError(String message, String exceptionType) {
        this.message = message;
        this.exceptionType = exceptionType;
    }

    public String getMessage() {
        return message;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public static CrawlerError fromException(Exception exception) {
        String message = exception.getMessage() == null ? "No additional error message available."
                : exception.getMessage();
        return new CrawlerError(message, exception.getClass().getName());
    }
}
