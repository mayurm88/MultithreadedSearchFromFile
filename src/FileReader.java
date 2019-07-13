import java.io.BufferedReader;
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
            BufferedReader br = new BufferedReader(new java.io.FileReader(file));
            String st;
            int count = 0;
            st = br.readLine();
            while(st != null) {
                int size = 0;
                FileContent fileContent = new FileContent(new ArrayList<>());
                while(size < 4*MB && st != null) {
                    size += Character.SIZE * st.length();
                    fileContent.appendLine(new Line(st, ++count));
                    st = br.readLine();
                }
                blockingQueue.put(fileContent);
            }
            System.out.println(Thread.currentThread().getName() + " Finished reading from file");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
