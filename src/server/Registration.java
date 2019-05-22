package server;

import interfaces.Conference;
import interfaces.Participant;

import java.rmi.RemoteException;

public class Registration implements Conference {

    private volatile DataParticipant dataParticipant;

    public Registration() throws RemoteException {
        dataParticipant = new DataParticipant();
    }

    public DataParticipant getData() {
        return dataParticipant;
    }

    @Override
    public synchronized int register(Participant participant) throws RemoteException {
        dataParticipant.addParticipant(participant);
        return dataParticipant.getSize();
    }

    @Override
    public String getInfo() {
        return dataParticipant.toString();
    }

    @Override
    public int getSize() {
        return dataParticipant.getSize();
    }

}
