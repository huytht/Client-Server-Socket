package com.client;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Client extends Thread {
	class Control {
		public AtomicInteger height = new AtomicInteger(0);
		public AtomicInteger width = new AtomicInteger(0);
		public volatile JFrame frame;
		public volatile JPanel panel;
	}

	final Control c = new Control();
	static ClientForm c1 = new ClientForm();
	private static long nextTime = 0;
	private static Client clientApp = null;
	private static String serverName = c1.txtIP.getText(); // loop back ip
	private static int portNo = 8087;
//	public Socket serverSocket = null;
	static String ip = "";

	// Receive Screen Thread
	class T1 implements Runnable {

		@Override
		public void run() {

			while (true) {
				try {

					// Getting screen size for frame

					Toolkit toolkit = Toolkit.getDefaultToolkit();
					Dimension dimensions = toolkit.getScreenSize();

					// Host IP address
					String fileName = c1.serverSocket.getInetAddress().getHostName().replace(".", "-");
//					System.out.println(fileName);

					// Get image from Socket
//					System.out.println(c1.serverSocket.getInputStream().available());
					if (c1.serverSocket.getInputStream().available() > 0) {
						BufferedImage img = ImageIO.read(ImageIO.createImageInputStream(c1.serverSocket.getInputStream()));
						
						if (img != null) {
							// Height and width of image
							c.height.set(img.getHeight());
							c.width.set(img.getWidth());

							// Panel dimensions
							c.panel.setSize(dimensions.width, dimensions.height);

							JLabel lab;

							// Setting image into label
							lab = new JLabel(new ImageIcon((new ImageIcon(img).getImage().getScaledInstance(c.panel.getWidth(),
									c.panel.getHeight(), java.awt.Image.SCALE_SMOOTH))));

							// Add label to panel
							c.panel.add(lab);
							c.frame.repaint();
							c.frame.pack();

							// Sleep for delay
							sleep(10000);

							// Removing label for next image
							c.panel.remove(lab);

							System.out.println("Image here");
						}
					}
				} catch (IOException ex) {
					Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	// Send Events Thread
//    class T2 implements Runnable {
//
//        @Override
//        public void run() {
//
//            while (true) {
//                try {
//
//                    //New Socket for send events
//                    
//                    Socket eve = new Socket(serverName, 8888);   
//
//                     //Instance of SendEvents class
//                    new SendEvents(c.panel, eve);               
//
//                } catch (IOException ex) {
//                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//
//        }
//    }

	// Client Constructor with IP of Server as parameter
	Client(String ip) {

		// Frame and Panel Initialization
		c.frame = new JFrame();
		c.panel = new JPanel();

		// Set frame and Panel size
		c.frame.setSize(1930, 1050);
		c.panel.setSize(1930, 1050);
		c.frame.setExtendedState(Frame.MAXIMIZED_BOTH);

		c.frame.add(c.panel);
		c.frame.pack();
		c.frame.setVisible(true);

		// Setting IP address of Server
		this.ip = ip;
		serverName = ip;

		// Creating three Threads
		T1 t1 = new T1();
//        T2 t2 = new T2();

		// Starting Threads
		new Thread(t1).start();
//        new Thread(t2).start();
	}
}
