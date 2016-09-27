package server;

import java.io.*;
import java.net.Socket;
import Object.MessageObject;



/**
 * Created by cdemet08 on 9/24/16.
 */
public class ConnectionThread implements Runnable {


	ObjectOutputStream objectSocketOut ;
	ObjectInputStream objectSocketIn;

	private int maxRequest = 1000;
	private int idServerThread = 0;

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

		System.out.println("request: " + requestNum + " from thread: " + idServerThread);

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

		String payloadMsg = new String();


		//call payload function to create
		//TODO // FIXME: 9/27/16
		//payloadMsg = createPayload();




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
			objectSocketOut = new ObjectOutputStream(clientSocket.getOutputStream());
			objectSocketIn = new ObjectInputStream(clientSocket.getInputStream());

		} catch (Exception e) {
			System.err.println("Cannot connect to the server, try again later.");
			System.exit(1);
		}


	}
}
