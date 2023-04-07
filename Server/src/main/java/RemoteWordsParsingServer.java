import cacheAndDB.WordsCache;
import models.Words;
import parsing.Processor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RemoteWordsParsingServer implements WordsParsing{
    WordsCache wordsCache;
    Processor processor;
    public RemoteWordsParsingServer() {
        wordsCache = new WordsCache();
        processor = new Processor(wordsCache);
        new Thread(processor).start();
    }

    @Override
    public void parsingWords(String link) throws RemoteException {
        processor.addLink(link);
    }
    @Override
    public List<Words> getLinkByWord(String word) throws RemoteException {

        List<Words> returnList = new ArrayList<>();
        if(wordsCache.getListCache() != null && !wordsCache.getListCache().isEmpty()){
            for(Words words : wordsCache.getListCache()){
                if (words.getWordName().equals(word)){
                    returnList.add(words);
                }
            }
        }

        Collections.sort(returnList);

        return returnList;
    }
}
