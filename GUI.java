import javax.swing.*;
import javax.imageio.ImageIO;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JComboBox;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class GUI extends JFrame implements ActionListener, DocumentListener, KeyListener{
	
	// Main panel of the GUI
    private JPanel panel = new JPanel();
    
    // subPanels of the GUI
    private JPanel subPanelNORTH = new JPanel();
    private CenterPanel subPanelCENTER = new CenterPanel();
    private JPanel subPanelSOUTH = new JPanel();
    
    // title and other information that would be displayed on the GUI
    private JLabel titleLABEL = new JLabel("AutoComplete");
    private JLabel numberOfMatches = new JLabel();
    private JLabel numberDisplay = new JLabel();

    // ComboBox that would show the suggestions
    // Text field that allows user to enter text
    private JComboBox<String> comboBox = new JComboBox<String>();
    private JTextField textField = new JTextField(10);

    // The initial size of GUI
    // But the GUI is resizable
    private final int height = 450;
    private final int width = 650;

    // Background image
    private BufferedImage img;

    // The total terms in the file
    //ArrayList<Term> terms = new ArrayList<>();
    Autocomplete autocomplete;
    Term[] terms;
    Autocomplete autocompleteHistory;
    
    // The total matching terms to user input
    Term[] results;

    // The maximum results that user could get
    int maxDisplay;
    
    // Writers for writing the history
    // into a text file
    FileWriter fw;
    BufferedWriter bw;
    PrintWriter pw;
    
    String suggestions = "Suggestions";

    public GUI(){

    	// Read in the background image
        try {
            img = ImageIO.read(new File("bg.png"));
            
            // The writers help to write the histories into the file
            fw = new FileWriter("history.txt", true);
        	bw = new BufferedWriter(fw);
        	pw = new PrintWriter(bw);
        	pw.println("");
        	pw.println("-- History --");
        } catch(IOException e) {
            e.printStackTrace();
        }
        // Layout managers for GUI
        panel.setLayout(new BorderLayout(0, 0));
        subPanelCENTER.setLayout(new GridBagLayout());
        subPanelSOUTH.setLayout(new BorderLayout(0, 0));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        // Set title
        subPanelNORTH.add(titleLABEL);
        titleLABEL.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 40));
        titleLABEL.setForeground(Color.WHITE);
        
        // Set other information
        subPanelSOUTH.add(numberOfMatches, BorderLayout.WEST);
        numberOfMatches.setText("Number of matches: 0");
        numberOfMatches.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 10));
        numberOfMatches.setForeground(Color.WHITE);

        // The position of the text field
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        subPanelCENTER.setBorder(BorderFactory.createEmptyBorder(0,80,100,80));
        subPanelCENTER.add(textField, c);
        subPanelCENTER.add(Box.createVerticalStrut(100));

        // The position of the combo box
        c.gridx = 0;
        c.gridy = 1;
        comboBox.setEditable(false);
        comboBox.setVisible(true);
        comboBox.setSelectedIndex(-1);
        comboBox.setMaximumRowCount(9);
        comboBox.addItem("Suggestions");
        subPanelCENTER.add(comboBox, c);

        // Add listeners to the text field and the combo box
        textField.addActionListener(this);
        textField.getDocument().addDocumentListener(this);
        textField.addKeyListener(this);

        comboBox.addActionListener(this);

        // Set the colours to the GUI
        subPanelNORTH.setOpaque(true);
        subPanelNORTH.setBackground(Color.DARK_GRAY);
        subPanelSOUTH.setBackground(Color.DARK_GRAY);
        panel.add(subPanelNORTH, BorderLayout.NORTH);
        panel.add(subPanelCENTER, BorderLayout.CENTER);
        panel.add(subPanelSOUTH, BorderLayout.SOUTH);

        this.add(panel);
        this.setTitle("AutoComplete");
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    
    // Display the maximum number of searching
    // results that can be displayed
    public void setMaximumDisplay(int max) {
    	subPanelSOUTH.add(numberDisplay, BorderLayout.EAST);
        numberDisplay.setText("Maximum searching number displayed: " + max);
        numberDisplay.setFont(new Font("Sans-Serif", Font.CENTER_BASELINE, 10));
        numberDisplay.setForeground(Color.WHITE);
    }

    // ActionListener for autocomplete in text field
    @Override
    public void actionPerformed(ActionEvent e){
            if(comboBox.getSelectedItem() != null && 
            		!comboBox.getSelectedItem().toString().equals("Suggestions")) 
            {
                textField.getDocument().removeDocumentListener(this);
                textField.setText(((String) comboBox.getSelectedItem()));
                textField.getDocument().addDocumentListener(this);
                // Write the records to a text file shows the
                // the words that user has chose
                pw.println((String) comboBox.getSelectedItem());
                pw.flush();
                comboBox.hidePopup();
                comboBox.removeAllItems();
                numberOfMatches.setText("number of matches: 1");
            }
    }

    // The suggestions that would be displayed
    // in the combo box
    public void displayResults(Term[] results) {
    	int numberOfResults = maxDisplay;
        comboBox.removeAllItems();
        comboBox.addItem(suggestions);
        
        if(numberOfResults > results.length) {
        	numberOfResults = results.length;
        }
        
        for(int i = 0; i < numberOfResults; i++) {
            comboBox.addItem(results[i].toString());
        }
    }

    // It would be executed when user entering
    // text into the text field
    @Override
    public void insertUpdate(DocumentEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // time for testing performance
                    long time1 = System.nanoTime();

                	if(!textField.getText().equals("")) {
                    results = autocomplete.allMatches(Practical1.replaceCharacters(textField.getText().toLowerCase()));
                    displayResults(results);
                    numberOfMatches.setText("number of matches: " + results.length);
                	}
                	else {
                		comboBox.removeAllItems();
                		comboBox.addItem(suggestions);
                	}
                    if(comboBox.getItemCount() == 1) {
                        comboBox.hidePopup();
                    }
                    else {
                    	comboBox.hidePopup();
                        comboBox.showPopup();
                    }
                    // time for testing performance
                    long time2 = System.nanoTime();
                	System.out.println("Time in ms: " + (time2-time1)/1000000);
                }
                catch(StringIndexOutOfBoundsException s) {
                    comboBox.hidePopup();
                }
            }
        });
    }

    // It would be executed when user removing
    // text from the text field
    @Override
    public void removeUpdate(DocumentEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // time for testing performance
                long time1 = System.nanoTime();

            	if(!textField.getText().equals("")) {
                results = autocomplete.allMatches(Practical1.replaceCharacters(textField.getText().toLowerCase()));
                displayResults(results);
                numberOfMatches.setText("number of matches: " + results.length);
            	}
            	else {
            		comboBox.removeAllItems();
            		comboBox.addItem(suggestions);
            		numberOfMatches.setText("number of matches: 0");
            	}
                if(textField.getText().equals("") || comboBox.getItemCount() == 1) {
                    comboBox.hidePopup();
                }
                else {
                	comboBox.hidePopup();
                    comboBox.showPopup();
                }

                // time for testing performance
                long time2 = System.nanoTime();
                System.out.println("Time in ms: " + (time2-time1)/1000000);
            }
        });
    }

    // No use in this practical
    // But have to implement this as this is an abstract method in the interface
    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    // It would be executed when user typed up, down and enter keys
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
	if(comboBox.isPopupVisible()) {
		if(keyCode == KeyEvent.VK_ENTER) {
			comboBox.setSelectedIndex(comboBox.getSelectedIndex());
		}
		
		comboBox.removeActionListener(this);
		if(keyCode == KeyEvent.VK_UP) {
			
			if(comboBox.getSelectedIndex() != 0) {
				comboBox.setSelectedIndex(comboBox.getSelectedIndex() - 1);	
			}
		}
		else if(keyCode == KeyEvent.VK_DOWN) {
			if(results.length < maxDisplay) {
				if(comboBox.getSelectedIndex() != results.length) {
					comboBox.setSelectedIndex(comboBox.getSelectedIndex() + 1);
				}
			}
				else {
					if(comboBox.getSelectedIndex() != maxDisplay) {
						comboBox.setSelectedIndex(comboBox.getSelectedIndex() + 1);
				}
			}
		}
		comboBox.addActionListener(this);
	}
}

	// No use in this practical
	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	// No use in this practical
	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	// The class extends from JPanel
	// that overrides the paintCOmponent method
	// in order to display the background image
	private class CenterPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }
    }
}