package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Conference extends Remote {

    int register(Participant participant) throws RemoteException;

    String getInfo() throws RemoteException;

    int getSize() throws RemoteException;

}
