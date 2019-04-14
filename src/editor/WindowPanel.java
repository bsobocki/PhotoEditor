package editor;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class WindowPanel extends JPanel {
    abstract public void update(int y, int width, int height);
    abstract public boolean isSaved();
    public void setButtonYESOperation(ActionListener l){}
    public void setButtonNOOperation (ActionListener l){}
}
