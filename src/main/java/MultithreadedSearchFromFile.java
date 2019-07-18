import java.util.concurrent.*;

public class MultithreadedSearchFromFile {

    private final static String filePath = "./resources/veryBigRandomText.txt";

    public static void main(String[] args) {
        BlockingQueue<FileContent> blockingQueue = new LinkedBlockingDeque<>(500);

        // Starting the consumer threads first
        int numCpus = Runtime.getRuntime().availableProcessors();
        System.out.println("Creating a fixed thread pool of size: " + Math.max(numCpus - 1, 1));
        ExecutorService searchExecutorService = Executors.newFixedThreadPool(Math.max(numCpus - 1, 1));
        for(int i = 0; i < numCpus - 1; i++) {
            SearchTask searchTask = new SearchTask(blockingQueue, "pulvinar");
            searchExecutorService.execute(searchTask);
        }

        // Starting the producer thread
        ExecutorService fileReadExecutor = Executors.newSingleThreadExecutor();
        FileReader fileReader = new FileReader(filePath, blockingQueue);
        fileReadExecutor.execute(fileReader);


        fileReadExecutor.shutdown();
        searchExecutorService.shutdown();
        try {
            fileReadExecutor.awaitTermination(10, TimeUnit.SECONDS);
            searchExecutorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
