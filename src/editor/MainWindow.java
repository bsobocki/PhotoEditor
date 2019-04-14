package editor;

import editor.panel.Panel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class MainWindow extends JFrame {
    private final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    private Point position = new Point(300, 50);
    private Dimension dimension = new Dimension(700, 700);

    private WindowPanel panel;
    private WindowToolBar windowToolBar;

    private boolean isFullSized;
    private boolean mainWindow;

    private LambdaToClose lambdaToClose;

    // CONSTRUCTOR

    public MainWindow(){
        super();
        init();
        createComponents();
        addComponents();
        repaint();
    }
    public MainWindow(Point p, Dimension d, WindowPanel panel){
        super();
        //lambda expression
        position.setLocation(p);
        dimension.setSize(d);
        //initialize
        init();
        //create components
        this.panel = panel;
        windowToolBar = new WindowToolBar();
        windowToolBar.update(this.getWidth(),windowToolBar.getHeight());
        //add components
        addComponents();
        //set predicates
        isFullSized = false;
        mainWindow = false;
        //visible
        setVisible(true);

        updateWindow();

        repaint();
    }

    // INITIALIZE

    private void init(){
        mainWindow = true;
        isFullSized = false;
        setLayout(null);
        //without window's form
        setUndecorated(true);
        //pack frame
        pack();
        //location
        setBounds(position.x, position.y, dimension.width,dimension.height);
        //visible
        setVisible(true);
    }
    private void createComponents(){
        windowToolBar = new WindowToolBar();
        panel = new Panel();
        repaint();
        updateWindow();
    }
    private void addComponents(){
        add(windowToolBar);
        add(panel);
    }

    // UPDATE

    private void updateBoundsToDefault() {
        setBounds(position.x, position.y, dimension.width,dimension.height);
        updateWindow();
    }
    private void updateBounds(int x, int y, int width, int height){
        setDefaultPosition(x, y);
        setDefaultDimension(width, height);
        setBounds(x, y, width, height);
        updateWindow();
    }
    public void updateWindow(){
        windowToolBar.update(this.getWidth(),windowToolBar.getHeight());
        panel.update(windowToolBar.getHeight(),this.getWidth(),this.getHeight()-windowToolBar.getHeight());
    }

    // METHODS

    private void fullSize(){
        setBoolFullSized(true);
        setBounds(0,0,screen.width,screen.height);
        updateWindow();
    }
    private void defaultSize(){
        updateBoundsToDefault();
        setBoolFullSized(false);
    }

    // GETTERS

    private boolean isFullSized(){ return isFullSized; }
    private Point getDefaultPosition() { return position; }
    private Dimension getDefaultDimension() { return dimension; }
    public WindowToolBar getWindowToolBar() { return windowToolBar; }
    public WindowPanel getPanel() { return panel; }

    // SETTERS

    public void setLambdaToClose(LambdaToClose l) { this.lambdaToClose = l; }
    private void setBoolFullSized(boolean ifs) { this.isFullSized = ifs;}
    private void setDefaultPosition(int x, int y) { position.setLocation(x, y); }
    private void setDefaultDimension(int width, int height) { dimension.setSize(width, height); }

    // WINDOW TOOLBAR'S CLASS

    public class WindowToolBar extends JPanel{
        private final int HEIGHT = 20;

        private JButton exitButton;
        private JButton maxButton;
        private JButton minButton;
        private JLabel title;
        private FlowLayout fl;

        //constructor

        WindowToolBar(){
            fl = new FlowLayout();

            title = new JLabel("Photo Editor");
            title.setForeground(Color.white);
            title.setBackground(Color.black);

            setExitButton(createWindowButton( "exit"));
            setMaxButton(createWindowButton("maximize"));
            setMinButton(createWindowButton("minimize"));
            setButtonsPositions();

            add();
            set();

            WListener l = new WListener();
            addMouseListener(l);
            addMouseMotionListener(l);
        }

        //initialize

        private void set(){
            setLayout(fl);
            setBounds(0,0,MainWindow.this.getWidth(),HEIGHT);
            setBackground(Color.black);
        }
        private void setButtonsPositions(){
            final int Y = 5;
            final int WIDTH = 20;
            final int HEIGHT = 10;

            title.setBounds(getWidth()/2-title.getWidth()/2, getHeight()/2-title.getHeight()/2, title.getWidth(), title.getHeight());

            exitButton.setBounds(getWidth()-25, Y, WIDTH, HEIGHT);
            maxButton.setBounds(getWidth()-50, Y, WIDTH, HEIGHT);
            minButton.setBounds(getWidth()-75, Y, WIDTH, HEIGHT);
        }

        void add(){
            add(exitButton);
            add(maxButton);
            add(minButton);
            add(title);
        }

        void setExitButton(JButton exitButton) { this.exitButton = exitButton; }
        void setMaxButton(JButton maxButton) { this.maxButton = maxButton; }
        void setMinButton(JButton minButton) { this.minButton = minButton; }

        // update

        void update(int width, int height){
            setBounds(0,0, width, height);
            updateWindowToolBar();
        }
        void updateWindowToolBar(){
            setLayout(null);
            setButtonsPositions();
        }

        public void updateExitButtonAction() {exitButton.addActionListener( e -> MainWindow.this.lambdaToClose.close());}

        //create buttons

        private JButton createWindowButton(String whichOne){
            JButton b = new JButton();
            //without Borders
            b.setBorder(null);
            switch(whichOne){
                case "exit":
                    setButton(b, "exit.png", "exit2.png", e -> {
                        if(mainWindow)
                            lambdaToClose.close();
                    });
                    break;
                case "maximize":
                    setButton(b,"max.png", "max2.png", e -> {
                        if(!MainWindow.this.isFullSized()) {
                            MainWindow.this.fullSize();
                        }
                        else
                            MainWindow.this.defaultSize();
                    });
                    break;
                case "minimize":
                    setButton(b,"min.png","min2.png", e -> MainWindow.this.setState(Frame.ICONIFIED));
            }
            return b;
        }

        //set buttons

        private void setButton(JButton b, String path1, String path2, ActionListener l){
            //set icon
            try{ b.setIcon(new ImageIcon(ImageIO.read(new File(path1)))); }
            catch(IOException e){ System.err.println(e.getMessage()); }
            //set background
            b.setBackground(Color.black);
            //set listeners
            b.addActionListener(l);
            b.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) { }
                @Override
                public void mousePressed(MouseEvent e) { }
                @Override
                public void mouseReleased(MouseEvent e) { }
                @Override
                public void mouseEntered(MouseEvent e) {
                    try { b.setIcon(new ImageIcon(ImageIO.read(new File(path2)))); }
                    catch (IOException e1) { e1.printStackTrace(); }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    try { b.setIcon(new ImageIcon(ImageIO.read(new File(path1)))); }
                    catch (IOException e1) { e1.printStackTrace(); }
                }
            });
        }

        //set title label

        public void setTitleLabel(String s){ title.setText(s); }

        //listener class

        class WListener implements MouseMotionListener, MouseListener {

            private int distanceCursorFromCornerX = 0;
            private int distanceCursorFromCornerY = 0;


            @Override
            public void mouseDragged(MouseEvent e) {
                if(isFullSized()) {
                    //set dimension and position to default
                    MainWindow.this.defaultSize();
                    //if we dragged the window and after setting dimension to default
                    //cursor is outside window
                    if(e.getX()>MainWindow.this.getWidth()/2)
                        distanceCursorFromCornerX = MainWindow.this.getWidth()/2;
                }
                MainWindow mw = MainWindow.this;
                MainWindow.this.updateBounds(mw.getX() + e.getX() - distanceCursorFromCornerX, mw.getY() + e.getY() - distanceCursorFromCornerY, mw.getWidth(), mw.getHeight());
            }

            @Override
            public void mouseMoved(MouseEvent e) {}

            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mousePressed(MouseEvent e) {
                distanceCursorFromCornerX = e.getX();
                distanceCursorFromCornerY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getLocationOnScreen().getY()==0) {
                    MainWindow.this.fullSize();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        }

    }

    public interface LambdaToClose {
        void close();
    }
}
