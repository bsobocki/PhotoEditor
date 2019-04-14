import editor.MainWindow;
import editor.SaveWindowPanel;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        MainWindow window = new MainWindow();
        window.setLambdaToClose( () -> {
            if(!window.getPanel().isSaved()) {
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension windowDimension = new Dimension(200,100);
                Point windowLocation = new Point(screen.width/2 - windowDimension.width/2, screen.height / 2 - windowDimension.height/2);

                MainWindow mw = new MainWindow(windowLocation, windowDimension, new SaveWindowPanel());
                mw.getWindowToolBar().setTitleLabel("Save");
                mw.setLambdaToClose(mw::dispose);
                mw.getWindowToolBar().updateExitButtonAction();
                mw.getPanel().setButtonNOOperation(e->{ window.dispose();mw.dispose();});
                mw.getPanel().setButtonYESOperation(e->{ window.dispose();mw.dispose();});
            }
        });
    }
}
