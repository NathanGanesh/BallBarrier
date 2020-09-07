package nl.saxion.concurrency.ballbarrier;

import java.awt.*;
import java.util.concurrent.Semaphore;

public class Ball extends Thread {

    BallWorld world;

    //made static
    private int xpos, ypos, xinc, yinc;

    private final Color col;
    public static Semaphore semaphore = new Semaphore(4);
    private Semaphore semaphore1 = new Semaphore(1);
    private final static int ballw = 10;
    private final static int ballh = 10;
    private double worldDiagional = 1;


    public Ball(BallWorld world, int xpos, int ypos,
                int xinc, int yinc, Color col, String name) {

        super("Ball :" + name);


        this.world = world;
        this.xpos = xpos;
        this.ypos = ypos;
        this.xinc = xinc;
        this.yinc = yinc;
        this.col = col;

        world.addBall(this);
    }

    public void run() {
        while (true) {
            int ypos2 = (world.getHeight() - (ypos + ballh));
            int xpos2 = world.getWidth() - (xpos + ballh);
            int diag = 10;
            if (ypos2 != 0 && xpos2 != 0) {
                diag = ypos2 / xpos2;
            }

            if (worldDiagional > diag) {
                Balls.nap(250);
                try {
                    acquireMainSemaphore();
                    semaphore1.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (semaphore.availablePermits() == 0) {
                        System.out.println("TIME TO RELEASE");
                        unRealese();
                    }
                    semaphore1.release();
                }
                Balls.nap(250);
            } else {
                move();
            }
        }
    }


    public void move() {
        if (xpos >= world.getWidth() - ballw || xpos <= 0) {
            xinc = -xinc;
        }

        if (ypos >= world.getHeight() - ballh || ypos <= 0) {
            yinc = -yinc;
        }



        doMove();
        Balls.nap(30);
        world.repaint();
    }

    private void acquireMainSemaphore() {
        Balls.nap(50);
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + " acquired a semaphore. The amount of semaphores left are: " + semaphore.availablePermits());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void unRealese(){
        semaphore.release(5);
        System.out.println(Thread.currentThread().getName() + " Time to release: " + semaphore.availablePermits());
    }

    //
    // SYNC: This modifies xpos and ypos.
    //
    public synchronized void doMove() {
        xpos += xinc;
        ypos += yinc;
    }


    //
    // SYNC: This is accessed from the GUI thread, and
    //       it reads xpos and ypos.
    //
    public synchronized void draw(Graphics g) {

        g.setColor(col);
        g.fillOval(xpos, ypos, ballw, ballh);
    }
}
