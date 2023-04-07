package cacheAndDB;

import cacheAndDB.MySQLClass;
import models.Words;

import java.util.ArrayList;
import java.util.List;

public class WordsCache {
    MySQLClass sql = new MySQLClass();
    List<Words> wordsCache = new ArrayList<>();
    public void addCacheAndDB(Words words){
        wordsCache.add(words);
//        sql.addWords(words);
    }
    public void replaceCacheAndDB(Words words){
        int index = 0;
        for(Words word : wordsCache){
            if(word.getWordName().equals(words.getWordName())){
                index = wordsCache.indexOf(word);
            }
        }
        wordsCache.remove(index);
        wordsCache.add(words);
//        sql.replaceWord(words);
    }
    public Words getWordsCache(String word){
        for(Words words : wordsCache){
            if(words.getWordName().equals(word)){
                return words;
            }
        }
        return null;
    }
    public List<Words> getListCache(){
        return wordsCache;
    }
}
