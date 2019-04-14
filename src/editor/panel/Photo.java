package editor.panel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Photo extends JPanel {

    private int minY; //the photo must be under ToolBar
    private BufferedImage photo;
    private BufferedImage undo;
    public enum COLORTYPE {RED, GREEN, BLUE}
    private int red;
    private int green;
    private int blue;

    // CONSTRUCTORS

    Photo(int minY){
        super();
        this.minY = minY;
        setBounds(0,0,0,0);
        photo = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
    }

    // GETTERS

    public BufferedImage getPhoto() { return photo; }
    public BufferedImage getUndo() { return undo; }

    // SETTERS

    public void setPhoto(BufferedImage photo) { this.photo = photo; }
    public void setPhotoToUndo() { this.photo = this.undo; }

    // LOAD

    public void loadPhoto(String path) {
        try{ photo = ImageIO.read(new File(path));
            undo = photo; }
        catch(IOException e){e.printStackTrace();}
    }

    // UPDATE

    public void update(int panelWidth, int panelHeight){
        int borderY = photo.getHeight()<panelHeight ? panelHeight/2- photo.getHeight()/2 : 0;
        setBounds(panelWidth/2-photo.getWidth()/2,borderY + minY, photo.getWidth(), photo.getHeight());
    }

    // EDIT PHOTO

    void grayScale(int power){
        template((c,x,y)->{
            red = (int)(c.getRed()*0.299);
            green = (int)(c.getGreen()*0.587);
            blue = (int)(c.getBlue()*0.114);
            int sum = red+green+blue;
            for(int i=2; i<=power; i++){
                red = (int)(sum*0.299);
                green = (int)(sum*0.587);
                blue = (int)(sum*0.114);
                sum = red+green+blue;
            }
            photo.setRGB(x,y,new Color(sum,sum,sum).getRGB());
        });
    }
    void sepia() {
        grayScale(3);
        addToAll(-5,-5,-5);
        addToAll(27,2,2);
    }
    void bright(){ addToAll(10,10,10); }
    void dark(){ addToAll(-10,-10,-10); }
    void saveColor(COLORTYPE color){
        template((c,x,y)->{
            red = c.getRed();
            green = c.getGreen();
            blue = c.getBlue();
            if(true) {
                red *= 1.1*red>255? 255: 1.1*red;
                green *= 1.1;
                blue *= 1.1;
            }
            photo.setRGB(x,y,new Color(red,green,blue).getRGB());
        });
    }

    //add value to color in one pixel
    private void addToAll(int value1, int value2, int value3){
        template((c,x,y)-> {
            //red
            if (value1 >= 0)
                red = c.getRed() + value1 > 255 ? 255 : c.getRed() + value1;
            else
                red = c.getRed() + value1 < 0 ? 0 : c.getRed() + value1;
            //green
            if(value2>=0)
                green = c.getGreen()+value2>255 ? 255 : c.getGreen()+value2;
            else
                green = c.getGreen()+value2<0 ? 0 : c.getGreen()+value2;
            //blue
            if(value3>=0)
                blue = c.getBlue()+value3>255 ? 255 : c.getBlue()+value3;
            else
                blue = c.getBlue()+value3<0 ? 0 : c.getBlue()+value3;
            //set color
            photo.setRGB(x,y,new Color(red,green,blue).getRGB());
        });
    }
    void addValueToColor(COLORTYPE color, int value){
        switch(color){
            case RED:
                addToAll(value,0,0);
                break;
            case GREEN:
                addToAll(0,value,0);
                break;
            case BLUE:
                addToAll(0,0,value);
        }
    }

    //template for methods
    //iterations for every pixel of photo
    private void template(Function fun){
        for(int x=0; x<photo.getWidth(); x++)
            for(int y=0; y<photo.getHeight(); y++){
                fun.iteration(new Color(photo.getRGB(x,y)),x,y);
            }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(photo,0,0,this);
    }

    //for lambda abstraction
    private interface Function {
        void iteration(Color c, int x, int y);
    }
}
