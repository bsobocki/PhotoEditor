package editor.panel;

import editor.WindowPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

public class Panel extends WindowPanel {
    //toolbar
    private PanelToolBar toolBar;
    //photo
    private Photo mainPhoto;
    private Photo extraPhoto;
    //save
    private boolean isSaved;

    // CONSTRUCTOR

    public Panel(){
        init();
        set();
    }

    // INITIALIZE

    private void init(){
        this.isSaved = false;
        this.toolBar = new PanelToolBar();
        this.extraPhoto = new Photo(toolBar.getHeight());
        this.mainPhoto = new Photo(toolBar.getHeight());
    }
    private void set(){
        setLayout(null);
        //listener
        Listener l = new Listener();
        addMouseListener(l);
        addMouseMotionListener(l);
        //toolbar
        add(toolBar);
        //color
        setBackground(Color.GRAY);
    }

    // UPDATE

    @Override
    public void update(int y, int width, int height){
        //update only width
        toolBar.update(width, toolBar.getHeight());
        //height-toolbar.height because the photo is in the centre of the DOWN PART of the panel
        mainPhoto.update(width,height-toolBar.getHeight());
        setBounds(0, y, width,height);
    }

    // SETTERS

    public void setToolBar(PanelToolBar toolBar) { this.toolBar = toolBar; }
    public void setMainPhoto(Photo mainPhoto) { this.mainPhoto = mainPhoto; }
    public void setExtraPhoto(Photo extraPhoto) { this.extraPhoto = extraPhoto; }
    public void setSaved(boolean saved) { isSaved = saved; }

    // GETTERS

    public Photo getMainPhoto() { return mainPhoto; }
    public Photo getExtraPhoto() { return extraPhoto; }

    // PREDICATES

    @Override
    public boolean isSaved() { return isSaved; }

    // DRAW

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(mainPhoto.getPhoto(), mainPhoto.getX(), mainPhoto.getY(), mainPhoto.getWidth(), mainPhoto.getHeight(), this);
    }

    // TOOLBAR'S CLASS

    class PanelToolBar extends JPanel{
        private Dimension dimension = new Dimension(Panel.this.getWidth(), 70);

        //constructor

        PanelToolBar(){
            setBounds(0,0, dimension.width, dimension.height);
            setBackground(Color.white);
            setLayout(null);
            createComponents();
        }

        //update

        private void update(int width, int height){
            setBounds(0,0,width,height);
            repaint();
        }

        //set

        private void setDimension (Dimension d) { dimension = d; }

        //create

        private void createComponents(){
            this.add(createButton("open.png",3,3, e->{
                //create window to choose photo
                JFileChooser chooser = new JFileChooser();
                //create file filter
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images","jpg","png");
                //set file filter
                chooser.setFileFilter(filter);
                //create window
                int returnVal = chooser.showOpenDialog(null);
                //when user chosen a file
                if(returnVal == JFileChooser.APPROVE_OPTION){
                    mainPhoto.loadPhoto(chooser.getSelectedFile().getPath());
                    Panel.this.update(Panel.this.getY(),Panel.this.getWidth(),Panel.this.getHeight());
                    Panel.this.repaint();
                }
            }));
            this.add(createButton("undo.png", 43,3, e-> {
                //make photo black and white
                mainPhoto.setPhotoToUndo();
                Panel.this.repaint();
            }));
            this.add(createButton("gray.png", 83,3, e-> {
                //make photo black and white
                mainPhoto.grayScale(1);
                Panel.this.repaint();
            }));
            this.add(createButton("red.png",123,3,e->{
                mainPhoto.addValueToColor(Photo.COLORTYPE.RED,3);
                Panel.this.repaint();
            }));
            this.add(createButton("sepia.png", 163,3,e->{
                mainPhoto.sepia();
                Panel.this.repaint();
            }));
            this.add(createButton("bright.png",203, 3, e->{
                mainPhoto.bright();
                Panel.this.repaint();
            }));
            this.add(createButton("dark.png",243, 3, e->{
                mainPhoto.dark();
                Panel.this.repaint();
            }));
        }
        private JButton createButton(String path, int x, int y, ActionListener l){
            final int WIDTH = 30;
            final int HEIGHT = 20;
            JButton b = new JButton();
            if(path!=null)
                try { b.setIcon(new ImageIcon(ImageIO.read(new File(path)))); }
                catch(IOException e) { e.printStackTrace(); }
            //b.setBorder(null);
            b.setBorderPainted(true);
            b.setBounds(x,y,WIDTH,HEIGHT);
            b.addActionListener(l);
            return b;
        }
    }

    // LISTENER

    private class Listener implements MouseMotionListener, MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) { }

        @Override
        public void mousePressed(MouseEvent e) { }

        @Override
        public void mouseReleased(MouseEvent e) { }

        @Override
        public void mouseEntered(MouseEvent e) { }

        @Override
        public void mouseExited(MouseEvent e) { }

        @Override
        public void mouseDragged(MouseEvent e) { }

        @Override
        public void mouseMoved(MouseEvent e) { }
    }
}
