package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The interface that is used to call remote methods on another virtual machine.
 */
public interface Conference extends Remote {

    int register(Participant participant) throws RemoteException;

    String getInfo() throws RemoteException;

    int getSize() throws RemoteException;

}
