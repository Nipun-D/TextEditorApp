package com.nipun;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextEditor implements ActionListener {

    private static JFrame frame;
    private static JTextArea textArea;
    private static JScrollPane scrollPane;
    private static JSpinner fontSizeSpinner;
    private static JLabel fontSizeLabel;
    private static JButton fontColourButton;
    private static JComboBox fontBox;
    private static JMenuBar menuBar;
    private static JMenu fileMenu;
    private static JMenuItem openItem, saveItem, exitItem;

    public static void main(String[] args) {

        //Creating the GUI frame
	    frame = new JFrame("Text Editor App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 520);
        frame.setLayout(new FlowLayout());
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); //when program is running GUI appears in the center of the screen

        //Creating the text area
        textArea = new JTextArea();
        //textArea.setPreferredSize(new Dimension(450,450)); Not needed as the size is being set on the scrollPane
        textArea.setLineWrap(true); //when the text reaches the end of the the text area it will move onto the next line
        textArea.setWrapStyleWord(true); //if a word reaches the edge it will be moved to a new line
        textArea.setFont(new Font("Arial", Font.PLAIN, 20 )); //Default font style and size

        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(480,400));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED); //scroll pane only appear if the text goes beyond the given dimensions

        //Allow user to change the font size
        fontSizeLabel = new JLabel("Font");
        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
        fontSizeSpinner.setValue(20);
        fontSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN,(int) fontSizeSpinner.getValue()));
            }
        });

        fontColourButton = new JButton("Font Colour");
        fontColourButton.addActionListener(new TextEditor());

        //Gathers all available fonts within java and puts them into the font array
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontBox = new JComboBox(fonts);
        fontBox.addActionListener(new TextEditor());
        fontBox.setSelectedItem("Arial");

        //MenuBar
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        openItem.addActionListener(new TextEditor());
        saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new TextEditor());
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new TextEditor());
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        frame.setJMenuBar(menuBar);
        frame.add(scrollPane);
        frame.add(fontSizeLabel);
        frame.add(fontSizeSpinner);
        frame.add(fontColourButton);
        frame.add(fontBox);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
       
        JFileChooser fileChooser;
        File file;
        int openResponse,saveResponse;
        Scanner fileOpen = null;
        PrintWriter fileOut = null;

        //Action for changing font colour
        if (e.getSource() == fontColourButton){

            Color color = JColorChooser.showDialog(null, "Choose a colour", Color.BLACK);
            textArea.setForeground(color);
        }

        //Action for changing to the chosen fot style
        if (e.getSource() == fontBox){

            textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
        }

        //Action for opening a file
        if (e.getSource() == openItem){

            fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("D:\\GUi save location")); //Default open location
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt"); //Filtering .txt
            fileChooser.setFileFilter(filter);
            openResponse = fileChooser.showOpenDialog(null);

            if (openResponse == JFileChooser.APPROVE_OPTION){

                file = new File(fileChooser.getSelectedFile().getAbsolutePath());

                try {
                    fileOpen = new Scanner(file);
                    if(file.isFile()){
                        while (fileOpen.hasNextLine()){
                            String line = fileOpen.nextLine() + "\n";
                            textArea.append(line);
                        }
                    }
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } finally {
                    fileOpen.close();
                }

            }

        }

        //Action for saving a file
        if (e.getSource() == saveItem){

            fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("D:\\GUi save location")); //Default save location
            saveResponse = fileChooser.showSaveDialog(null);

            if (saveResponse == JFileChooser.APPROVE_OPTION){

                file = new File(fileChooser.getSelectedFile().getAbsolutePath());

                try {
                    fileOut = new PrintWriter(file);
                    fileOut.println(textArea.getText());
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } finally {
                    fileOut.close();
                }

            }
        }

        //Exiting the application
        if (e.getSource() == exitItem){

             System.exit(0);

        }

    }
}
