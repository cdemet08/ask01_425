package server;

import java.io.*;
import java.net.Socket;
import java.util.Random;

import Object.MessageObject;



/**
 * Created by cdemet08 on 9/24/16.
 */
public class ConnectionThread implements Runnable {


	ObjectOutputStream objectSocketOut ;
	ObjectInputStream objectSocketIn;

	private int maxRequest = 1000;
	private int idServerThread = 0;
    //@
	//Total Throughput
	public static long TotalThroughput = 0;
	//Number of threads completed
	public static int threadCompleted = 0;

	//
	private BufferedReader stdin;

	//object server
	private MessageObject msgServer = new MessageObject();

	//object from the client
	private MessageObject msgClient = new MessageObject();

	private Socket clientSocket;


	public ConnectionThread(Socket client, int idServerThread) {
		this.clientSocket = client;
		this.idServerThread = idServerThread;
	}


	@Override
	public void run() {

		int requestNum=0;

		String userIDStr = new String();


		//	initialize thread socket
		initializeSocket();

        //@
        //start time counter
        long start = System.currentTimeMillis();
		while(maxRequest > requestNum) {


			// call function to receive the msg from client
			userIDStr = receiveMsg();

			//	exit - close the thread
			if(userIDStr.matches("stop")){
				// stop from while
				break;
			}

			// create msg to send to server
			createMsgToSend(userIDStr);

			//	send the answer to the client
			sendMsgToClient(this.msgServer);

			requestNum++;

		}
		//@
		//User and thread done, count++
        threadCompleted++;

		System.out.println("request: " + requestNum + " from thread: " + idServerThread);

        //@
        //diff = time for user to be done
        long diff = System.currentTimeMillis() - start;
        long throughput = requestNum/diff;
        System.out.println("Throughput: " + throughput + "rpms");

        //Calculate and print Total Throughput so far
        TotalThroughput = ((TotalThroughput*threadCompleted) + thoughput)/(threadCompleted + 1);
        System.out.println("Total Throughput: " + TotalThroughput + "rpms");
	}

	private String receiveMsg(){

		String ipAndPortClient = new String();

		String userIDStr = new String();

		// read from socket the message
		try {

			while (( this.msgClient = (MessageObject) objectSocketIn.readObject()) != null) {

				userIDStr = this.msgClient.getIdClient();

				if(msgClient.getClientMsg().matches("STOP")){
					userIDStr="stop";
				}

				ipAndPortClient = this.msgClient.getClientIP_Port();

				//System.out.println("ip client: " + ipAndPortClient);

				break;
			}

		} catch (ClassNotFoundException e) {
			System.err.println("first");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("sec");
			e.printStackTrace();
		}

		//	return the ID client thread
		return userIDStr;

	}

	private void createMsgToSend(String userId){

		this.msgServer = new MessageObject();

		this.msgServer.setServerMsg("WELCOME " + userId);

		//create the payload function
		createPayload();

	}


	private void sendMsgToClient(MessageObject objectServerSend){


		//	send the object to the client
		try {

			objectSocketOut.writeObject(objectServerSend);
			objectSocketOut.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}



	/**
	 *
	 * create the payload msg to send to the client
	 * @return
	 */
	private void  createPayload(){

		Random rand = new Random();
		// size in kb we must convert in byte
		int size = rand.nextInt(1700);
		size = size + 301;
		size = size * 1024; //for byte

		byte[] payloadTemp = new byte[size];

		for(int i=0; i < size; i++){
			payloadTemp[i] = (byte) rand.nextInt(255);
		}

		// set the payload in msgServer
		msgServer.setPayloadServer(payloadTemp);


	}

	private void initializeSocket() {

		try {


			stdin = new BufferedReader(new InputStreamReader(System.in));
			objectSocketOut = new ObjectOutputStream(clientSocket.getOutputStream());
			objectSocketIn = new ObjectInputStream(clientSocket.getInputStream());

		} catch (Exception e) {
			System.err.println("Cannot connect to the server, try again later.");
			System.exit(1);
		}


	}
}
