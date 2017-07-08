import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client
{
	//private static final long serialVersionUID = 1L;
	private static final int PORT = 9001;
	BufferedReader in;
	PrintWriter out;
	
	
	JFrame frame;
	JPanel panel;
	JTextField NewMsg;
	JTextArea ChatHistory;
	JButton Send;
	JLabel ChatHistoryLabel;
	JLabel showUsrName;
	JTextField uname;
	String name;
	
	// constructor . 
	Client()
	{
		// Design part of the Client			
		frame = new JFrame();
		panel = new JPanel();
		NewMsg = new JTextField();
		uname = new JTextField();
		ChatHistory = new JTextArea();
		Send = new JButton("Send");
		showUsrName = new JLabel("User Name: ");
		
		frame.setSize(500, 500);
		frame.setVisible(false);
		frame.setTitle("MChat");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setLayout(null);
		
		
		showUsrName.setBounds(20, 10, 100, 20);
		uname.setBounds(110, 15, 360, 15);
		ChatHistory.setBounds(20, 35, 450, 360);
		
		panel.add(ChatHistory);
		NewMsg.setBounds(20, 410, 340, 30);
		panel.add(NewMsg);
		panel.add(showUsrName);
		panel.add(uname);
		Send.setBounds(375, 410, 95, 30);
		panel.add(Send);
		
		frame.add(panel);
		// End of GUI Design
		
		// When Send button Clicked
		Send.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				out.println(NewMsg.getText()); // put the value of NewMsg area in the out;
				NewMsg.setText(""); // free the text field of NewMsg area.
			}
		});
		
		// When we enter form Jtext field ;
		NewMsg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				out.println(NewMsg.getText()); // put the value of NewMsg area in the out;
				NewMsg.setText(""); // free the text field of NewMsg area.
			}
		});
	}
	
	// This function prompt for the user name 
	private String getUserName()
	{
		return JOptionPane.showInputDialog(frame, "User Name : ", "Login Dialgo", JOptionPane.PLAIN_MESSAGE);
	}
	
	// This function connect to out server and processs all the messages.
	// The ServerSocket and Socket class can throws an exception so in function name we must throws IOException.
	// or we can use try-catch block for handle exception; 
	private void connectToServer () throws IOException
	{
		//ServerSocket serverSocket = new ServerSocket(PORT);
		Socket socket = new Socket("localhost", PORT);
		//Socket socket = new Socket("IP address of server Computer", PORT);
		
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		
		// Process all messages from server, according to the protocol.
        while (true) 
        {
            String line = in.readLine(); // read form server;
            if (line.startsWith("SUBMITNAME")) {
            	name = getUserName(); 
                out.println(name); // send name to server from client;
                uname.setText(name);
                uname.setEditable(false);
                frame.setVisible(true);
            } else if (line.startsWith("NAMEACCEPTED")) {
                NewMsg.setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                ChatHistory.append(line.substring(8) + "\n");
            }
        }
	}
	
	public static void main(String[] args) 
	{
		Client myClient = new Client();
		 try {
			myClient.connectToServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
