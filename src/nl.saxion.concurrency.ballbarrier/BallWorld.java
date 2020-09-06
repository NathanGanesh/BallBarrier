package nl.saxion.concurrency.ballbarrier;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BallWorld extends JPanel  {

    private final int xSize = 250;
    private final int ySize = 250;

    private final static Color BGCOLOR = Color.white;

    private ArrayList<Ball> balls = new ArrayList<Ball>();

    public BallWorld() {
        setPreferredSize(new Dimension(xSize,ySize));
        setOpaque(true);
        setBackground(BGCOLOR);
    }

    //
    // In order to access the balls collection only
    // from one thread, let it be modified inside
    // the GUI thread.
    //
    public void addBall(final Ball b) {
        SwingUtilities.invokeLater(new Runnable () {
            public void run() {
                balls.add(b);
                repaint();
                }
            });
    }


    public void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2){

        //creates a copy of the Graphics instance
        Graphics2D g2d = (Graphics2D) g.create();

        //set the stroke of the copy, not the original
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.drawLine(x1, y1, x2, y2);

        //gets rid of the copy
        g2d.dispose();
    }

    //
    // This is run from the GUI thread and
    // reads the balls collection.
    //
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawDashedLine(g,0,0,this.getWidth(), this.getHeight());
        for(Ball b : balls)
            b.draw(g);
    }
}
