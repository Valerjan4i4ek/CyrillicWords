package parsing;

import cacheAndDB.WordsCache;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

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
    private List<String> LIST_LINKS = new LinkedList<>();
    BlockingDeque<String> BLOCKING_DEQUEUE_LINKS = new LinkedBlockingDeque<>();
    private WordsCache wordsCache;
    ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
    Task task;
    List<String> removeList = new LinkedList<>();
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

            if(!BLOCKING_DEQUEUE_LINKS.isEmpty()){
                if(BLOCKING_DEQUEUE_LINKS.size() > nThreads){
                    int count = 0;
                    while (count < nThreads){
                        task = new Task(BLOCKING_DEQUEUE_LINKS.poll(), wordsCache);
                        executorService.submit(task);
                        count++;
                    }
                }
                else{
                    task = new Task(BLOCKING_DEQUEUE_LINKS.poll(), wordsCache);
                    executorService.submit(task);
                }
            }

//            if(!LIST_LINKS.isEmpty()){
//                if(LIST_LINKS.size() > nThreads){
//                    Iterator<String> iterator = LIST_LINKS.listIterator();
//                    while (iterator.hasNext()){
//                        task = new Task(iterator.next(), wordsCache);
//                        executorService.submit(task);
//                        LIST_LINKS.remove(iterator.next());
//                        removeList.add(iterator.next());
//                    }
//                    int count = 0;
//                    while (count < nThreads){
//                        for(String link : LIST_LINKS){
//                            task = new Task(link, wordsCache);
//                            executorService.submit(task);
//                            count++;
//                            removeList.add(link);
//                        }
//                    }
//                }
//                else{
//                    for(String link : LIST_LINKS){
//                        task = new Task(link, wordsCache);
//                        executorService.submit(task);
//                        removeList.add(link);
//                    }
//                }
//                LIST_LINKS.removeAll(removeList);
//            }
        }

    }
    public void addLink(String link){
        BLOCKING_DEQUEUE_LINKS.add(link);
//        LIST_LINKS.add(link);
    }
}
