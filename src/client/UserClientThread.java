package client;

import server.*;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Object.MessageObject;


/**
 * Created by cdemet08 on 9/24/16.
 */
public class UserClientThread implements Runnable {

	//Socket and Stream
	private Socket socket;
	private PrintWriter socketOut ;
	private BufferedReader socketIn ;

	ObjectOutputStream objectSocketOut ;
	ObjectInputStream objectSocketIn;



	//
	private BufferedReader stdin;

	//	ID client thread
	private int idThread = 0;

	// config variable
	private String serverIPAddress;
	private int port=0;

	//object server
	private MessageObject msgServer = new MessageObject();

	//object from the client
	private MessageObject msgClient = new MessageObject();


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
		this.msgClient = createClientMsg(ipAddress);

		//	send the msg to server
		sendMsg(this.msgClient);

		// reveive the msg from server
		receiveMsg();





	}


	private void receiveMsg(){


		String welcome;


		/// read from socket the message
		try {


			while (( msgServer = (MessageObject) objectSocketIn.readObject()) != null) {

				welcome = msgServer.getServerMsg();
				System.out.println(welcome);

				break;
			}



		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	private MessageObject createClientMsg(String ipAddress){

		MessageObject msgClientToSend = new MessageObject();


		msgClientToSend.setClientMsg("HELLO");
		msgClientToSend.setClientIP_Port(ipAddress + ":" +this.port);
		msgClientToSend.setIdClient(String.valueOf(idThread));


		//call payload function to create
		//TODO // FIXME: 9/27/16
		//payloadMsg = createPayload();

		return msgClientToSend;

	}

	/**
	 *
	 */
	private void sendMsg(MessageObject clientMsg){


		System.out.println("Send msg: "+clientMsg.getClientMsg());

		//	send the object to the client
		try {

			objectSocketOut.writeObject(clientMsg);
			objectSocketOut.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}


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



			stdin = new BufferedReader(new InputStreamReader(System.in));
			objectSocketOut = new ObjectOutputStream(socket.getOutputStream());
			objectSocketIn = new ObjectInputStream(socket.getInputStream());

		} catch (Exception e) {
			System.err.println("Cannot connect to the server, try again later.");
			System.exit(1);
		}


	}

}


