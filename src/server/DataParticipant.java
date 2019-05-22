package server;

import interfaces.Participant;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Logical data management of participants.
 */
public class DataParticipant implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<Participant> participants;
    private ArrayList<DataParticipantListener> listeners = new ArrayList<>();
    private DataChangeEvent event = new DataChangeEvent(this);

    DataParticipant() throws RemoteException {
        setParticipants(new ArrayList<>());
    }

    private void setParticipants(ArrayList<Participant> participants) throws RemoteException {
        this.participants = participants;
        fireDataChange();
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void addParticipant(Participant participant) throws RemoteException {
        participants.add(participant);
        fireDataChange();
    }

    public Participant getParticipant(int index) {
        return participants.get(index);
    }

    public void addDataParticipantListener(DataParticipantListener listener) {
        listeners.add(listener);
    }

    private void fireDataChange() throws RemoteException {
        for (DataParticipantListener listener : listeners)
            listener.dataChanged(event);
    }

    public void clear() {
        participants.clear();
    }

    public int getSize() {
        return participants.size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < participants.size(); i++) {
            builder.append(i + 1 + "). ");
            builder.append(getParticipant(i).toString());
            builder.append("\n");
        }
        return builder.toString();
    }

}
