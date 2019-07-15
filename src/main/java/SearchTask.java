import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class SearchTask implements Runnable {

    private BlockingQueue<FileContent> blockingQueue;
    private String searchWord;

    SearchTask(BlockingQueue<FileContent> blockingQueue, String searchWord) {
        this.blockingQueue = blockingQueue;
        this.searchWord = searchWord;
    }

    @Override
    public void run() {
        try {
            while(true) {
                FileContent fileContent = blockingQueue.poll(5, TimeUnit.SECONDS);
                if(fileContent != null) {
                    search(fileContent, searchWord);
                } else {
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    static void search(FileContent fileContent, String searchWord) {
        for(Line line : fileContent.getLines()) {
            if(line.getLine().contains(searchWord)) {
                System.out.println(searchWord + ":" + "Line number: " + line.getLineNumber() + " - " + line.getLine());
            }
        }
    }
}
