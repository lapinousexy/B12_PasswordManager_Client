package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.file.ServerDriver;
import ch.heigvd.pro.client.password.PasswordGenerator;
import ch.heigvd.pro.client.structure.Entry;
import ch.heigvd.pro.client.structure.Safe;
import com.google.gson.annotations.Expose;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Date;

public class EntryGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel westPanel;
    private JPanel eastPanel;
    private JPanel southPanel;
    private JPanel centerPanel;
    private JTextArea notesField;
    private JFormattedTextField usernameField;
    private JFormattedTextField entryNameField;
    private JPasswordField passwordField;
    private JPasswordField RetypePasswordField;
    private JFormattedTextField targetField;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton showButton;
    private JButton autoGenerateButton;
    private JProgressBar progressBar1;
    private JLabel imageLabel;
    private JButton browseButton;
    private JButton deleteButton;

    private String iconFilename;

    public EntryGUI(Safe safe, int folderNumber, int entryNumber, HomePageGUI homepage, ServerDriver serverDriver) {

        /**
         * TO SEE : regarder de -1 bizarre
         */
        if (entryNumber != -1) {
            this.iconFilename = safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).getIcon();

            // TODO Resize image
            ImageIcon myPicture = new ImageIcon(iconFilename);
            imageLabel.setIcon(myPicture);

            CharBuffer charBuffer = CharBuffer.wrap(safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).getEntryName());
            entryNameField.setText(charBuffer.toString());

            charBuffer = CharBuffer.wrap(safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).getTarget());
            targetField.setText(charBuffer.toString());

            charBuffer = CharBuffer.wrap(safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).getUsername());
            usernameField.setText(charBuffer.toString());

            charBuffer = CharBuffer.wrap(safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).getClearPassword());
            passwordField.setText(charBuffer.toString());

            charBuffer = CharBuffer.wrap(safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).getClearPassword());
            RetypePasswordField.setText(charBuffer.toString());

            charBuffer = CharBuffer.wrap(safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).getNotes());
            notesField.setText(charBuffer.toString());

            // Wiping sensible data
            Arrays.fill(charBuffer.array(), (char) 0);
        }

        /**
         * Initialize frame
         */
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("javaIcone.png")));
        setTitle("Entry");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(600, 450);
        setResizable(false);
        //pack();
        SwingUtilities.getRootPane(saveButton).setDefaultButton(saveButton);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        progressBar1.setValue(50);
        setVisible(true);

        /**
         * On click on button show it will show the password
         */
        showButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                passwordField.setEchoChar((char) 0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                passwordField.setEchoChar('*');
            }
        });

        deleteButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    serverDriver.deleteEntry(safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).getIdPassword());
                    safe.getFolderList().get(folderNumber).removeEntry(entryNumber);
                    JOptionPane.showMessageDialog(null,
                            "The entry has been deleted",
                            "Delete password",
                            JOptionPane.INFORMATION_MESSAGE);
                    homepage.setEnabled(true);
                    dispose();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            e.getMessage(),
                            "Error : Delete password",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        /**
         * Show password
         */
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (passwordField.getPassword().length != 0) {
                    RetypePasswordField.setEditable(true);
                }

                if (RetypePasswordField.getPassword().length != 0) {
                    RetypePasswordField.setText("");
                }
            }
        });

        /**
         *
         */
        /*RetypePasswordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                char[] pass1 = passwordField.getPassword();
                char[] pass2 = RetypePasswordField.getPassword();
                boolean different = false;

                // Check same length
                if (pass1.length == pass2.length) {
                    // CHeck same length and same chars
                    for (int i = 0; i < pass1.length; ++i) {
                        if (pass1[i] != pass2[i]) {
                            different = true;
                        }
                    }
                } else {
                    different = true;
                }

                if (different) {
                    JOptionPane.showMessageDialog(null, "Passwords must be the same");
                    passwordField.setText("");
                    RetypePasswordField.setText("");
                }
                RetypePasswordField.setEditable(false);

                // TODO : Clean with Array.fill()
                // Clean password arrays
                for (int i = 0; i < pass1.length; i++) {
                    pass1[i] = '\0';
                }

                for (int i = 0; i < pass2.length; i++) {
                    pass2[i] = '\0';
                }
            }
        });*/

        /**
         * Create the entry
         */
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try{
                    // Verify that all fields are filled
                    if (!usernameField.getText().isEmpty()
                            && passwordField.getPassword().length != 0
                            && RetypePasswordField.getPassword().length != 0
                            && !targetField.getText().isEmpty()
                            && !entryNameField.getText().isEmpty() && entryNumber == -1) {

                        // Create a new Entry
                        Entry newEntry = new Entry(0, entryNameField.getText().toCharArray(),
                                usernameField.getText().toCharArray(), targetField.getText().toCharArray(),
                                passwordField.getPassword(), notesField.getText().toCharArray(), new Date());

                        newEntry.setIcon(iconFilename);
                        safe.getFolderList().get(folderNumber).addEntry(newEntry);

                        // Add the entry to the server
                        if(serverDriver != null){
                            serverDriver.addEntry(newEntry, safe.getFolderList().get(folderNumber).getId(), safe);
                        }

                        JOptionPane.showMessageDialog(null,
                                "The entry has been created",
                                "Created entry",
                                JOptionPane.INFORMATION_MESSAGE);

                        homepage.InitGroupTree();
                        homepage.refreshTable();
                        homepage.setEnabled(true);

                        dispose();

                        } else if (entryNumber != -1) {
                        safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).setUsername(usernameField.getText().toCharArray());
                        safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).setClearPassword(passwordField.getPassword());
                        safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).setTarget(targetField.getText().toCharArray());
                        safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).setNotes(notesField.getText().toCharArray());
                        safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).setIcon(iconFilename);
                    }

                } catch (Exception e) {
                e.printStackTrace();
            }

            }
        });

        /**
         * Generate password
         */
        autoGenerateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PasswordGenerator passwordGenerator = new PasswordGenerator("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"#$%&')(*+,-_./".toCharArray(), 15);
                char[] generatedPassword = passwordGenerator.generatePassword();
                passwordField.setText(String.valueOf(generatedPassword));
                RetypePasswordField.setText(String.valueOf(generatedPassword));
                Arrays.fill(generatedPassword, (char) 0);
            }
        });

        /**
         * Get icone picture
         */
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                int ret = chooser.showOpenDialog(null);

                if (ret == JFileChooser.APPROVE_OPTION) {
                    String filename = chooser.getSelectedFile().getAbsolutePath();

                    ImageIcon myPicture = new ImageIcon(filename);
                    setIconFilename(filename);
                    imageLabel.setIcon(myPicture);
                }
            }
        });
    }

    private void createUIComponents() {
        // Source: https://stackoverflow.com/questions/10051638/updating-an-image-contained-in-a-jlabel-problems
        ImageIcon myPicture = new ImageIcon(iconFilename);
        imageLabel = new JLabel();
        imageLabel.setIcon(myPicture);
    }

    // Getters
    public JButton getCancelButton() {
        return cancelButton;
    }

    public void setIconFilename(String iconFilename) {
        this.iconFilename = iconFilename;
    }

}
