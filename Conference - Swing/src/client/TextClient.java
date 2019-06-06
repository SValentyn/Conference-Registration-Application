package client;

import javax.swing.*;

class TextClient extends JFrame {

    private static final long serialVersionUID = 1L;

    TextClient(String text) {
        initComponents(text);
    }

    private void initComponents(String text) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JScrollPane jScrollPane = new JScrollPane();
        JTextArea mainArea = new JTextArea();
        JLabel label = new JLabel();

        mainArea.setColumns(20);
        mainArea.setRows(5);
        jScrollPane.setViewportView(mainArea);

        label.setText("Info:");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(label, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(277, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 11, Short.MAX_VALUE)
                        .addComponent(label)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane, GroupLayout.PREFERRED_SIZE, 276, GroupLayout.PREFERRED_SIZE))
        );

        mainArea.setText(text);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

}
