import java.util.concurrent.*;

public class MultithreadedSearchFromFile {

    private final static String filePath = "./resources/veryBigRandomText.txt";

    public static void main(String[] args) {
        int searchThreads = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);
        SearchService searchService = new SearchService("pulvinar", searchThreads, 100);
        FileReader fileReader = new FileReader(filePath, searchService);
        fileReader.start();
        try {
            fileReader.shutdown(2, TimeUnit.MINUTES);
            searchService.shutdown(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
