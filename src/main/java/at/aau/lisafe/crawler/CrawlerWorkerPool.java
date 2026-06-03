package at.aau.lisafe.crawler;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.aau.lisafe.visitor.PageVisitor;

/**
 * Executes {@link PageCrawlTask}s on a fixed-size thread pool.
 *
 * The pool repeatedly drains pending requests from a {@link CrawlContext},
 * submits them for asynchronous execution, and collects finished results via
 * a {@link CompletionService} (blocking on the next completed task instead of
 * busy-polling). Newly discovered links are fed back into the CrawlContext so
 * the
 * loop continues until both the CrawlContext and the in-flight task list are
 * empty.
 */
public class CrawlerWorkerPool {
    private static final Logger LOGGER = Logger.getLogger(CrawlerWorkerPool.class.getName());

    private final ExecutorService executorService;
    private final PageVisitor pageVisitor;

    public CrawlerWorkerPool(PageVisitor pageVisitor, int threadCount) {
        this.pageVisitor = pageVisitor;
        this.executorService = Executors.newFixedThreadPool(threadCount);
    }

    /**
     * Executes the crawling process by submitting tasks to the worker pool and
     * collecting results.
     *
     * @param ctx the CrawlContext containing the URLs to crawl
     * @return a map of URL to PageCrawlResult representing the results of the crawl
     */
    public Map<String, PageCrawlResult> executeCrawling(CrawlContext ctx) {
        CompletionService<PageCrawlResult> completionService = new ExecutorCompletionService<>(executorService);

        Map<String, PageCrawlResult> results = new ConcurrentHashMap<>();

        int runningTasks = 0;
        try {
            runningTasks += submitPendingRequests(ctx, completionService);
            while (runningTasks > 0) {
                PageCrawlResult result = waitForNextResult(completionService);
                runningTasks--;
                results.put(result.url(), result);
                ctx.addChildUrls(result);
                runningTasks += submitPendingRequests(ctx, completionService);
            }

            return results;
        } finally {
            executorService.shutdown();
        }
    }

    private int submitPendingRequests(CrawlContext ctx, CompletionService<PageCrawlResult> completionService) {
        int submitted = 0;
        CrawlRequest request;
        while ((request = ctx.poll()) != null) {
            completionService.submit(new PageCrawlTask(request, pageVisitor));
            submitted++;
        }
        return submitted;
    }

    private PageCrawlResult waitForNextResult(CompletionService<PageCrawlResult> completionService) {
        try {
            Future<PageCrawlResult> completedTask = completionService.take();
            return completedTask.get();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Crawl was interrupted while waiting for results", exception);
        } catch (ExecutionException exception) {
            LOGGER.log(Level.WARNING, "Crawl task failed unexpectedly", exception.getCause());
            return new PageCrawlResult("unknown", 0, null, true, List.of(), Set.of(),
                    CrawlerError.fromException(exception));
        }
    }
}
