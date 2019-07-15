import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class SearchService implements Consumer<FileContent> {

    private final ExecutorService es;
    private final String searchWord;
    private final Semaphore permits;

    SearchService(String searchWord, int numThreads, int maxQueueSize) {
        this.searchWord = searchWord;
        this.permits = new Semaphore(maxQueueSize);
        this.es = Executors.newFixedThreadPool(numThreads);
    }

    @Override
    public void accept(FileContent fileContent) {
        try {
            permits.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        es.submit(() -> {
            SearchTask.search(fileContent, searchWord);
            permits.release();
        });
    }

    void shutdown(int time, TimeUnit unit) throws InterruptedException {
        es.shutdown();
        es.awaitTermination(time, unit);
    }
}
