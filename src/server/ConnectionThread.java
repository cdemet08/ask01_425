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


	private Socket clientSocket;


	public ConnectionThread(Socket client) {
		this.clientSocket = client;
	}


	@Override
	public void run() {
		
		//
		String serverMsg = new String();

		String userIDStr = new String();


		//	initialize thread socket
		initializeSocket();

		// call function to receive the msg from client
		userIDStr = receiveMsg();

		// create msg to send to server
		serverMsg = createMsgToSend(userIDStr);

		//	send the answer to the client
		sendMsgToClient(serverMsg);



	}

	private String createMsgToSend(String userId){

		String msgToSend = new String();

		String payloadMsg = new String();

		msgToSend = "WELCOME " + userId;
		msgToSend += "\n";

		//call payload function to create
		//TODO // FIXME: 9/27/16
		//payloadMsg = createPayload();

		msgToSend += payloadMsg;

		return msgToSend;

	}


	private void sendMsgToClient(String msgToSend){


		//	send the msg to the server
		socketOut.write(msgToSend);
		socketOut.flush();

	}

	private String  receiveMsg(){

		String clientMsg = new String();

		String lines[];

		String userIDStr = new String();

		// read from socket the message
		try {

			while ((clientMsg = socketIn.readLine()) != null) {



				System.out.println(clientMsg); //print

				lines = clientMsg.split("\\n");
				System.out.println("len:"+lines.length);

				//userIDStr = lines[2];



			}

		} catch (IOException ex) {

		}

		//	return the ID client thread
		return userIDStr;

	}

	/**
	 * TODO
	 * crete the payload msg to send to the client
	 * @return
	 */
	private String  createPayload(){
		String payloadTemp = new String();

		return payloadTemp;
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
