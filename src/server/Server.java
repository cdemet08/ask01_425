package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.exit;

/**
 * Created by cdemet08 on 9/23/16.
 */
public class Server {

	static int portNumber=0;

	static int numRepetitions=0;

	public static void main(String[] args) {

		//	check input parameter
		checkInputParameter(args);

		//	port number initiliaze
		 portNumber = Integer.parseInt(args[0]);
		 System.out.println("The port number for server is:"+portNumber);

		//
		numRepetitions = Integer.parseInt(args[1]);
		System.out.println("The repetitions for client:"+numRepetitions);





	}

	private static void checkInputParameter(String[] args) {
		if(args.length < 1) {
			System.out.println("Give the port number and repetitions.");
			exit(0);
		}
	}


}
