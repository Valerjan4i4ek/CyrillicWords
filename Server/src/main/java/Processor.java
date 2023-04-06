import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Processor implements Runnable{
    private List<String> LIST_LINKS = new ArrayList<>();
    private WordsCache wordsCache;
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    Task task;
    public Processor(WordsCache wordsCache) {
        this.wordsCache = wordsCache;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(!LIST_LINKS.isEmpty()){
                for(String link : LIST_LINKS){
                    task = new Task(link, wordsCache);
                    executorService.submit(task);
                }
                LIST_LINKS.clear();
            }
        }

    }
    public void addLink(String link){
        LIST_LINKS.add(link);
    }
}
