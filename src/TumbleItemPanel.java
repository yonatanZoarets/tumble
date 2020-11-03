import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;


public class TumbleItemPanel extends JPanel {

    int loopslot = -1;           //the current frame number
    int pause = 3900;            //the length of pause between revs
    int offset = -57;            //how much to offset between loops
    int off;                     //the current offset
    int speed = 100;             //animation speed
    int nimgs  = 16;             //number of images to animate
    int width =  600;            //width of applet's content pane
    boolean finishedLoading;     // switch

    ImageIcon[] imgs = new ImageIcon[nimgs];   //the images
    int maxWidth;                              //widest image width
    JLabel statusLabel;
    InputWorker worker = new InputWorker();

    TumbleItemPanel() {
        if (offset < 0)  off = width - maxWidth;
        setOpaque(true);
        setBackground(Color.white);
        BorderLayout layout = new BorderLayout ();
        statusLabel = new JLabel("Loading Images...", 											JLabel.CENTER);
        add(statusLabel, BorderLayout.CENTER);
        AListener listener = new AListener();
        Timer timer = new Timer(speed, listener);
        timer.addActionListener (listener);
        timer.setInitialDelay(pause);
        timer.start();
        if (loopslot == nimgs - 1) timer.restart();

        //Start loading the images using the background worker.
        worker.execute();
    }			   // end of constructor TumbleItemPanel()


    private class AListener implements ActionListener  {
        public void actionPerformed (ActionEvent event) {
            //If still loading, can't animate.
            if (!worker.isDone()) return;
            loopslot++;
            if (loopslot >= nimgs) {
                loopslot = 0;
                off += offset;
                if (off < 0) off = width - maxWidth;
                else if (off + maxWidth > width) off = 0;
            }
            repaint();
        }		 //END actionPerformed()
    }
    class InputWorker extends SwingWorker<ImageIcon[], Void> {
        InputWorker() { }

        @Override
        public ImageIcon[] doInBackground() {
            final ImageIcon[] innerImgs = new ImageIcon[nimgs];
            for (int i = 0; i < nimgs; i++) {
                String path = "images/T" + (i+1) + ".gif";
                innerImgs[i] = new ImageIcon(path);
            }
            finishedLoading = true;
            return innerImgs;
        }				// END doInBackground()

        @Override
        public void done() {
            removeAll();
            loopslot = -1;
            try { imgs = get();
            }
            catch (Exception e) {};

        }	// END done()
    }	// end SwingWorker
}		// end of class TumbleItemPanel

// END class AListener

