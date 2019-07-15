import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * This class is designed to work only in a Single Threaded Executor.
 */

public class FileReader implements Runnable {

    private final static int MB = 1000 * 1000;
    private final String fileName;
    private final Consumer<FileContent> consumer;
    private final ExecutorService es;

    FileReader(String fileName, Consumer<FileContent> consumer) {
        this.fileName = fileName;
        this.consumer = consumer;
        this.es = Executors.newSingleThreadExecutor();
    }

    void start() {
        es.submit(this);
    }

    @Override
    public void run() {
        File file = new File(fileName);
        try {
            int count = 0;
            LineIterator lineIterator = FileUtils.lineIterator(file);
            while (lineIterator.hasNext()) {
                int size = 0;
                FileContent fileContent = new FileContent(new ArrayList<>());
                while (size < 4 * MB && lineIterator.hasNext()) {
                    String st = lineIterator.nextLine();
                    size += Character.SIZE * st.length();
                    fileContent.appendLine(new Line(st, ++count));
                }
                consumer.accept(fileContent);
            }
            System.out.println(Thread.currentThread().getName() + " Finished reading from file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void shutdown(int time, TimeUnit unit) throws InterruptedException {
        es.shutdown();
        es.awaitTermination(time, unit);
    }
}
