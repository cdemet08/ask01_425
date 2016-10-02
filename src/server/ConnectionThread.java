package server;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.Socket;
import java.util.Random;
import Object.MessageObject;
import javax.management.*;


/**
 * The thread for server side to answer and get the message from the client
 * Created by cdemet08 on 9/24/16.
 */
public class ConnectionThread implements Runnable {


	/**
	 * The stream to send and get the message from the client
	 */
	ObjectOutputStream objectSocketOut ;
	ObjectInputStream objectSocketIn;

	/**
	 * max request for message
	 */
	private int maxRequest = 1000;

	/**
	 * the server id thread
	 */
	private int idServerThread = 0;

	/**
	 * Message object for the server
	 */
	private MessageObject msgServer = new MessageObject();

	/**
	 * Message object for the client
 	 */
	private MessageObject msgClient = new MessageObject();

	/**
	 * create the client socket to connect
	 */
	private Socket clientSocket;


	/**
	 * ConnectionThread constructor to set the client socket and id thread server
	 * @param client
	 * @param idServerThread
	 */
	public ConnectionThread(Socket client, int idServerThread) {
		this.clientSocket = client;
		this.idServerThread = idServerThread;
	}


	/**
	 * run ConnectionThread function
	 */
	@Override
	public void run() {
		int mb = 1024 * 1024;
		Runtime memory = Runtime.getRuntime();

		long totalMemory = 0;

		System.out.println("Total memory:"+memory.totalMemory()/mb+" free:"+memory.freeMemory()/mb +" mem:"+memory.maxMemory()/mb);
		int requestNum=0;

		String userIDStr = new String();


		//	initialize thread socket
		initializeSocket();

		//	take the millisecond before send
		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();

		long startFullThread = System.currentTimeMillis();

		long counterSec =0;

		int requestPerSecond = 0;

		long cpuLoad = 0;

		while(maxRequest > requestNum) {


			cpuLoad += getProcessCpuLoad();
			totalMemory += (memory.totalMemory());

			// call function to receive the msg from client
			userIDStr = receiveMsg();

			cpuLoad += getProcessCpuLoad();

			//	exit - close the thread
			if(userIDStr.matches("stop")){
				// stop from while
				break;
			}

			// create msg to send to server
			createMsgToSend(userIDStr);


			cpuLoad += getProcessCpuLoad();


			//	send the answer to the client
			sendMsgToClient(this.msgServer);

			cpuLoad += getProcessCpuLoad();

			requestNum++;

			//	request per second to count
			requestPerSecond++;

			end = System.currentTimeMillis();

			//take one sec
			counterSec = (end - start)/1000;

			//	one sec pass
			if(counterSec >= 1){
				System.out.println("Thread: "+idServerThread +" counterPerSec: "+requestPerSecond);

				//	reset the counter and start time for throughput.
				requestPerSecond=0;
				start = System.currentTimeMillis();

			}


		}	// end of while


		// cpu load
		cpuLoad = cpuLoad / (requestNum*4);
		System.out.println("cpu: " + cpuLoad +"%");


		long endFullThread = System.currentTimeMillis();

		long fullTimeThread = (endFullThread - startFullThread) / 1000;

		double throughput = requestNum / fullTimeThread;

		System.out.println("Thread: "+idServerThread+" Request: "+requestNum +" Throughput: "+throughput);

		// total memory

		totalMemory = totalMemory / mb;
		System.out.println("Total memory:"+(totalMemory/requestNum) + " Mb");


	}


	/**
	 * Get the cpu load of the computer
	 * @return cpuload
	 */
	private double getProcessCpuLoad() {

		MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
		ObjectName name    = null;
		try {
			name = ObjectName.getInstance("java.lang:type=OperatingSystem");
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		}
		AttributeList list = null;
		try {
			list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		} catch (ReflectionException e) {
			e.printStackTrace();
		}

		if (list.isEmpty())     return Double.NaN;

		Attribute att = (Attribute)list.get(0);
		Double value  = (Double)att.getValue();

		// usually takes a couple of seconds before we get real values
		if (value == -1.0)      return Double.NaN;
		// returns a percentage value with 1 decimal point precision
		return ((int)(value * 1000) / 10.0);
	}


	/**
	 * wait to get the message from client
	 * @return message
	 */
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


	/**
	 * create the message to send to the client
	 * @param userId
	 */
	private void createMsgToSend(String userId){

		this.msgServer.setServerMsg("WELCOME " + userId);

		//create the payload function
		createPayload();

	}


	/**
	 * send the message to the client
	 * @param objectServerSend
	 */
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
		this.msgServer.setPayloadServer(payloadTemp);


	}

	/**
	 * Initialize the socket to send and receive the message
	 */
	private void initializeSocket() {

		try {

			objectSocketOut = new ObjectOutputStream(clientSocket.getOutputStream());
			objectSocketIn = new ObjectInputStream(clientSocket.getInputStream());

		} catch (Exception e) {
			System.err.println("Cannot connect to the server, try again later.");
			System.exit(1);
		}


	}
}
