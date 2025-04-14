/**
 *                          FINAL YEAR PROJECT
 *                          -------------------
 *   This is the final year student project,this project was written
 *   from scratch by Bakari Said who is the Student at the Open University of Tanzania.
 *   the main goal of this project is to demonstrate student ability on the
 *   knowledge he has gained throught out his study session.
 *
 *   the following are credentials of the author of this work:
 *
 *   Name: Bakari Said
 *   Email: chanogab@gmail.com
 *   Contacts: +255716162784/+255742863986
 *
 */
package com.programmer;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;

public class Main {

    //**********************************************
    //      create main components for our program
    //**********************************************
    JFrame window = new JFrame();
    private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    private JPanel labelPanel;
    private JLabel caretLabel = new JLabel("Status:");
    private JSlider slider = new JSlider(10,100,10);;
    private JToolBar toolBar;
    private JTextPane textPane;
    private JScrollPane scrollPane;
    private JFileChooser fileChooser = new JFileChooser();
    private AbstractDocument document;
    private StyledDocument styledDocument;
    private UndoManager undoManager = new UndoManager();
    UndoAction undoAction = new UndoAction();
    RedoAction redoAction = new RedoAction();
    HashMap<Object,Action> actions;

    static private Main main = new Main();
    //*********************
    //  create and show GUI
    //*********************
    private void showGui(){
        //image icon
        ImageIcon windowIcon = new ImageIcon(
                main.getClass().getResource("resources/openU.png")
        );
        window.getContentPane().add(new MainPanel(),BorderLayout.CENTER);
        labelPanel = new JPanel(new BorderLayout(1,5));
        labelPanel.add(createOpacity(),BorderLayout.CENTER);
        labelPanel.add(caretLabel,BorderLayout.PAGE_END);

        window.add(createToolBar(),BorderLayout.PAGE_START);
        window.add(labelPanel,BorderLayout.PAGE_END);
        window.setVisible(true);
        window.setIconImage(windowIcon.getImage());
        window.setTitle("JEditor");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setMinimumSize(new Dimension(300,200));
        window.pack();

    }

    private class MainPanel extends JPanel{

        //constructor
        private MainPanel(){
            super(new BorderLayout(0,0));
            tabbedPaneProperties(); //invoke tabbed pane properties method
            setPreferredSize(new Dimension(700,800));
            setBackground(Color.gray);
            add(tabbedPane,BorderLayout.CENTER);
        }
    }
    //******************************************************
    //Create the properties of tabbed pane
    //******************************************************
    private void tabbedPaneProperties(){
        //add initial tab
        tabbedPane.setPreferredSize(new Dimension(500,500));
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addChangeListener(new MyChangeListener());
        tabbedPane.addContainerListener(new MyContainerListener());
    }

    private JSlider createOpacity(){
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        slider.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //get device graphics environment
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gd = ge.getDefaultScreenDevice();
                //if device doesn't support translucent exit the program
                if (!gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT))
                    System.exit(1);

                    //else if device supports translucent then continue
                else{
                    if (e.getSource() == slider) {
                        switch (slider.getValue()) {
                            case 10:
                                window.setOpacity(1.0f);
                                break;
                            case 15:
                                window.setOpacity(0.95f);
                                break;
                            case 20:
                                window.setOpacity(0.90f);
                                break;
                            case 25:
                                window.setOpacity(0.85f);
                                break;
                            case 30:
                                window.setOpacity(0.80f);
                                break;
                            case 35:
                                window.setOpacity(0.75f);
                                break;
                            case 40:
                                window.setOpacity(0.70f);
                                break;
                            case 45:
                                window.setOpacity(0.65f);
                                break;
                            case 50:
                                window.setOpacity(0.60f);
                                break;
                            case 55:
                                window.setOpacity(0.55f);
                                break;
                            case 60:
                                window.setOpacity(0.50f);
                                break;
                            case 65:
                                window.setOpacity(0.45f);
                                break;
                            case 70:
                                window.setOpacity(0.40f);
                                break;
                            case 75:
                                window.setOpacity(0.35f);
                                break;
                            case 80:
                                window.setOpacity(0.30f);
                                break;
                            case 85:
                                window.setOpacity(0.25f);
                                break;
                            case 90:
                                window.setOpacity(0.20f);

                            case 95:
                                window.setOpacity(0.15f);
                                break;
                            case 100:
                                window.setOpacity(0.10f);
                                break;
                            default:
                                break;
                        }
                    }
                }

            }
        });
        return slider;
    }
    private JToolBar createToolBar(){
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.addSeparator(new Dimension(5,1));
        toolBar.setLayout(new FlowLayout(FlowLayout.RIGHT,5,1));
        toolBar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toolBar.setBackground(new Color(255,245,211));
        toolBar.setBorderPainted(true);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(255,240,245));
        menuBar.add(createStyleMenu());
        menuBar.add(settingMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createFileMenu());

        toolBar.add(newButton());
        toolBar.add(openButton());
        toolBar.add(saveButton());
        toolBar.add(copyButton());
        toolBar.add(pasteButton());
        toolBar.add(undoButton());
        toolBar.add(redoButton());
        toolBar.add(cutButton());
        toolBar.add(exitButton());
        toolBar.add(menuBar);
        return toolBar;
    }
    private JMenu createFileMenu(){
        JMenu fileMenu = new JMenu("File ");
        fileMenu.setFont(new Font("Monospaced",Font.PLAIN,17));
        fileMenu.setForeground(Color.lightGray);

        JMenuItem newFile = new JMenuItem(" New File ");
        ImageIcon newFileIcon = new ImageIcon(main.getClass().
                getResource("resources/new.png"));
        newFile.setIcon(newFileIcon);
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
        newFile.setBackground(Color.darkGray);
        newFile.setForeground(Color.white);
        newFile.setFont(new Font("Monospaced",Font.PLAIN,16));
        newFile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //register newFile menu item to the action listener
        newFile.addActionListener(new MyActionListener());

        JMenuItem open = new JMenuItem(" Open  ");
        ImageIcon openIcon = new ImageIcon(main.getClass().
                getResource("resources/open.png"));
        open.setIcon(openIcon);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        open.setBackground(Color.darkGray);
        open.setForeground(Color.white);
        open.setFont(new Font("Monospaced",Font.PLAIN,16));
        open.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        open.addActionListener(new MyOpenFileListener());

        JMenuItem save = new JMenuItem(" Save ");
        ImageIcon saveIcon = new ImageIcon(main.getClass().
                getResource("resources/save.png"));
        save.setIcon(saveIcon);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
        save.setBackground(Color.darkGray);
        save.setForeground(Color.white);
        save.setFont(new Font("Monospaced",Font.PLAIN,16));
        save.addActionListener((e) -> saveFile());
        save.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JMenuItem save_as = new JMenuItem(" Save as");
        ImageIcon save_as_Icon = new ImageIcon(main.getClass().
                getResource("resources/saveas.png"));
        save_as.setIcon(save_as_Icon);
        save_as.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
        save_as.setBackground(Color.darkGray);
        save_as.setForeground(Color.white);
        save_as.setFont(new Font("Monospaced",Font.PLAIN,16));
        save_as.addActionListener((e) -> saveFile());
        save_as.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JMenuItem exit = new JMenuItem(" Exit");
        ImageIcon exitIcon = new ImageIcon(main.getClass().
                getResource("resources/exit.png"));
        exit.setIcon(exitIcon);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,ActionEvent.CTRL_MASK));
        exit.setBackground(Color.darkGray);
        exit.setForeground(Color.white);
        exit.setFont(new Font("Monospaced",Font.PLAIN,16));
        exit.addActionListener((e) -> System.out.println("Open menu clicked"));
        exit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        fileMenu.add(newFile);
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(save_as);
        fileMenu.add(exit);
        return fileMenu;
    }

    private JMenu createEditMenu(){
        JMenu editMenu = new JMenu("Edit ");
        editMenu.setFont(new Font("Monospaced",Font.PLAIN,17));
        editMenu.setForeground(Color.lightGray);

        //menu items
        JMenuItem undo = new JMenuItem(undoAction);
        undo.setText(" Undo ");
        ImageIcon undoIcon = new ImageIcon(main.getClass().
                getResource("resources/undo.png"));
        undo.setIcon(undoIcon);
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,ActionEvent.CTRL_MASK));
        undo.setBackground(Color.darkGray);
        undo.setForeground(Color.white);
        undo.setFont(new Font("Monospaced",Font.PLAIN,16));
        undo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        undo.addActionListener(undoAction);
        JMenuItem redo = new JMenuItem(redoAction);
        redo.setText(" Redo");
        ImageIcon redoIcon = new ImageIcon(main.getClass().
                getResource("resources/redo.png"));
        redo.setIcon(redoIcon);
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,ActionEvent.CTRL_MASK));
        redo.setBackground(Color.darkGray);
        redo.setForeground(Color.white);
        redo.setFont(new Font("Monospaced",Font.PLAIN,16));
        redo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        redo.addActionListener(redoAction);

        JMenuItem copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText(" Copy ");
        ImageIcon copyIcon = new ImageIcon(main.getClass().
                getResource("resources/copy.png"));
        copy.setIcon(copyIcon);
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
        copy.setBackground(Color.darkGray);
        copy.setForeground(Color.white);
        copy.setFont(new Font("Monospaced",Font.PLAIN,16));
        copy.addActionListener((e) -> System.out.println("Open menu clicked"));
        copy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JMenuItem paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText(" Paste ");
        ImageIcon pasteIcon = new ImageIcon(main.getClass().
                getResource("resources/paste.png"));
        paste.setIcon(pasteIcon);
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK));
        paste.setBackground(Color.darkGray);
        paste.setForeground(Color.white);
        paste.setFont(new Font("Monospaced",Font.PLAIN,16));
        paste.addActionListener((e) -> System.out.println("Open menu clicked"));
        paste.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JMenuItem cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setText(" Cut ");
        ImageIcon cutIcon = new ImageIcon(main.getClass().
                getResource("resources/cut.png"));
        cut.setIcon(cutIcon);
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
        cut.setBackground(Color.darkGray);
        cut.setForeground(Color.white);
        cut.setFont(new Font("Monospaced",Font.PLAIN,16));
        cut.addActionListener((e) -> System.out.println("Open menu clicked"));
        cut.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        editMenu.add(undo);editMenu.add(redo);
        editMenu.add(copy);editMenu.add(paste);
        editMenu.add(cut);
        return editMenu;
    }

    private JMenu settingMenu(){
        JMenu setting = new JMenu("Settings ");
        setting.setFont(new Font("Monospaced",Font.PLAIN,17));
        setting.setForeground(Color.lightGray);

        JMenuItem bgColor = new JMenuItem(" Background Color");

        bgColor.addActionListener((e) -> textPane.
                setBackground(JColorChooser.
                        showDialog(window,"White",Color.gray)));

        setting.add(bgColor);
        return setting;
    }
    //Create the style menu.
    protected JMenu createStyleMenu() {
        JMenu menu = new JMenu("Style");
        menu.setFont(new Font("Monospaced",Font.PLAIN,17));
        menu.setForeground(Color.lightGray);

        Action action = new StyledEditorKit.BoldAction();
        action.putValue(Action.NAME, "Bold");
        menu.add(action);

        action = new StyledEditorKit.ItalicAction();
        action.putValue(Action.NAME, "Italic");
        menu.add(action);

        action = new StyledEditorKit.UnderlineAction();
        action.putValue(Action.NAME, "Underline");
        menu.add(action);

        menu.addSeparator();

        menu.add(new StyledEditorKit.FontSizeAction("12", 12));
        menu.add(new StyledEditorKit.FontSizeAction("14", 14));
        menu.add(new StyledEditorKit.FontSizeAction("18", 18));
        menu.add(new StyledEditorKit.FontSizeAction("20",20));
        menu.add(new StyledEditorKit.FontSizeAction("25",25));
        menu.add(new StyledEditorKit.FontSizeAction("30",30));
        menu.add(new StyledEditorKit.FontSizeAction("35",35));
        menu.addSeparator();

        menu.add(new StyledEditorKit.FontFamilyAction("Serif",
                "Serif"));
        menu.add(new StyledEditorKit.FontFamilyAction("SansSerif",
                "SansSerif"));

        menu.addSeparator();

        menu.add(new StyledEditorKit.ForegroundAction("Red",
                Color.red));
        menu.add(new StyledEditorKit.ForegroundAction("Green",
                Color.green));
        menu.add(new StyledEditorKit.ForegroundAction("Blue",
                Color.blue));
        menu.add(new StyledEditorKit.ForegroundAction("Black",
                Color.black));
        menu.add(new StyledEditorKit.ForegroundAction("White",Color.white));

        return menu;
    }

    //*************************************************
    // create buttons for copying,pasting,open,exit etc
    //*************************************************
    private JButton openButton(){
        JButton open = new JButton();
        ImageIcon openIcon = new ImageIcon(main.getClass().
                getResource("resources/open.png"));
        open.setIcon(openIcon);
        open.setBackground(Color.gray);
        open.setBorder(BorderFactory.createEmptyBorder());
        open.setToolTipText("Open a file");
        open.addActionListener(new MyOpenFileListener());

        //open.addActionListener(new OpenFileActionListener());
        return open;
    }

    private class MyOpenFileListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if (tabbedPane.getTabCount() == 0){
                JOptionPane.showMessageDialog(null,
                        "To Open a new file,first create a new tab by clicking the new tab button");
            }else
                openFile();
        }
    }
    private JButton newButton(){
        JButton newFile = new JButton();
        ImageIcon openIcon = new ImageIcon(main.getClass().
                getResource("resources/new.png"));
        newFile.setIcon(openIcon);
        newFile.setBackground(Color.gray);
        newFile.setBorder(BorderFactory.createEmptyBorder());
        newFile.setToolTipText("create new File");
        newFile.addActionListener(new MyActionListener());
        return newFile;
    }

    private JButton saveButton(){
        JButton save = new JButton();
        ImageIcon saveIcon = new ImageIcon(main.getClass().
                getResource("resources/save.png"));
        save.setIcon(saveIcon);
        save.setBackground(Color.gray);
        save.setBorder(BorderFactory.createEmptyBorder());
        save.setToolTipText("Save file");

        save.addActionListener((e) -> saveFile());
        return save;
    }

    private JButton copyButton(){
        JButton copy = new JButton(new DefaultEditorKit.CopyAction());
        copy.setText("");
        ImageIcon copyIcon = new ImageIcon(main.getClass().
                getResource("resources/copy.png"));
        copy.setIcon(copyIcon);
        copy.setBackground(Color.gray);
        copy.setBorder(BorderFactory.createEmptyBorder());
        copy.setToolTipText("Copy content");

        return copy;
    }
    private JButton pasteButton(){
        JButton paste = new JButton(new DefaultEditorKit.PasteAction());
        paste.setText("");
        ImageIcon openIcon = new ImageIcon(main.getClass().
                getResource("resources/paste.png"));
        paste.setIcon(openIcon);
        paste.setBackground(Color.gray);
        paste.setBorder(BorderFactory.createEmptyBorder());
        paste.setToolTipText("Paste content");

        return paste;
    }
    private JButton cutButton(){
        JButton cut = new JButton(new DefaultEditorKit.CutAction());
        cut.setText("");
        ImageIcon openIcon = new ImageIcon(main.getClass().
                getResource("resources/cut.png"));
        cut.setIcon(openIcon);
        cut.setBackground(Color.gray);
        cut.setBorder(BorderFactory.createEmptyBorder());
        cut.setToolTipText("Cut selected text");

        return cut;
    }
    private JButton undoButton(){
        JButton undo = new JButton(undoAction);
        undo.setText("");
        ImageIcon openIcon = new ImageIcon(main.getClass().
                getResource("resources/undo.png"));
        undo.setIcon(openIcon);
        undo.setBackground(Color.gray);
        undo.setBorder(BorderFactory.createEmptyBorder());
        undo.setToolTipText("Undo Changes");

        return undo;
    }
    private JButton redoButton(){
        JButton redo = new JButton(redoAction);
        redo.setText("");
        ImageIcon openIcon = new ImageIcon(main.getClass().
                getResource("resources/redo.png"));
        redo.setIcon(openIcon);
        redo.setBackground(Color.gray);
        redo.setBorder(BorderFactory.createEmptyBorder());
        redo.setToolTipText("Redo Changes");
        return redo;
    }
    private JButton exitButton(){
        JButton exit = new JButton();
        ImageIcon exitIcon = new ImageIcon(main.getClass().
                getResource("resources/exit.png"));
        exit.setIcon(exitIcon);
        exit.setBackground(Color.gray);
        exit.setBorder(BorderFactory.createEmptyBorder());
        exit.setToolTipText("Terminate Program");
        exit.addActionListener((e) -> System.exit(0));
        return exit;
    }
    //**************************************************************
    // Listens for any change that may take place in the tabbed pane
    //**************************************************************
    private class MyChangeListener implements ChangeListener {

        public void stateChanged(ChangeEvent e){

            for(int i = 0; i < tabbedPane.getTabCount(); i++)
                displaySelectedTab(i);
        }

        public void displaySelectedTab(int index){

            if(index != -1){
                initTabComponent(index);
            }

        }

        private void initTabComponent(int i){
            tabbedPane.setTabComponentAt(i,
                    new TabbedPane(tabbedPane));
        }

    }
    //**************************
    // Open a file method
    //*************************
    private void openFile(){
        //create empty string object
        String data;
        int approval = fileChooser.showOpenDialog(null);
        if (approval == JFileChooser.APPROVE_OPTION){
            data = fileChooser.getSelectedFile().getPath();

            tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(),
                    fileChooser.getSelectedFile().getName());
            try{
                FileInputStream fileInputStream = new FileInputStream(data);
                InputStreamReader isr = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                StringBuffer buffer = new StringBuffer();

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }

                reader.close();

                textPane.setText(buffer.toString());
                System.out.println(tabbedPane.getName());

            }catch (IOException e){

            }
        }

    }

    //**********************************************
    //   Saving file Function
    //**********************************************
    private void saveFile(){
        int approval = fileChooser.showSaveDialog(null);

        if (approval == JFileChooser.APPROVE_OPTION) {
            File file = new File(
                    fileChooser.getSelectedFile().getPath()
            );
            try (FileWriter writer = new FileWriter(file);
                 BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

                String data = textPane.getText();
                bufferedWriter.write(data);
                tabbedPane.setTitleAt(
                        tabbedPane.getSelectedIndex(),
                        file.getName()
                );
                //flushes the buffers to force it to write the
                //contents to the disc
                bufferedWriter.flush();
            } catch (IOException e) {

            }
        }
    }

    //*************************************
    // Our own containerListener class
    //*************************************
    private class MyContainerListener implements ContainerListener{
        @Override
        public void componentAdded(ContainerEvent e){
            container(e);
            System.out.println("Component added"+e.getChild()+"\n "+" Container"+
                    e.getContainer());
        }
        @Override
        public void componentRemoved(ContainerEvent e){
            System.out.println("component removed");
        }
        private void container(ContainerEvent e){
            tabbedPane.setSelectedComponent(e.getChild());

        }
    }
    //****************************************************
    //  create our own implementation of action listener
    //****************************************************
    private class MyActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e){
            tabbedPane.addTab("Untitled ",createScrollPane());

            System.out.println("New tab added at Index: "+tabbedPane.getSelectedIndex());
        }
    }
    //create JScrollPane
    //all the commented codes in this function are for displaying
    //background image to the text pane,for your own curiosity you can
    //remove the comments and compile the program to see the effects.

    private JScrollPane createScrollPane(){


        textPane = new JTextPane(){
//
//            ImageIcon imageIcon = new ImageIcon(
//              main.getClass().getResource("resources/openU.png")
//            );
//            Image image = imageIcon.getImage().
//                    getScaledInstance( (int) d.getWidth(),
//                            (int) d.getHeight() -100,10);
//            Image grayImage = GrayFilter.createDisabledImage(image);
//
//            {setOpaque(false);}
//
//            public void paintComponent (Graphics g) {
//
//
//                g.drawImage(grayImage,
//                        getWidth()/4,
//                        0,
//                        500 ,
//                        500 ,
//                        this);
//                super.paintComponent(g);
//
//            }

        };
        scrollPane = new JScrollPane(
                textPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.setPreferredSize(new Dimension(500,500));

        stylingTextPane();

        actions = createActionTable(textPane);
        return scrollPane;
    }
    private void stylingTextPane(){

        textPane.setCaretPosition(0);
        textPane.setBackground(Color.darkGray);
        textPane.setForeground(Color.WHITE);
        textPane.setFont(new Font("Monospaced",Font.PLAIN,25));
        textPane.setMargin(new Insets(5,5,5,5));
        styledDocument = textPane.getStyledDocument();

        if(styledDocument instanceof AbstractDocument){
            document = (AbstractDocument)styledDocument;
            document.setDocumentFilter(document.getDocumentFilter());
        }

        document.addUndoableEditListener(new MyUndoableEditListener());
        textPane.addCaretListener(new MyCaretListener());
        document.addDocumentListener(new MyDocumentListener());
        textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (SwingUtilities.isRightMouseButton(e))
                    //invoke createpopupMenu method
                    createPopupMenu().show(e.getComponent(),
                            e.getX(),
                            e.getY());
            }
        });
        addBindings();
    }
    //createPoppMethod
    private JPopupMenu createPopupMenu(){
        JPopupMenu popupMenu = new JPopupMenu();
        ImageIcon copyIcon = new ImageIcon(
                main.getClass().getResource("resources/copy.png")
        );
        JMenuItem copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setBackground(Color.gray);
        copy.setForeground(Color.white);
        copy.setText("Copy");
        copy.setIcon(copyIcon);

        ImageIcon cutIcon = new ImageIcon(
                main.getClass().getResource("resources/cut.png")
        );
        JMenuItem cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setBackground(Color.gray);
        cut.setForeground(Color.white);
        cut.setText("Cut");
        cut.setIcon(cutIcon);

        ImageIcon pasteIcon = new ImageIcon(
                main.getClass().getResource("resources/paste.png")
        );
        JMenuItem  paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setBackground(Color.gray);
        paste.setForeground(Color.white);
        paste.setText("Paste");
        paste.setIcon(pasteIcon);

        JMenuItem undo = new JMenuItem(undoAction);
        undo.setText(" Undo ");
        ImageIcon undoIcon = new ImageIcon(main.getClass().
                getResource("resources/undo.png"));
        undo.setIcon(undoIcon);
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,ActionEvent.CTRL_MASK));
        undo.setBackground(Color.darkGray);
        undo.setForeground(Color.white);
        undo.setFont(new Font("Monospaced",Font.PLAIN,16));
        undo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        undo.addActionListener(undoAction);

        JMenuItem redo = new JMenuItem(redoAction);
        redo.setText(" Redo");
        ImageIcon redoIcon = new ImageIcon(main.getClass().
                getResource("resources/redo.png"));
        redo.setIcon(redoIcon);
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,ActionEvent.CTRL_MASK));
        redo.setBackground(Color.darkGray);
        redo.setForeground(Color.white);
        redo.setFont(new Font("Monospaced",Font.PLAIN,16));
        redo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        redo.addActionListener(redoAction);
        JMenuItem save = new JMenuItem(" Save ");
        ImageIcon saveIcon = new ImageIcon(main.getClass().
                getResource("resources/save.png"));
        save.setIcon(saveIcon);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
        save.setBackground(Color.darkGray);
        save.setForeground(Color.white);
        save.setFont(new Font("Monospaced",Font.PLAIN,16));
        save.addActionListener((e) -> saveFile());
        save.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        JMenuItem open = new JMenuItem(" Open  ");
        ImageIcon openIcon = new ImageIcon(main.getClass().
                getResource("resources/open.png"));
        open.setIcon(openIcon);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        open.setBackground(Color.darkGray);
        open.setForeground(Color.white);
        open.setFont(new Font("Monospaced",Font.PLAIN,16));
        open.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        open.addActionListener(new MyOpenFileListener());

        popupMenu.add(open);
        popupMenu.add(save);
        popupMenu.add(copy);
        popupMenu.add(paste);
        popupMenu.add(cut);
        popupMenu.add(undo);
        popupMenu.add(redo);

        return popupMenu;
    }
    private class MyUndoableEditListener implements UndoableEditListener{

        public void undoableEditHappened(UndoableEditEvent e){
            undoManager.addEdit(e.getEdit());
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        }
    }
    //Add a couple of emacs key bindings for navigation.
    protected void addBindings() {
        InputMap inputMap = textPane.getInputMap();

        //Ctrl-b to go backward one character
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.backwardAction);

        //Ctrl-f to go forward one character
        key = KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.forwardAction);

        //Ctrl-p to go up one line
        key = KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.upAction);

        //Ctrl-n to go down one line
        key = KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK);
        inputMap.put(key, DefaultEditorKit.downAction);
    }
    //The following two methods allow us to find an
    //action provided by the editor kit by its name.
    private HashMap<Object, Action> createActionTable(JTextComponent textComponent) {
        HashMap<Object, Action> actions = new HashMap<>();
        Action[] actionsArray = textComponent.getActions();
        for (int i = 0; i < actionsArray.length; i++) {
            Action a = actionsArray[i];
            actions.put(a.getValue(Action.NAME), a);
        }
        return actions;
    }
    //
//    private Action getActionByName(String name) {
//        return actions.get(name);
//    }
    class UndoAction extends AbstractAction {
        public UndoAction() {
            super();
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.undo();
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            updateUndoState();
            redoAction.updateRedoState();
        }

        protected void updateUndoState() {
            if (undoManager.canUndo()) {
                setEnabled(true);
                //putValue(Action.NAME, undoManager.getUndoPresentationName());
            } else {
                setEnabled(false);
                // putValue(Action.NAME, "Undo");
            }
        }
    }

    class RedoAction extends AbstractAction {
        public RedoAction() {
            super();
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.redo();
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            updateRedoState();
            undoAction.updateUndoState();
        }

        protected void updateRedoState() {
            if (undoManager.canRedo()) {
                setEnabled(true);
            } else {
                setEnabled(false);

            }
        }
    }


    private class MyDocumentListener implements DocumentListener{

        public void insertUpdate(DocumentEvent e){

            displayInformation(e);
        }
        public void changedUpdate(DocumentEvent e){
            displayInformation(e);
        }
        public void removeUpdate(DocumentEvent e){
            displayInformation(e);
        }

        private void displayInformation(DocumentEvent e){

            //create document
            Document doc = e.getDocument();
            int length = doc.getLength();

            System.out.println("Document tyle"+ e.getType().toString()+" "
                    +" Document length "+length
            );
        }
    }

    private class MyCaretListener implements CaretListener {

        @Override
        public void caretUpdate(CaretEvent e){
            displayInfo(e.getDot(),e.getMark());
        }

        /**
         * This method must be invoked to the event dispatch
         * thread this is because it invokes modelToView method
         * @param dot
         * @param mark
         */

        private void displayInfo(final int dot,final int mark){
            Runnable runner = () ->{

                if(dot == mark){
                    try{
                        Rectangle caretPosition = textPane.modelToView(dot);
                        System.out.println("No Selection at all"
                                +dot+" "+caretPosition.getX()+" "+caretPosition.getY()
                        );
                    }catch(BadLocationException e){

                    }
                }
                else if (dot < mark){
                    System.out.println("Selection from: "+dot+" "+mark);
                }
                else if(dot > mark){
                    System.out.println("Selection from: "+mark+" "+dot);
                }

            };
            EventQueue.invokeLater(runner);
        }
    }

    public static void main(String[] args){
        //****************************************
        // run program in event dispatching thread
        //****************************************
        JFrame.setDefaultLookAndFeelDecorated(true);
        EventQueue.invokeLater(() -> new Main().showGui() );
    }

}
