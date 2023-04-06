import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Task implements Runnable {
    private String link;
    private WordsCache wordsCache;
    private final static String USER_AGENT = "Chrome/104.0.0.0";

    public Task(String link, WordsCache wordsCache) {
        this.link = link;
        this.wordsCache = wordsCache;
    }

    @Override
    public void run() {
        returnCyrillicWords();
    }
    public synchronized WordsAndLinks getURLData() throws IOException {
        URL urlObject = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = connection.getResponseCode();
        if(responseCode == 404){
            throw new IllegalArgumentException();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null){
            response.append(inputLine);
        }
        WordsAndLinks wordsAndLinks = new WordsAndLinks(link, response);
        in.close();
        return wordsAndLinks;
    }
    public synchronized List<WordsAndLinks> parsingCyrillicWords() throws IOException{
        WordsAndLinks wordsAndLinks = getURLData();
        List<WordsAndLinks> returnList = new LinkedList<>();

        StringBuffer result = wordsAndLinks.getStringBuffer();
        StringBuffer sb = new StringBuffer();

        if(result != null && !result.isEmpty()){
            for (int i = 0; i < result.length(); i++) {
                if(Character.UnicodeBlock.of(result.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)){
                    if(result.charAt(i+1)==' ' || result.charAt(i+1)=='-' || result.charAt(i+1)=='‑'){
                        sb.append(result.charAt(i) + " ");
                    }
                    else if(result.charAt(i-1)==0 && result.charAt(i+1)==0){
                        sb.append(result.charAt(i) + " ");
                    }
                    else if(result.charAt(i+1)=='.' || result.charAt(i+1)==',' || result.charAt(i+1)=='?'){
                        sb.append(result.charAt(i) + " ");
                    }
                    else{
                        sb.append(result.charAt(i));
                    }
                }
            }
            returnList.add(new WordsAndLinks(wordsAndLinks.getLink(), sb));
        }

        return returnList;
    }
    public void addWords(StringBuffer stringBuffer, String link){
        String word = stringBuffer.toString().toLowerCase();
        String[] words = word.split(" ");
        Words wordAddToDatabaseAndCache;
        Map<String, Words> map = new HashMap<>();

        for(String s : words){
            if(map != null && !map.isEmpty()){
                if(map.containsKey(s)){
                    wordAddToDatabaseAndCache = new Words(map.get(s).getId(), map.get(s).getWordName(),
                            map.get(s).getWordCount()+1, map.get(s).getLink());
                    map.put(s, wordAddToDatabaseAndCache);

                    if(wordsCache.getWordsCache(s) != null){
                        wordsCache.replaceCacheAndDB(wordAddToDatabaseAndCache);
                    }
                    else{
                        wordsCache.addCacheAndDB(wordAddToDatabaseAndCache);}
                }
                else{
                    wordAddToDatabaseAndCache = new Words(map.size() + 1, s, 1, link);
                    map.put(s, wordAddToDatabaseAndCache);
                    wordsCache.addCacheAndDB(wordAddToDatabaseAndCache);
                }
            }
            else{
                wordAddToDatabaseAndCache = new Words(1, s, 1, link);
                map.put(s, wordAddToDatabaseAndCache);
                wordsCache.addCacheAndDB(wordAddToDatabaseAndCache);
            }
        }
    }

    public void returnCyrillicWords() {
        List<WordsAndLinks> wordsAndLinksList = null;
        try {
            wordsAndLinksList = parsingCyrillicWords();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(WordsAndLinks wordsAndLinks : wordsAndLinksList){
            addWords(wordsAndLinks.getStringBuffer(), wordsAndLinks.getLink());
        }
    }
}
