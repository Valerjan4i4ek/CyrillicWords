import models.Words;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface WordsParsing extends Remote {
    void parsingWords(String link) throws RemoteException;
    List<Words> getLinkByWord(String word) throws RemoteException;
}
