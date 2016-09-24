package client;

import server.ConnectionThread;

import java.io.*;
import java.net.Socket;
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

	private int idThread = 0;

	// config variable
	private String ipAddress;
	static int port=0;

	private Socket clientSocket;
	private BufferedReader in = null;


	/**
	 * Constructor
	 */
	public UserClientThread(String ipAddress,int port) {

		this.ipAddress=ipAddress;
		this.port=port;

	}

	@Override
	public void run() {
		String clientSelection = new String();

		//
		initializeSocket();

		socketOut.write("HELLO");
		socketOut.flush();


	}


	private void initializeSocket(){

		try {
			socket = new Socket(ipAddress, port);
			stdin = new BufferedReader(new InputStreamReader(System.in));
			socketOut = new PrintWriter(socket.getOutputStream(), true);
			socketIn = new BufferedReader( new InputStreamReader(socket.getInputStream()));


		} catch (Exception e) {
			System.err.println("Cannot connect to the server, try again later.");
			System.exit(1);
		}


	}

}


