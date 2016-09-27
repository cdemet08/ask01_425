package client;

import server.*;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Object.MessageObject;
//time libraries
import java.sql.Timestamp;
import java.util.Date;


/**
 * Created by cdemet08 on 9/24/16.
 */
public class UserClientThread implements Runnable {

	//Socket and Stream
	private Socket socket;

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

		InetAddress IP;
		String ipAddress = new String();

		int simulationTime = 300;


		//
		initializeSocket();

		// take the ip
		IP = takeIP();
		ipAddress = IP.getHostAddress();
		System.out.println("IP of my system is := "+ipAddress);
		//	TODO check what IP address to send

		//	run for 300+ times
		// TODO random the simulation time min 300

		for(int i = 0; i < simulationTime; i++ ) {

			//	create the client msg to send to server
			createClientMsg(ipAddress);

			//	send the msg to server
			sendMsg(this.msgClient);

            //@
			//sent timestamp
            java.util.Date sTs= new java.util.Date();

			// reveive the msg from server
			receiveMsg();

            //@
            //received timestamp
            java.util.Date rTs= new java.util.Date();
            long latency = Math.abs(sTs - rTs);
            System.out.println("Latency :" + latency + " ms\n");
		}

		createStopClientMsg();

		sendMsg(this.msgClient);

		//TODO send STOP msg to the server




	}



	private void createStopClientMsg(){

		this.msgClient = new MessageObject();


		this.msgClient.setClientMsg("STOP");
		this.msgClient.setIdClient(String.valueOf(idThread));


	}


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


	private void createClientMsg(String ipAddress){

		this.msgClient = new MessageObject();


		this.msgClient.setClientMsg("HELLO");
		this.msgClient.setClientIP_Port(ipAddress + ":" +this.port);
		this.msgClient.setIdClient(String.valueOf(idThread));


	}

	/**
	 *
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


