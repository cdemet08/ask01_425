package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import Object.MessageObject;


/**
 * The class to run for every user and send request to the server
 * Created by cdemet08 on 9/24/16.
 */
public class UserClientThread implements Runnable {

	/**
	 * Socket to connect to the server
 	 */
	private Socket socket;

	/**
	 * Socket to send the data to the server
	 */
	ObjectOutputStream objectSocketOut ;

	/**
	 * Socket to receive the data from the server
	 */
	ObjectInputStream objectSocketIn;


	/**
	 * The client id thread
 	 */
	private int idThread = 0;

	/**
	 * The server ip address
 	 */
	private String serverIPAddress;

	/**
	 * The server port
	 */
	private int port=0;

	/**
	 * The message object from receive from the server
 	 */
	private MessageObject msgServer = new MessageObject();

	/**
	 * The message object to send to the server
 	 */
	private MessageObject msgClient = new MessageObject();


	/**
	 * Constructor the user thread
	 * @param ipAddress
	 * @param port
	 * @param id
	 */
	public UserClientThread(String ipAddress,int port,int id) {

		this.serverIPAddress=ipAddress;
		this.port =port;
		this.idThread = id;

	}

	@Override
	public void run() {

		InetAddress IP;
		String ipAddress = new String();

		int simulationTime = 300;


		//
		initializeSocket();

		// take the ip
		IP = takeIP();
		ipAddress = IP.getHostAddress();
		System.out.println("IP of my system is := "+ipAddress);


		long start = System.currentTimeMillis();

		for(int i = 0; i < simulationTime; i++ ) {

			//	create the client msg to send to server
			createClientMsg(ipAddress);

			//	send the msg to server
			sendMsg(this.msgClient);


			// reveive the msg from server
			receiveMsg();



		}

		long end = System.currentTimeMillis();

		long allRTT = end - start;
		double latency = allRTT / simulationTime ;

		System.out.println("Client: "+idThread+" RTT all: " + latency);


		createStopClientMsg();

		sendMsg(this.msgClient);





	}

	/**
	 * Create stop message to send to the server
	 */
	private void createStopClientMsg(){

		this.msgClient = new MessageObject();


		this.msgClient.setClientMsg("STOP");
		this.msgClient.setIdClient(String.valueOf(idThread));


	}

	/**
	 * Wait to take the message from the server
	 */
	private void receiveMsg(){

		String welcome;

		/// read from socket the message
		try {

			while (( this.msgServer = (MessageObject) objectSocketIn.readObject()) != null) {

				welcome = this.msgServer.getServerMsg();

				break;
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 *	Create the Message to send to the server
	 * @param ipAddress
	 */
	private void createClientMsg(String ipAddress){

		this.msgClient = new MessageObject();


		this.msgClient.setClientMsg("HELLO");
		this.msgClient.setClientIP_Port(ipAddress + ":" +this.port);
		this.msgClient.setIdClient(String.valueOf(idThread));


	}

	/**
	 * Send the message
	 */
	private void sendMsg(MessageObject clientMsgToSend){


		//System.out.println("Send msg: "+clientMsgToSend.getClientMsg());

		//	send the object to the client
		try {

			objectSocketOut.writeObject(clientMsgToSend);
			objectSocketOut.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	/**
	 *	Take the ip to the client
	 * @return
	 */
	private InetAddress takeIP(){
		InetAddress IP= null;
		try {
			IP = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return IP;

	}

	/**
	 * Initialize the Socket and Stream
	 */
	private void initializeSocket(){

		try {
			socket = new Socket(serverIPAddress, port);

			objectSocketOut = new ObjectOutputStream(socket.getOutputStream());
			objectSocketIn = new ObjectInputStream(socket.getInputStream());

		} catch (Exception e) {
			System.err.println("Cannot connect to the server, try again later.");
			System.exit(1);
		}


	}

}


