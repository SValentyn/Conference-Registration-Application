package server;

import java.rmi.RemoteException;
import java.util.EventListener;

public interface DataParticipantListener extends EventListener {

    void dataChanged(DataChangeEvent event) throws RemoteException;
}
