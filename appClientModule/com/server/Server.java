
package com.server;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.net.*;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.client.ClientForm;

public class Server extends Thread {

	// Initializing Server and client sockets
	public static ServerSocket serverSocket = null;
	public static Socket server = null;
	public static ServerSocket eveSocket = null;
	private static Socket eve = null;
	static ObjectInputStream ois = null;
//	static ServerForm s = new ServerForm();

	// Robot and Threads Initialization
	public static Robot robot = null;
	static Thread Server_Thread_1 = null;
	static Thread Server_Thread_2 = null;

	// Constructor to assign threads
	public Server(Thread ServerThread1, Thread ServerThread2)
			throws IOException, SQLException, ClassNotFoundException, Exception {

		// Assigning Thread values
		this.Server_Thread_1 = ServerThread1;
		this.Server_Thread_2 = ServerThread2;

		// Making start screen visible
		ServerForm s = new ServerForm();
		s.setVisible(true);

		// Creating Sockets on different ports
		serverSocket = new ServerSocket(8087);
		server = serverSocket.accept();
		eveSocket = new ServerSocket(8888);
		serverSocket.setSoTimeout(1000000);

	}

	public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, Exception {

		// Thread for Sending Screenshots
		Server_Thread_1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// New Robot for the current screen
					robot = new Robot();
				} catch (AWTException ex) {
					Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
				}

				while (true) {
					try {
						// Accepting Client Requests and creating new socket for each client
//						if (ServerForm.acceptFlag) {
						ois = new ObjectInputStream(server.getInputStream());
						String message = (String) ois.readObject();
						System.out.println("Message Received: " + message);
						if (ServerForm.acceptFlag) {
							switch (message) {
							case "TAKEPIC":
								sendScreen(robot);
								break;
							case "SHUTDOWN":
								shutDown();
								break;
							case "QUIT":
								System.exit(0);
								ServerForm.acceptFlag = false;
							}
						}
//						}
					} catch (SocketTimeoutException st) {
						System.out.println("Socket timed out!");
						break;
					} catch (IOException e) {
						e.printStackTrace();
						break;
					} catch (Exception ex) {
						System.out.println(ex);
					}
				}
			}
		});

		// Thread to Receive events from clients
		Thread Server_Thread_2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					// New Robot for the current screen

					robot = new Robot();
				} catch (AWTException ex) {
					Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
				}

//                while (true) {
//                    try {
//                        //Accepting Client Requests and creating new socket for each client
//                        eve = eveSocket.accept();
//                       
//                        //Recieve Events from Clients on current screen
//                        new ReceiveEvents(eve, robot);
//                    } catch (SocketTimeoutException st) {
//                        System.out.println("Socket timed out!");
//                        break;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        break;
//                    } catch (Exception ex) {
//                        System.out.println(ex);
//                    }
//                }
			}
		});

		// Creating instance of client with three Threads
		Server serverApp = new Server(Server_Thread_1, Server_Thread_2);
		Thread thread = new Thread(serverApp);
		thread.start();

		// Starting Each thread
		Server_Thread_1.start();
//		Server_Thread_2.start();
	}

	// Method to send screenshots to clients
	private static void sendScreen(Robot robot) throws AWTException, IOException {

		// Get current screen dimensions
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimensions = toolkit.getScreenSize();

		// Take screenshot using robot class
		BufferedImage screenshot = robot.createScreenCapture(new Rectangle(dimensions));

		// Send image to client through output stream of socket
		ImageIO.write(screenshot, "png", server.getOutputStream());
	}

	private static void shutDown() {
		Runtime r = Runtime.getRuntime();
		try {
			System.out.println("Shutting down the PC after " + 1 + " seconds.");
			r.exec("shutdown -s -t " + 1);
		} catch (IOException e) {
			System.out.println("Exception: " + e);
		}
	}

}
