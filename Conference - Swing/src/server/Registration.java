package server;

import interfaces.Conference;
import interfaces.Participant;

import java.rmi.RemoteException;

public class Registration implements Conference {

    private volatile DataParticipant dataParticipant;

    Registration() {
        dataParticipant = new DataParticipant();
    }

    @Override
    public synchronized int register(Participant participant) {
        dataParticipant.addParticipant(participant);
        return dataParticipant.getSize();
    }

    @Override
    public String getInfo() throws RemoteException {
        return dataParticipant.toString();
    }

    @Override
    public int getSize() {
        return dataParticipant.getSize();
    }

    public DataParticipant getData() {
        return dataParticipant;
    }

}
