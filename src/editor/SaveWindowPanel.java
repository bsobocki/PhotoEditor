package editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SaveWindowPanel extends WindowPanel {

    private JButton butYES;
    private JButton butNO;

    // CONSTRUCTOR

    public SaveWindowPanel(){
        setBackground(Color.lightGray);

        JLabel text = new JLabel("Save Photo?");
        butYES = new JButton("YES");
        butNO = new JButton("NO");

        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.setBackground(Color.lightGray);
        p.add(butYES);
        p.add(butNO);

        add(text);
        add(p);

        setLayout(new FlowLayout());
    }

    @Override
    public void setButtonYESOperation(ActionListener l){ butYES.addActionListener(l); }
    @Override
    public void setButtonNOOperation (ActionListener l){ butNO.addActionListener(l); }
    @Override
    public void update(int y,int width, int height) {
        setBounds(0,y,width, height);
    }
    @Override
    public boolean isSaved(){ return true; }
}
