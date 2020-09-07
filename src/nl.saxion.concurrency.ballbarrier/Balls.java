package nl.saxion.concurrency.ballbarrier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Balls {

    public static void nap(int ms) {
		try {
			Thread.sleep(ms);
		}
		catch(InterruptedException e) {
			//
			//  Print out the name of the tread that caused this.
			//
			System.err.println("Thread "+Thread.currentThread().getName()+
					   " throwed exception "+e.getMessage());
		}
    }

    public static void main(String[] a) {
	
        final BallWorld world = new BallWorld();
		final JFrame win = new JFrame();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			win.getContentPane().add(world);
			win.pack();
			win.setVisible(true);
			}});

		//prevent resizing of the component
		win.addComponentListener(new ComponentListener() {
			private int width = 0,height = 0;
			@Override
			public void componentResized(ComponentEvent e) {
				if (width == 0) return;
				if (e.getSource() instanceof JFrame) {
					JFrame frame = (JFrame)(e.getSource());
					frame.setSize(new Dimension(width,height));
				}
			}

			@Override
			public void componentMoved(ComponentEvent e) {

			}

			@Override
			public void componentShown(ComponentEvent e) {
				if (e.getSource() instanceof JFrame) {
					JFrame frame = (JFrame)(e.getSource());
					width = frame.getWidth();
					height = frame.getHeight();
				}
			}

			@Override
			public void componentHidden(ComponentEvent e) {

			}
		});


		Thread.currentThread().setName("MyMainThread");

		nap((int)(1*Math.random()));
		new Ball(world, 50, 80, 5, 10, Color.red, "red1").start();
		nap((int)(1000*Math.random()));
		new Ball(world, 70, 100, 8, 6, Color.blue, "blue2").start();
		nap((int)(1000*Math.random()));
		new Ball(world, 150, 100, 9, 7, Color.green, "green3").start();
		nap((int)(1000*Math.random()));
		new Ball(world, 200, 130, 3, 8, Color.black, "black3").start();
		nap((int)(1000*Math.random()));


    }
}
