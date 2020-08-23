import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.plaf.metal.*; 

public class notepad extends JFrame
{
 
    private JMenuBar menuBar;

    
    private JMenu fileMenu;
    private JMenu fontMenu;
    private JMenu editMenu;

    
    private JMenuItem newItem;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem saveAsItem;
    private JMenuItem printItem;
    private JMenuItem exitItem;
    private JMenuItem cutItem;
    private JMenuItem copyItem;
    private JMenuItem pasteItem;
    private JMenuItem undoItem;
    private JMenuItem redoItem;
    private JMenuItem replaceItem;

    
    private JRadioButtonMenuItem monoItem;
    private JRadioButtonMenuItem serifItem;
    private JRadioButtonMenuItem sansSerifItem;

    
    private JCheckBoxMenuItem italicItem;
    private JCheckBoxMenuItem boldItem;
    private JCheckBoxMenuItem underlineItem;

    private String filename;     
    private JTextArea editorText;
    private JScrollPane scrollPane;
    private JPanel panel;
    private JTextField fromField = new JTextField(8);
    private JTextField toField = new JTextField(8);
    private final int NUM_LINES = 30;  // Lines to display
    private final int NUM_CHARS = 50;  // Chars per line
    private String Text = "";
    String str[] = new String[200];
    String temp = "";
    int i, m = 0;
    private final java.util.List<String> words;
    private static enum Mode {
        INSERT, COMPLETION
    };

    private Mode mode = Mode.INSERT;
    UndoManager undoManager = new UndoManager();
    
    public notepad()
    {
    	
        setTitle("Your Personal Text Editor");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        editorText = new JTextArea(NUM_LINES, NUM_CHARS);

        editorText.setLineWrap(true);
        editorText.setWrapStyleWord(true);

        // Set Undo-Redo limit upto 10
        undoManager.setLimit(1000);

        editorText.getDocument().addUndoableEditListener(new UndoableEditListener() 
            {
                public void undoableEditHappened(UndoableEditEvent evt) {
                    undoManager.addEdit(evt.getEdit());
                }
            });

        scrollPane=new JScrollPane(editorText);

        add(scrollPane, BorderLayout.CENTER);
         
        panel = new JPanel();
        
        add(panel, BorderLayout.SOUTH);
        
        panel.setVisible(false);

        
        buildMenuBar();

        
        editorText.getDocument().addDocumentListener(new TempFileListener());

        pack();
        setVisible(true);
                str = temp.split("[\\s]");

        words = new java.util.ArrayList<String>(str.length);
        for(String s : str)  {
            words.add(s);
        }
    }
    private void buildMenuBar()
    {
        buildFileMenu();
        buildEditMenu();
        buildFontMenu();
        menuBar = new JMenuBar();

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(fontMenu);


        setJMenuBar(menuBar);
    }

    

    private void buildFileMenu()
    {
     
        newItem = new JMenuItem("New");
       
        newItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.Event.CTRL_MASK));
      
        newItem.addActionListener(new NewListener());

      
        openItem = new JMenuItem("Open");
        openItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.Event.CTRL_MASK));;
        openItem.addActionListener(new OpenListener());

        // Create the Save menu item.
        saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.Event.CTRL_MASK));
        saveItem.addActionListener(new SaveListener());

        // Create the Save As menu item.
        saveAsItem = new JMenuItem("Save As");
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        saveAsItem.addActionListener(new SaveListener());

        // Create the Print menu item.
        printItem = new JMenuItem("Print");
        printItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.Event.CTRL_MASK));
        printItem.addActionListener(new PrintListener());

        // Create the Exit menu item.
        exitItem = new JMenuItem("Close");
        exitItem.setMnemonic(KeyEvent.VK_X);
        exitItem.addActionListener(new ExitListener());

        // Create a menu for the items we just created.
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        // Add the items and some separator bars to the menu.
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.addSeparator();// Separator bar
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();// Separator bar
        fileMenu.add(printItem);
        fileMenu.addSeparator();// Separator bar
        fileMenu.add(exitItem);
    }

    /**
    The buildFontMenu method creates the font menu and populates it with its menu items.
     */

    private void buildFontMenu()
    {
        // Create the Monospaced menu item.
        monoItem = new JRadioButtonMenuItem("Monospaced");
        monoItem.addActionListener(new FontListener());

        // Create the Serif menu item.
        serifItem = new JRadioButtonMenuItem("Serif");
        serifItem.addActionListener(new FontListener());

        // Create the SansSerif menu item.
        sansSerifItem = new JRadioButtonMenuItem("SansSerif", true);
        sansSerifItem.addActionListener(new FontListener());

        // Group the radio button menu items.
        ButtonGroup group = new ButtonGroup();
        group.add(monoItem);
        group.add(serifItem);
        group.add(sansSerifItem);

        // Create the Italic menu item.
        italicItem = new JCheckBoxMenuItem("Italic");
        italicItem.addActionListener(new FontListener());

        // Create the Bold menu item.
        boldItem = new JCheckBoxMenuItem("Bold");
        boldItem.addActionListener(new FontListener());

        underlineItem = new JCheckBoxMenuItem("Underline");
        underlineItem.addActionListener(new FontListener());

        // Create a menu for the items we just created.
        fontMenu = new JMenu("Font");
        fontMenu.setMnemonic(KeyEvent.VK_T);

        // Add the items and some separator bars to the menu.
        fontMenu.add(monoItem);
        fontMenu.add(serifItem);
        fontMenu.add(sansSerifItem);
        fontMenu.addSeparator();// Separator bar
        fontMenu.add(italicItem);
        fontMenu.add(boldItem);
        fontMenu.add(underlineItem);
    }

   

    private void buildEditMenu()
    {
        cutItem = new JMenuItem("Cut");
        cutItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.Event.CTRL_MASK));
        cutItem.addActionListener(new CutListener()); 

        copyItem = new JMenuItem("Copy");
        copyItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.Event.CTRL_MASK));
        copyItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    editorText.copy();
                }
            });

        pasteItem = new JMenuItem("Paste");
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.Event.CTRL_MASK));
        pasteItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    editorText.paste();
                }
            });

        undoItem = new JMenuItem("Undo");
        undoItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.Event.CTRL_MASK));
        editorText.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
        undoItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (undoManager.canUndo()) 
                        undoManager.undo();
                    else{
                        warn("Can't Undo");
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
            });

        redoItem = new JMenuItem("Redo");
        redoItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.Event.CTRL_MASK));
        editorText.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
        redoItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (undoManager.canRedo()) 
                        undoManager.redo();
                    else{
                        warn("Can't Redo");
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
            });

        replaceItem = new JMenuItem("Replace");
        replaceItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.Event.CTRL_MASK));
        replaceItem.addActionListener(new ReplaceListener()); 

        editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);

        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.addSeparator();// Separator bar
        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.addSeparator();// Separator bar
        editMenu.add(replaceItem);
    }

    private void warn(String msg)
    {
        JOptionPane.showMessageDialog(this, "Warning: " + msg, "Warning",JOptionPane.WARNING_MESSAGE);
    }

    /**
    Private inner class that handles the event that is generated when there is any change made in the document.
    This class createa a temporary file, helping to perform file recovery feature.
     */

    private class TempFileListener implements DocumentListener
    {
        public void changedUpdate(DocumentEvent ev){  }


        public void insertUpdate(DocumentEvent ev) 
        {
            boolean success;
            String Text="";
            FileWriter fwriter;
            PrintWriter outputFile;
            if (ev.getLength() != 1) {
                return;
            }
            int pos = ev.getOffset();
            String content = null;

            try {
                File file = new File(filename+"_Temp");
                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                content = editorText.getText(0, pos + 1);
                Text = editorText.getText();
                output.write(Text); // Write data in the file
                output.close();// Close the file

            } catch ( IOException ex ) {
                ex.printStackTrace();
            }
            catch (BadLocationException e)
            {
                e.printStackTrace();
            }
            int w;
            for (w = pos; w >= 0; w--) {
                if (!Character.isLetter(content.charAt(w))) {
                    break;
                }
            }
            if (pos - w < 2) {
                // Too few chars
                return;
            }

            String prefix = content.substring(w + 1).toLowerCase();

            Boolean suggestionFound = false;
            for(String suggestion : words)  {
                if(suggestion.contains(prefix) && suggestion.indexOf(prefix) == 0)  {
                    String completion = suggestion.substring(pos - w);
                    SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1));
                    break;
                }
            }
            if(!suggestionFound)  {
                // Nothing found
                mode = Mode.INSERT;
            }
        }
        private class CompletionTask implements Runnable {
            String completion;
            int position;

            CompletionTask(String completion, int position) {
                this.completion = completion;
                this.position = position;
            }

            public void run() {
                editorText.insert(completion, position);
                editorText.setCaretPosition(position + completion.length());
                editorText.moveCaretPosition(position);
                mode = Mode.COMPLETION;
            }
        }

    }


    private class CutListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            editorText.cut();
        }
    }


    private class PrintListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String text = editorText.getText();
           
    			try { 
    				// print the file 
    				editorText.print(); 
    			} 
    			catch (Exception evt) { 
    			//	JOptionPane.showMessageDialog(f, evt.getMessage()); 
    			}
        }
    }

  

    private class ReplaceListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            panel.setVisible(true);
            JButton replaceButton = new JButton("Replace");
            panel.add(replaceButton);
            replaceButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent evt) {
                        String from = fromField.getText();
                        int start = editorText.getText().indexOf(from);
                        if (start >= 0 && from.length() > 0)
                            editorText.replaceRange(toField.getText(), start, start + from.length());
                    }
                });
            // Adding the labels and textfields to the panel
            panel.add(fromField);
            panel.add(new JLabel("with"));
            panel.add(toField);
        }
    }

 

    private class NewListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            editorText.setText("");
            filename = null;
        }
    }

 
    private class OpenListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            int chooserStatus;

            JFileChooser chooser = new JFileChooser();
            chooserStatus = chooser.showOpenDialog(null);
            if (chooserStatus == chooser.APPROVE_OPTION)
            {
                File selectedFile = chooser.getSelectedFile();

                filename = selectedFile.getPath();

                if (!openFile(filename))
                {
                    JOptionPane.showMessageDialog(null,"Error reading " +filename, "Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        }

 
        private boolean openFile(String filename)
        {
            boolean success;
            String inputLine;
            FileReader freader;
            BufferedReader inputFile;

            try
            {
                // Open the file.
                freader = new FileReader(filename);
                inputFile = new BufferedReader(freader);
                Text="";
                editorText.setText("");
                while ((inputLine =inputFile.readLine()) != null)
                {
                    Text = Text + inputLine + "\n";
                    editorText.append(Text);
                }

                inputFile.close();  

                   success = true;
            }
            catch (IOException e)
            {
                success = false;
                            }
            return success;
        }
    }


    private class SaveListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            int chooserStatus;


            if (e.getActionCommand() == "Save As" || filename == null)
            {
                JFileChooser chooser = new JFileChooser();
                chooserStatus = chooser.showSaveDialog(null);
                if (chooserStatus == JFileChooser.APPROVE_OPTION)
                {
                    File selectedFile = chooser.getSelectedFile();

                    filename = selectedFile.getPath();
                }
            }

            if (!saveFile(filename))
            {
                JOptionPane.showMessageDialog(null,"Error saving " + filename, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }


        private boolean saveFile(String filename)
        {
            boolean success;
            String Text;
            FileWriter fwriter;
            PrintWriter outputFile;

            try
            {
                // Open the file.
                fwriter = new FileWriter(filename);
                outputFile = new PrintWriter(fwriter);

                Text = editorText.getText();
                outputFile.print(Text);

                // Close the file.
                outputFile.close();

                success = true;
            }
            catch (IOException e)
            {
                success = false;
            }

            return success;
        }
    }


    private class ExitListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }


    private class FontListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // Get the current font.
            Font textFont = editorText.getFont();

            String fontName = textFont.getName();
            int fontSize = textFont.getSize();

            int fontStyle = Font.PLAIN;

            if (monoItem.isSelected())
                fontName = "Monospaced";
            else if (serifItem.isSelected())
                fontName = "Serif";
            else if (sansSerifItem.isSelected())
                fontName = "SansSerif";

            if (italicItem.isSelected())
                fontStyle += Font.ITALIC;

            if (boldItem.isSelected())
                fontStyle += Font.BOLD;


            editorText.setFont(new Font(fontName, fontStyle, fontSize));
        }
    }


    public static void main(String[] args)
    {
        notepad te = new notepad();
    }
}
