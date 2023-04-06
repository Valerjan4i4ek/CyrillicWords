import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RemoteWordsParsingServer implements WordsParsing{
    WordsCache wordsCache;
    Processor processor;
    public static final List<String> LIST_LINKS = new LinkedList<>();
    static {
        LIST_LINKS.add("https://javarush.com/");
        LIST_LINKS.add("https://music.youtube.com/");
        LIST_LINKS.add("https://vertex-academy.com/tutorials/ru/samouchitel-po-java-s-nulya/");
    }

    public RemoteWordsParsingServer() {
        wordsCache = new WordsCache();
        processor = new Processor(wordsCache);
        new Thread(processor).start();
    }

    @Override
    public void parsingWords() throws RemoteException {
        for(String link : LIST_LINKS){
            processor.addLink(link);
        }
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
