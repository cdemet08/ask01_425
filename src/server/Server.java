package server;


import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.System.exit;

/**
 * The server side for answer the message to the client
 * Created by cdemet08 on 9/23/16.
 */
public class Server {

	/**
	 * the port number of server
	 */
	private static int portNumber=0;

	/**
	 * the server socket,wait for client to connect
	 */
	private static ServerSocket serverSocket;

	/**
	 * The client socket to send the message
	 */
	private static Socket clientSocket = null;

	/**
	 * The start of the program
	 * @param args
	 */
	public static void main(String[] args) {

		/**
		 * thread number for the client
		 */
		int threadNum=0;

		//	check input parameter
		checkInputParameter(args);

		//	port number initiliaze
		 portNumber = Integer.parseInt(args[0]);
		 System.out.println("The port number for server is:"+portNumber);


		//numRepetitions = Integer.parseInt(args[1]);
		//System.out.println("The repetitions for client:"+numRepetitions);

		//	wait for connection
		try {
			serverSocket = new ServerSocket(portNumber);
			System.out.println("Server started.In port:" + portNumber);
		} catch (Exception e) {
			System.err.println("Port already in use.");
			System.exit(1);
		}


		while (true) {
			try {
				clientSocket = serverSocket.accept();
				System.out.println("Accepted connection : " + clientSocket);

				Thread t = new Thread(new ConnectionThread(clientSocket,threadNum));
				t.start();

				threadNum++;

			} catch (Exception e) {
				System.err.println("Error in connection attempt.");
			}
		}




	}

	/**
	 * check input parameter in the program
	 * @param args
	 */
	private static void checkInputParameter(String[] args) {
		if(args.length < 1) {
			System.err.println("Give the port number and repetitions.");
			exit(1);
		}
	}


}
