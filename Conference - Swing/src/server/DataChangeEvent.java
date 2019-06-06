package server;

import java.util.EventObject;

public class DataChangeEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    public DataChangeEvent(Object source) {
        super(source);
    }

}
