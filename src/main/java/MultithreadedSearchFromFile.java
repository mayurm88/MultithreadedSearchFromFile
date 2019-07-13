import java.util.concurrent.*;

public class MultithreadedSearchFromFile {

    private final static String filePath = "./resources/veryBigRandomText.txt";

    public static void main(String[] args) {
        BlockingQueue<FileContent> blockingQueue = new LinkedBlockingDeque<>();
        ExecutorService fileReadExecutor = Executors.newSingleThreadExecutor();
        FileReader fileReader = new FileReader(filePath, blockingQueue);
        fileReadExecutor.execute(fileReader);
        int numCpus = Runtime.getRuntime().availableProcessors();
        System.out.println("Creating a fixed thread pool of size: " + (numCpus - 1));
        ExecutorService searchExecutorService = Executors.newFixedThreadPool(numCpus - 1);
        for(int i = 0; i < numCpus - 1; i++) {
            SearchTask searchTask = new SearchTask(blockingQueue, "pulvinar");
            searchExecutorService.execute(searchTask);
        }
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
