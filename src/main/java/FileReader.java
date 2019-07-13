import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 *  This class is designed to work only in a Single Threaded Executor.
 */

public class FileReader implements Runnable {

    private final static int MB = 1000*1000;
    private String fileName;
    private BlockingQueue<FileContent> blockingQueue;

    FileReader(String fileName, BlockingQueue<FileContent> blockingQueue) {
        this.fileName = fileName;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        File file = new File(fileName);
        try {
            int count = 0;
            LineIterator lineIterator = FileUtils.lineIterator(file);
            while(lineIterator.hasNext()) {
                int size = 0;
                FileContent fileContent = new FileContent(new ArrayList<>());
                while(size < 4*MB && lineIterator.hasNext()) {
                    String st = lineIterator.nextLine();
                    size += Character.SIZE * st.length();
                    fileContent.appendLine(new Line(st, ++count));
                }
                blockingQueue.put(fileContent);
            }
            System.out.println(Thread.currentThread().getName() + " Finished reading from file");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
