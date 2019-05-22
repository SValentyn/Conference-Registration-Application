package server;

import interfaces.Participant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class DOMParser {

    public static Document parse(String nameFile) throws SAXException, ParserConfigurationException, IOException {
        String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
        SchemaFactory schemaFactory = SchemaFactory.newInstance(language);
        Schema schema = schemaFactory.newSchema(new File("src/xml/Schema.xsd"));

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setSchema(schema);
        builderFactory.setValidating(false);
        builderFactory.setNamespaceAware(true);
        builderFactory.setIgnoringElementContentWhitespace(true);

        ErrorHandler handler = new MyErrorHandler();
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        documentBuilder.setErrorHandler(handler);

        return documentBuilder.parse(new File(nameFile));
    }

    public static void transformDocumentToData(Document document, DataParticipant dataParticipant) throws RemoteException {
        dataParticipant.clear();
        Element rootElement = document.getDocumentElement();
        NodeList nodes = rootElement.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Element participant = (Element) nodes.item(i);

            Element name = (Element) participant.getFirstChild();
            String strName = ((Text) name.getFirstChild()).getData().trim();

            Element surname = (Element) name.getNextSibling();
            String strSurname = ((Text) surname.getFirstChild()).getData().trim();

            Element organization = (Element) surname.getNextSibling();
            String strOrganization = ((Text) organization.getFirstChild()).getData().trim();

            Element report = (Element) organization.getNextSibling();
            String strReport = ((Text) report.getFirstChild()).getData().trim();

            Element email = (Element) report.getNextSibling();
            String strEmail = ((Text) email.getFirstChild()).getData().trim();

            dataParticipant.addParticipant(new Participant(strName, strSurname, strOrganization, strReport, strEmail));
        }
    }

    public static Document transformDataToDocument(DataParticipant dataParticipant) throws ParserConfigurationException, IllegalAccessException {
        Document document = createDocument();
        ArrayList<Participant> participants = dataParticipant.getParticipants();

        Element rootElement = document.createElement("RegisteredConferees");
        for (Participant participant : participants) {
            Element conferee = document.createElement("Participant");

            Class cls = participant.getClass();
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    Element element = document.createElement(field.getName());
                    element.setTextContent(field.get(participant).toString());
                    conferee.appendChild(element);
                }
            }
            rootElement.appendChild(conferee);
        }
        document.appendChild(rootElement);
        return document;
    }

    private static Document createDocument() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.newDocument();
    }

}
