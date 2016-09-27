package server;


import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.System.exit;

/**
 * Created by cdemet08 on 9/23/16.
 */
public class Server {

	private static int portNumber=0;

	private static int numRepetitions=0;

	private static ServerSocket serverSocket;

	private static Socket clientSocket = null;

	public static void main(String[] args) {

		int threadNum=0;

		//	check input parameter
		checkInputParameter(args);

		//	port number initiliaze
		 portNumber = Integer.parseInt(args[0]);
		 System.out.println("The port number for server is:"+portNumber);


		//	TODO fix this
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

	private static void checkInputParameter(String[] args) {
		if(args.length < 1) {
			System.err.println("Give the port number and repetitions.");
			exit(1);
		}
	}


}
