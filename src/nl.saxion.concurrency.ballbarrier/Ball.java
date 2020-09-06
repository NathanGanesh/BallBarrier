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
            move();
            if (semaphore.availablePermits() == 0){
                unfreeze();

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


        int ypos2 = (world.getHeight()-(ypos+ballh));
        int xpos2 = world.getWidth()-(xpos+ballh);
        int diag =10;
        if (ypos2!=0 && xpos2 !=0){ diag = ypos2/xpos2; }

        if (worldDiagional>diag){
            freeze();
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else{
            doMove();
            Balls.nap(30);
            world.repaint();
        };








    }

    //
    // SYNC: This modifies xpos and ypos.
    //
    public synchronized void doMove() {
        xpos += xinc;
        ypos += yinc;

    }

    void unfreeze(){
        semaphore.release(5);

    }

    void freeze() {
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + " acquired a semaphore. The amount of semaphores left are: " + semaphore.availablePermits());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        xinc = 0;
        yinc = 0;

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
