package server;

import interfaces.Participant;

import java.io.Serializable;
import java.util.ArrayList;

public class DataParticipant implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<Participant> participants;
    private ArrayList<DataParticipantListener> listeners = new ArrayList<>();
    private DataChangeEvent event = new DataChangeEvent(this);

    DataParticipant() {
        setParticipants(new ArrayList<>());
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
        fireDataChange();
    }

    private Participant getParticipant(int index) {
        return participants.get(index);
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
        fireDataChange();
    }

    public void clear() {
        participants.clear();
    }

    public int getSize() {
        return participants.size();
    }

    public void addDataParticipantListener(DataParticipantListener listener) {
        listeners.add(listener);
    }

    private void fireDataChange() {
        for (DataParticipantListener listener : listeners) {
            listener.dataChanged(event);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < participants.size(); i++) {
            builder.append(i + 1 + ") ");
            builder.append(getParticipant(i).toString());
            builder.append("\n");
        }
        return builder.toString();
    }

}
