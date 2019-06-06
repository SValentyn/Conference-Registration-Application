package server;

import java.util.EventListener;

public interface DataParticipantListener extends EventListener {

    void dataChanged(DataChangeEvent event);
}
