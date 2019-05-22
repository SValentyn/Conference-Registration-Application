package server;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class MyErrorHandler implements ErrorHandler {

    @Override
    public void error(SAXParseException exception) {
        System.err.println("Error - " + exception);
        System.err.println("line = " + exception.getLineNumber() + " col = " + exception.getColumnNumber());
    }

    @Override
    public void fatalError(SAXParseException exception) {
        System.err.println("Error - " + exception);
        System.err.println("line = " + exception.getLineNumber() + " col = " + exception.getColumnNumber());
    }

    @Override
    public void warning(SAXParseException exception) {
        System.err.println("Error - " + exception);
        System.err.println("line = " + exception.getLineNumber() + " col = " + exception.getColumnNumber());
    }
}
