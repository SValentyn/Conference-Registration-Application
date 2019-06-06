package client;

import interfaces.Conference;
import interfaces.Participant;

import javax.swing.*;
import java.awt.*;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * The application is a window for sending the data of each new conference participant.
 * The basis is the use of RMI technology and Swing library.
 *
 * @author Syniuk Valentyn
 * @version 1.0
 */
public class Client extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField hostTextField;
    private JTextField portTextField;
    private JTextField participantsTextField;
    private JTextField nameTextField;
    private JTextField surnameTextField;
    private JTextField organizationTextField;
    private JTextField reportTextField;
    private JTextField emailTextField;

    private Conference stub;

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            Client client = new Client();
            client.setVisible(true);
            client.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - client.getWidth(), Toolkit.getDefaultToolkit().getScreenSize().height / 2 - client.getHeight() / 2);
        });
    }

    private Client() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton();
        JButton clearButton = new JButton();
        JButton getInfoButton = new JButton();
        JButton exitButton = new JButton();

        JPanel mainPanel = new JPanel();
        JLabel labelHost = new JLabel();
        JLabel labelPort = new JLabel();
        JLabel labelParticipants = new JLabel();
        JLabel labelName = new JLabel();
        JLabel labelSurname = new JLabel();
        JLabel labelOrganization = new JLabel();
        JLabel labelReport = new JLabel();
        JLabel labelEmail = new JLabel();

        hostTextField = new JTextField();
        portTextField = new JTextField();
        nameTextField = new JTextField();
        surnameTextField = new JTextField();
        organizationTextField = new JTextField();
        reportTextField = new JTextField();
        emailTextField = new JTextField();
        participantsTextField = new JTextField();

        labelHost.setText("Host:");
        hostTextField.setText("127.0.0.1");

        labelPort.setText("Port:");
        portTextField.setText("10100");

        labelParticipants.setText("Participants:");
        participantsTextField.setEditable(false);
        participantsTextField.setText("0");

        labelName.setText("Name:");
        labelSurname.setText("Surname:");
        labelOrganization.setText("Organization:");
        labelReport.setText("Report:");
        labelEmail.setText("Email:");

        registerButton.setText("Register");
        registerButton.addActionListener(event -> {
            try {
                registerParticipant();
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        });

        clearButton.setText("Clear");
        clearButton.addActionListener(event -> clear());

        getInfoButton.setText("Get info");
        getInfoButton.addActionListener(event -> {
            try {
                getInfo();
            } catch (RemoteException | NotBoundException e) {
                JOptionPane.showMessageDialog(Client.this, "Failed to get information", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        exitButton.setText("Finish");
        exitButton.addActionListener(event -> System.exit(0));

        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(getInfoButton);
        buttonPanel.add(exitButton);

        GroupLayout MainPanelLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(MainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(MainPanelLayout.createSequentialGroup()
                                        .addComponent(labelHost)
                                        .addGap(18, 18, 18)
                                        .addComponent(hostTextField, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(labelPort)
                                        .addGap(18, 18, 18)
                                        .addComponent(portTextField, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(labelParticipants)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(participantsTextField, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
                                .addGroup(MainPanelLayout.createSequentialGroup()
                                        .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(labelSurname)
                                                .addComponent(labelName)
                                                .addComponent(labelOrganization)
                                                .addComponent(labelReport)
                                                .addComponent(labelEmail))
                                        .addGap(29, 29, 29)
                                        .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(nameTextField)
                                                .addComponent(surnameTextField)
                                                .addComponent(organizationTextField)
                                                .addComponent(reportTextField, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                                                .addComponent(emailTextField))))
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MainPanelLayout.setVerticalGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelHost)
                                .addComponent(hostTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelPort)
                                .addComponent(portTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelParticipants)
                                .addComponent(participantsTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelName)
                                .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelSurname)
                                .addComponent(surnameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelOrganization)
                                .addComponent(organizationTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelReport)
                                .addComponent(reportTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelEmail)
                                .addComponent(emailTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(buttonPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        this.setResizable(false);
        pack();
    }

    private void registerParticipant() throws NumberFormatException, RemoteException, NotBoundException {
        try {
            String findName = "Registrable";
            Registry registry = LocateRegistry.getRegistry(hostTextField.getText(), Integer.parseInt(portTextField.getText()));
            stub = (Conference) registry.lookup(findName);

            participantsTextField.setText(String.valueOf(stub.register(getParticipantInfo())));
            JOptionPane.showMessageDialog(Client.this, "Thank you for registering!", "Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(Client.this, "Failed to register", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Participant getParticipantInfo() {
        String name = nameTextField.getText();
        String surname = surnameTextField.getText();
        String organization = organizationTextField.getText();
        String report = reportTextField.getText();
        String email = emailTextField.getText();
        return new Participant(name, surname, organization, report, email);
    }

    private void getInfo() throws NumberFormatException, RemoteException, NotBoundException {
        if (stub == null) {
            String findName = "Registrable";
            Registry registry = LocateRegistry.getRegistry(hostTextField.getText(), Integer.parseInt(portTextField.getText()));
            stub = (Conference) registry.lookup(findName);
        }
        participantsTextField.setText(String.valueOf(stub.getSize()));
        new TextClient(stub.getInfo()).setVisible(true);
    }

    private void clear() {
        nameTextField.setText("");
        surnameTextField.setText("");
        organizationTextField.setText("");
        reportTextField.setText("");
        emailTextField.setText("");
    }

}
