package parsing;

import cacheAndDB.WordsCache;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Processor implements Runnable{
    private final static String fileName = "Server/src/main/resources/database.properties";
    public static int nThreads = 0;
    static {
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream(fileName);
            property.load(fis);

            nThreads = Integer.parseInt(property.getProperty("nThreads"));
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
    }
    private List<String> LIST_LINKS = new ArrayList<>();
    private WordsCache wordsCache;
    ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
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