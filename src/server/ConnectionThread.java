package server;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cdemet08 on 9/24/16.
 */
public class ConnectionThread implements Runnable {

	//Socket and Stream
	private PrintWriter socketOut;
	private BufferedReader socketIn;

	//
	private BufferedReader stdin;

	private int clientID = 0;

	private Socket clientSocket;


	public ConnectionThread(Socket client) {
		this.clientSocket = client;
	}


	@Override
	public void run() {

		initializeSocket();

		String clientSelection;

		try {

			while ((clientSelection = socketIn.readLine()) != null) {
				System.out.println(clientSelection);
			}

		} catch (IOException ex) {
			
		}

	}


	private void initializeSocket() {

		try {

			stdin = new BufferedReader(new InputStreamReader(System.in));
			socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
			socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


		} catch (Exception e) {
			System.err.println("Cannot connect to the server, try again later.");
			System.exit(1);
		}


	}
}
