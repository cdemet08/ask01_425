package client;

import server.ConnectionThread;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cdemet08 on 9/24/16.
 */
public class UserClientThread implements Runnable {

	//Socket and Stream
	private Socket socket;
	private PrintWriter socketOut ;
	private BufferedReader socketIn ;

	//
	private BufferedReader stdin;

	//	ID client thread
	private int idThread = 0;

	// config variable
	private String serverIPAddress;
	private int port=0;


	/**
	 * Constructor
	 */
	public UserClientThread(String ipAddress,int port,int id) {

		this.serverIPAddress=ipAddress;
		this.port =port;
		this.idThread = id;

	}

	@Override
	public void run() {
		String clientMsg = new String();
		InetAddress IP;
		String ipAddress = new String();


		//
		initializeSocket();

		// take the ip
		IP = takeIP();
		ipAddress = IP.getHostAddress();
		System.out.println("IP of my system is := "+ipAddress);
		//	TODO check what IP address to send

		//	create the client msg to send to server
		clientMsg = createClientMsg(ipAddress);


		//	send the msg to server
		sendMsg(clientMsg);

		// reveive the msg from server
		receiveMsg();





	}


	private void receiveMsg(){

		String serverMsg = new String();

		String lines[];


		// read from socket the message
		try {

			while ((serverMsg = socketIn.readLine()) != null) {

				System.out.println(serverMsg); //print




			}

		} catch (IOException ex) {

		}

	}


	private String createClientMsg(String ipAddress){
		String tempClientMsg = new String();

		//	create the msg to send
		tempClientMsg =  "HELLO\n";
		tempClientMsg += ipAddress + ":"+ this.port;
		tempClientMsg += "\n";
		tempClientMsg += String.valueOf(idThread);
		tempClientMsg += "\n";


		return tempClientMsg;

	}

	/**
	 *
	 */
	private void sendMsg(String clientMsg){


		System.out.println(clientMsg);

		//	send the msg to the server
		socketOut.write(clientMsg);
		socketOut.flush();

	}

	private InetAddress takeIP(){
		InetAddress IP= null;
		try {
			IP = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return IP;

	}

	private void initializeSocket(){

		try {
			socket = new Socket(serverIPAddress, port);
			stdin = new BufferedReader(new InputStreamReader(System.in));
			socketOut = new PrintWriter(socket.getOutputStream(), true);
			socketIn = new BufferedReader( new InputStreamReader(socket.getInputStream()));


		} catch (Exception e) {
			System.err.println("Cannot connect to the server, try again later.");
			System.exit(1);
		}


	}

}


