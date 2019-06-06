
package server;

import interfaces.Conference;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.Permission;

/**
 * The application is a window for registering participants for the conference.
 * The basis is the use of RMI technology and Swing library.
 *
 * @author Syniuk Valentyn
 * @version 1.0
 */
public class Server extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextArea textArea;

    private JButton startButton;
    private JButton stopButton;
    private JButton loadButton;
    private JButton saveButton;
    private JButton exitButton;

    private JTextField hostTextField;
    private JTextField portTextField;
    private JTextField participantsTextField;

    private Registration registration;
    private Conference stub;
    private Registry registry;

    private Document document;

    private final JFileChooser fileChooser = new JFileChooser();

    public static void main(String[] args) {
        System.setProperty("java.rmi.server.codebase", "file://codebase.jar");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager() {
                @Override
                public void checkConnect(String host, int port) {
                }

                @Override
                public void checkConnect(String host, int port, Object context) {
                }

                @Override
                public void checkPermission(Permission perm) {
                }
            });
        }

        java.awt.EventQueue.invokeLater(() -> new Server().setVisible(true));
    }

    private Server() {
        registration = new Registration();
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fileChooser.setCurrentDirectory(new File("src/xml/"));

        JPanel buttonPanel = new JPanel();
        startButton = new JButton();
        stopButton = new JButton();
        saveButton = new JButton();
        loadButton = new JButton();
        exitButton = new JButton();

        JScrollPane jScrollPane = new JScrollPane();
        textArea = new JTextArea();

        textArea.setEditable(false);
        textArea.setColumns(20);
        textArea.setRows(5);
        jScrollPane.setViewportView(textArea);

        JLabel labelHost = new JLabel();
        JLabel labelPort = new JLabel();
        JLabel labelParticipants = new JLabel();

        hostTextField = new JTextField();
        portTextField = new JTextField();
        participantsTextField = new JTextField();

        labelHost.setText("Host: ");
        hostTextField.setEditable(false);
        hostTextField.setText("127.0.0.1");

        labelPort.setText("Port:");
        portTextField.setText("10100");

        labelParticipants.setText("Participants:");
        participantsTextField.setEditable(false);
        participantsTextField.setText("0");

        startButton.setText("Start");
        startButton.addActionListener(event -> {
            try {
                startServer();
                JOptionPane.showMessageDialog(Server.this, "Server started successfully!", "Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(Server.this, "Could not start server", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        stopButton.setText("Stop");
        stopButton.setEnabled(false);
        stopButton.addActionListener(event -> {
            try {
                stopServer();
                JOptionPane.showMessageDialog(Server.this, "Server successfully stopped!", "Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException | RemoteException | NotBoundException e) {
                JOptionPane.showMessageDialog(Server.this, "Could not stop server", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        saveButton.setText("Save");
        saveButton.addActionListener(event -> {
            if (JFileChooser.APPROVE_OPTION == fileChooser.showDialog(null, "Save")) {
                String fileName = fileChooser.getSelectedFile().getPath();
                try {
                    document = DOMParser.transformDataToDocument(registration.getData());
                    save(document, fileName);
                    JOptionPane.showMessageDialog(Server.this, "Save data!", "Error", JOptionPane.INFORMATION_MESSAGE);
                } catch (TransformerException | IllegalAccessException | ParserConfigurationException e) {
                    JOptionPane.showMessageDialog(Server.this, "Could not save", "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        });

        loadButton.setText("Load");
        loadButton.addActionListener(event -> {
            if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(null)) {
                String fileName = fileChooser.getSelectedFile().getPath();
                try {
                    document = DOMParser.parse(fileName);
                } catch (SAXException | ParserConfigurationException | IOException e) {
                    JOptionPane.showMessageDialog(Server.this, "Could not load", "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
                DOMParser.transformDocumentToData(document, registration.getData());
            }
        });

        exitButton.setText("Exit");
        exitButton.addActionListener(event -> System.exit(0));

        GroupLayout ButtonPanelLayout = new GroupLayout(buttonPanel);
        buttonPanel.setLayout(ButtonPanelLayout);
        ButtonPanelLayout.setHorizontalGroup(
                ButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(ButtonPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(startButton, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(stopButton, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(loadButton, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(exitButton, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(14, Short.MAX_VALUE))
        );
        ButtonPanelLayout.setVerticalGroup(
                ButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, ButtonPanelLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(ButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(startButton)
                                        .addComponent(stopButton)
                                        .addComponent(saveButton)
                                        .addComponent(loadButton)
                                        .addComponent(exitButton))
                                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(buttonPanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(labelHost, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hostTextField, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(labelPort, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(portTextField, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(labelParticipants)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(participantsTextField, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelHost, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(labelPort)
                                        .addComponent(labelParticipants)
                                        .addComponent(hostTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(portTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(participantsTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(16, 16, 16)
                                .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        this.setResizable(false);

        registration.getData().addDataParticipantListener(event -> {
            try {
                textArea.setText(registration.getInfo());
                participantsTextField.setText(String.valueOf(registration.getSize()));
            } catch (RemoteException ignored) {
            }

        });

        try {
            stub = (Conference) UnicastRemoteObject.exportObject(registration, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        pack();
        this.setLocation(0, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
    }

    private void startServer() throws RemoteException {
        if (registry == null) registry = LocateRegistry.createRegistry(Integer.parseInt(portTextField.getText()));
        String name = "Registrable";
        registry.rebind(name, stub);

        saveButton.setEnabled(false);
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        loadButton.setEnabled(false);
        portTextField.setEditable(false);
    }

    private void stopServer() throws NumberFormatException, RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(Integer.parseInt(portTextField.getText()));
        String name = "Registrable";
        registry.unbind(name);

        stopButton.setEnabled(false);
        startButton.setEnabled(true);
        saveButton.setEnabled(true);
        loadButton.setEnabled(true);
    }

    private void save(Document document, String fileName) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "Windows-1251");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(document), new StreamResult(new File(fileName)));
    }

}
