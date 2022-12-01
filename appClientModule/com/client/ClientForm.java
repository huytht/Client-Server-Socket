package com.client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientForm extends JFrame {

	private JPanel contentPane;
	public JTextField txtIP;
	static String ip = "";
	static InetAddress IP = null;
	public static Socket serverSocket = null;
	private static int portNo = 8087;
	static ObjectOutputStream oos = null;;

	/**
	 * Launch the application.
	 */
	public static void main(String args[]) throws UnknownHostException {

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					// Setting Host IP address in the frame
					new ClientForm().setVisible(true);

					// Getting IP address of the host
					IP = InetAddress.getLocalHost();
					System.out.println("My IP Address is:");
					System.out.println(IP.getHostAddress());
					serverSocket = new Socket(IP.getHostName(), portNo);

				} catch (UnknownHostException ex) {
					Logger.getLogger(ClientForm.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 473, 352);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtIP = new JTextField();
		txtIP.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		txtIP.setBounds(22, 10, 305, 35);
		contentPane.add(txtIP);
		txtIP.setColumns(10);

		JButton btnConnect = new JButton("Kết nối");
		btnConnect.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnConnect.setBounds(347, 10, 100, 35);
		btnConnect.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				connectActionPerformed(evt);
			}
		});
		contentPane.add(btnConnect);

		JButton btnProcess = new JButton("<html>Process <br/> Running</html>");
		btnProcess.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				writeMessage("PROCESS");
			}
		});
		btnProcess.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnProcess.setBounds(24, 63, 83, 242);
		contentPane.add(btnProcess);

		JButton btnApp = new JButton("App Running");
		btnApp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				writeMessage("APPLICATION");
			}
		});
		btnApp.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnApp.setBounds(117, 63, 225, 53);
		contentPane.add(btnApp);

		JButton btnTat = new JButton("<html>Tắt<br/> máy</html>");
		btnTat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				writeMessage("SHUTDOWN");
			}
		});
		btnTat.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnTat.setBounds(117, 126, 65, 91);
		contentPane.add(btnTat);

		JButton btnReg = new JButton("Sửa registry");
		btnReg.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				writeMessage("REGISTRY");
			}
		});
		btnReg.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnReg.setBounds(117, 233, 251, 72);
		contentPane.add(btnReg);

		JButton btnKeyLock = new JButton("Keystroke");
		btnKeyLock.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				writeMessage("KEYLOG");
			}
		});
		btnKeyLock.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnKeyLock.setBounds(347, 62, 100, 155);
		contentPane.add(btnKeyLock);

		JButton btnExit = new JButton("Thoát");
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				writeMessage("QUIT");
				System.exit(0);
			}
		});
		btnExit.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnExit.setBounds(378, 233, 71, 72);
		contentPane.add(btnExit);

		JButton btnPic = new JButton("Chụp màn hình");
		btnPic.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				writeMessage("TAKEPIC");
			}
		});
		btnPic.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnPic.setBounds(192, 126, 150, 91);
		contentPane.add(btnPic);
	}

	private void writeMessage(String message) {
		try {
			if (serverSocket == null) {
				JOptionPane.showMessageDialog(this, "Please enter the IP address to get connected");
			} else {
				oos = new ObjectOutputStream(serverSocket.getOutputStream());
				oos.writeObject(message);
				Thread.sleep(100);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void connectActionPerformed(java.awt.event.ActionEvent evt) {

		// Checking Null values of IP
		if (txtIP.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter the IP address to get connected");
		} else {
			Client c = new Client(txtIP.getText());
		}
	}
}
