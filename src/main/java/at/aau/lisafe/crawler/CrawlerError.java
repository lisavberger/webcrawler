package at.aau.lisafe.crawler;

/**
 * Represents an error that occurred during the crawling process.
 *
 * Each CrawlerError contains:
 * - a message describing the error
 * - the type of exception that caused the error
 *
 * This class is used to capture and represent errors encountered while crawling
 * web pages,
 * allowing for better error handling and reporting in the crawler.
 */
public record CrawlerError(String message, String exceptionType) {

    public CrawlerError {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        if (exceptionType == null) {
            throw new IllegalArgumentException("Exception type cannot be null");
        }
    }

    /**
     * Creates a PageCrawlError instance from an exception.
     *
     * @param exception the exception to convert into a PageCrawlError
     * @return a PageCrawlError containing the message and type of the exception
     */
    public static CrawlerError fromException(Exception exception) {
        String message = exception.getMessage() == null ? "No additional error message available."
                : exception.getMessage();
        return new CrawlerError(message, exception.getClass().getName());
    }
}
