import models.Words;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

public class Client {
    public static final String UNIQUE_BINDING_NAME = "server.WordsParsing";
    public static final List<String> LIST_LINKS = new LinkedList<>();
    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static Registry registry;
    static WordsParsing wordsParsing;
    static {
        try{
            registry = LocateRegistry.getRegistry("127.0.0.1", 2732);
            wordsParsing = (WordsParsing) registry.lookup(UNIQUE_BINDING_NAME);
            LIST_LINKS.add("https://javarush.com/");
            LIST_LINKS.add("https://music.youtube.com/");
            LIST_LINKS.add("https://vertex-academy.com/tutorials/ru/samouchitel-po-java-s-nulya/");
        }
        catch (RemoteException | NotBoundException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        parsingWords();
        System.out.println("Add the word");
        String word = reader.readLine();
        getLinkByWord(word);
    }
    public static void parsingWords() throws RemoteException {
        for(String link : LIST_LINKS){
            wordsParsing.parsingWords(link);
        }
    }
    public static void getLinkByWord(String word) throws RemoteException{
        for(Words words : wordsParsing.getLinkByWord(word)){
            System.out.println(words.getLink() + " " + words.getWordCount());
        }
    }
}
